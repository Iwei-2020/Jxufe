package com.smile.utils.listen;

import cn.hutool.core.util.IdUtil;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.smile.entity.Attendance;
import com.smile.entity.Record;
import com.smile.entity.User;
import com.smile.service.AttendanceService;
import java.util.ArrayList;
import java.util.List;

/**
 * @author smilePlus
 */
public class AttendanceListen extends AnalysisEventListener<Attendance>  {
    private final AttendanceService attendanceService;
    private final String executorId;

    public AttendanceListen(AttendanceService attendanceService, String executorId) {
        this.attendanceService = attendanceService;
        this.executorId = executorId;
    }

    private static final int BATCH_COUNT = 5;
    List<Attendance> list = new ArrayList<>();

    @Override
    public void invoke(Attendance attendance, AnalysisContext analysisContext) {
        User user = attendanceService.findUserIdByName(attendance.getName());
        attendance.setId(IdUtil.objectId());
        attendance.setUserId(user.getId());
        Record record = new Record();
        record.setExecutorId(executorId);
        record.setAttendanceId(attendance.getId());
        record.setTchId(user.getId());
        record.setType(1);
        attendanceService.attendanceCompute(user.getTagTch(),record,attendance);
        list.add(attendance);
        if (list.size() >= BATCH_COUNT) {
            attendanceService.saveBatch(list);
            list.clear();
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        attendanceService.saveBatch(list);
    }

}
