package com.smile.utils.listen;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.alibaba.excel.read.metadata.holder.ReadSheetHolder;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.smile.entity.Schedule;
import com.smile.entity.User;
import com.smile.entity.common.dto.UserDto;
import com.smile.entity.common.exception.BizException;
import com.smile.service.ScheduleService;
import com.smile.service.UserService;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO
 *
 * @author smilePlus
 * @version 1.0
 * @date 2021/1/3 3:13
 */
public class ScheduleListen extends AnalysisEventListener<Schedule> {

    private static final int BATCH_COUNT = 5;
    List<Schedule> schedules = new ArrayList<>();
    private final ScheduleService scheduleService;
    private final UserService userService;

    public ScheduleListen(ScheduleService scheduleService, UserService userService) {
        this.scheduleService = scheduleService;
        this.userService = userService;
    }

    @Override
    public void invoke(Schedule schedule, AnalysisContext analysisContext) {
        ReadSheetHolder readSheetHolder = analysisContext.readSheetHolder();
        String sheetName = readSheetHolder.getSheetName();
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("name",sheetName);
        User user = userService.getOne(userQueryWrapper);
        if (user == null) {
            throw new BizException(0,"导入课程表失败!用户不存在");
        }
        schedule.setUserId(user.getId());
        schedules.add(schedule);
        if (schedules.size() >= BATCH_COUNT) {
            for (Schedule schedule1 : schedules) {
                QueryWrapper<Schedule> scheduleQueryWrapper = new QueryWrapper<>();
                scheduleQueryWrapper
                        .eq("user_id",schedule1.getUserId())
                        .eq("no",schedule1.getNo());
                Schedule schedule2 = scheduleService.getOne(scheduleQueryWrapper);
                if (schedule2 != null) {
                    schedule1.setId(schedule2.getId());
                }
                scheduleService.saveOrUpdate(schedule1);
            }
            schedules.clear();
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        for (Schedule schedule1 : schedules) {
            QueryWrapper<Schedule> scheduleQueryWrapper = new QueryWrapper<>();
            scheduleQueryWrapper
                    .eq("user_id",schedule1.getUserId())
                    .eq("no",schedule1.getNo());
            Schedule schedule2 = scheduleService.getOne(scheduleQueryWrapper);
            if (schedule2 != null) {
                schedule1.setId(schedule2.getId());
            }
            scheduleService.saveOrUpdate(schedule1);
        }
    }
}
