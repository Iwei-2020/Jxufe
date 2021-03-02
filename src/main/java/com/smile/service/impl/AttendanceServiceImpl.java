package com.smile.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.smile.entity.*;
import com.smile.entity.common.exception.BizException;
import com.smile.entity.common.util.RuleStatic;
import com.smile.mapper.*;
import com.smile.service.AttendanceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.smile.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author thePassionate
 * @since 2020-11-30
 */
@Service
public class AttendanceServiceImpl extends ServiceImpl<AttendanceMapper, Attendance> implements AttendanceService {

    private final UserService userService;
    private final ScheduleMapper scheduleMapper;
    private final UserMapper userMapper;
    private final AttendanceMapper attendanceMapper;
    private final RuleMapper ruleMapper;
    private final RecordMapper recordMapper;
    private final SystemYearMapper systemYearMapper;

    public AttendanceServiceImpl(UserService userService, ScheduleMapper scheduleMapper, UserMapper userMapper, AttendanceMapper attendanceMapper, RuleMapper ruleMapper, RecordMapper recordMapper, SystemYearMapper systemYearMapper) {
        this.userService = userService;
        this.scheduleMapper = scheduleMapper;
        this.userMapper = userMapper;
        this.attendanceMapper = attendanceMapper;
        this.ruleMapper = ruleMapper;
        this.recordMapper = recordMapper;
        this.systemYearMapper = systemYearMapper;
    }

    @Override
    public User findUserIdByName(String name) {
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("name",name);
        User user = userMapper.selectOne(userQueryWrapper);
        if (user == null) {
            throw new BizException(0,"导入失败！系统中不存在该用户");
        }
        return user;
    }

    @Override
    public void attendanceCompute(Integer tchTag,Record record, Attendance attendance) {
        // 查询课表
        QueryWrapper<Schedule> scheduleQueryWrapper = new QueryWrapper<>();
        scheduleQueryWrapper.eq("user_id",attendance.getUserId());
        List<Schedule> schedules = scheduleMapper.selectList(scheduleQueryWrapper);
        if (schedules == null) {
            throw new BizException(0, "导入失败！用户的课表不存在");
        }
        attendanceComputeUtils(schedules, attendance, record, tchTag);
    }

    @Override
    public List<Attendance> getTchAttendance(String id) {
        QueryWrapper<Attendance> attendanceQueryWrapper = new QueryWrapper<>();
        attendanceQueryWrapper.eq("user_id",id);
        return attendanceMapper.selectList(attendanceQueryWrapper);
    }



    private void attendanceComputeUtils(List<Schedule> schedules,Attendance attendance,Record record, Integer tchTag) {
        record.setYear(systemYearMapper.getSystemYear());
        String zhengChang = "正常", queKa = "缺卡", chiDao = "迟到";
        boolean shangWuYouKe = false;
        boolean xiaWuYouKe = false;
        ArrayList<String> classList = new ArrayList<>();
        HashMap<String, Integer> weekdayMap = new HashMap<String, Integer>(7) {
            {
                put("星期一",1);
                put("星期二",2);
                put("星期三",3);
                put("星期四",4);
                put("星期五",5);
                put("星期六",0);
                put("星期日",0);
            }
        };
        String attDate = attendance.getAttDate();
        String weekday = attDate.substring(attDate.lastIndexOf(" ") + 1);
        int type = weekdayMap.get(weekday);
        for (Schedule schedule : schedules) {
            switch (type){
                case 1:
                    classList.add(schedule.getMonday());
                    break;
                case 2:
                    classList.add(schedule.getTuesday());
                    break;
                case 3:
                    classList.add(schedule.getWednesday());
                    break;
                case 4:
                    classList.add(schedule.getThursday());
                    break;
                case 5:
                    classList.add(schedule.getFriday());
                    break;
                default:
                    return;
            }
        }
        for (int i = 0; i < classList.size(); i++) {
            if (i <= 3 && classList.get(i) != null) {
                shangWuYouKe = true;
            }
            if (i >= 4 && classList.get(i) != null) {
                xiaWuYouKe = true;
            }
        }

        Record copyRecord1 = new Record();
        BeanUtils.copyProperties(record,copyRecord1);
        if (shangWuYouKe) {
            copyRecord1.setRuleId(RuleStatic.shangKe);
            copyRecord1.setAddScore((float) 2);
            copyRecord1.setDesScore((float) 0);
            userService.modifyScore(copyRecord1);
        } else {
            // 如果有打卡记录
            if (attendance.getTimeWork1() != null || attendance.getTimeOff1() != null) {
                if (tchTag == 1) {
                    if (zhengChang.equals(attendance.getWorkResult1())
                            && zhengChang.equals(attendance.getOffResult1())) {
                        copyRecord1.setRuleId(RuleStatic.zuoBan);
                        copyRecord1.setAddScore((float) 2);
                        copyRecord1.setDesScore((float) 0);
                    } else if (chiDao.equals(attendance.getWorkResult1())) {
                        copyRecord1.setRuleId(RuleStatic.chiDao);
                        copyRecord1.setAddScore((float) 0);
                        copyRecord1.setDesScore((float) -1);
                    } else if (queKa.equals(attendance.getWorkResult1())) {
                        copyRecord1.setRuleId(RuleStatic.kuangKe);
                        copyRecord1.setAddScore((float) 0);
                        copyRecord1.setDesScore((float) -4);
                    }
                } else {
                    copyRecord1.setRuleId(RuleStatic.zuoBan);
                    copyRecord1.setAddScore((float) 2);
                    copyRecord1.setDesScore((float) 0);
                }
                userService.modifyScore(copyRecord1);
            }
        }

        Record copyRecord2 = new Record();
        BeanUtils.copyProperties(record,copyRecord2);
        if (xiaWuYouKe) {
            copyRecord2.setRuleId(RuleStatic.shangKe);
            copyRecord2.setAddScore((float) 2);
            copyRecord2.setDesScore((float) 0);
            userService.modifyScore(copyRecord2);
        }  else {
            if (attendance.getTimeWork2() != null || attendance.getTimeOff2() != null) {
                if (tchTag == 1) {
                    if (zhengChang.equals(attendance.getWorkResult2())
                            && zhengChang.equals(attendance.getOffResult2())) {
                        copyRecord2.setRuleId(RuleStatic.zuoBan);
                        copyRecord2.setAddScore((float) 2);
                        copyRecord2.setDesScore((float) 0);
                    } else if (chiDao.equals(attendance.getWorkResult2())) {
                        copyRecord2.setRuleId(RuleStatic.chiDao);
                        copyRecord2.setAddScore((float) 0);
                        copyRecord2.setDesScore((float) -1);
                    } else if (queKa.equals(attendance.getWorkResult2())) {
                        copyRecord2.setRuleId(RuleStatic.kuangKe);
                        copyRecord2.setAddScore((float) 0);
                        copyRecord2.setDesScore((float) -4);
                    }
                } else {
                    copyRecord2.setRuleId(RuleStatic.zuoBan);
                    copyRecord2.setAddScore((float) 2);
                    copyRecord2.setDesScore((float) 0);
                }
                userService.modifyScore(copyRecord2);
            }
        }
    }
}
