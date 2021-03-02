package com.smile.service;

import com.smile.entity.Attendance;
import com.baomidou.mybatisplus.extension.service.IService;
import com.smile.entity.Record;
import com.smile.entity.User;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author thePassionate
 * @since 2020-11-30
 */
public interface AttendanceService extends IService<Attendance> {

    /**
     * TODO 通过教工姓名获取教工的id
     * @param name 教工姓名
     * @author smilePlus
     * @date 2020/12/4 20:28
     * @return java.lang.String
     */
    User findUserIdByName(String name);

    /**
     * TODO
     * @param tchTag 判断是否为行政人员
     * @param record 记录
     * @param attendance 考勤记录
     * @author smilePlus
     * @date 2020/12/4 20:29
     */
    void attendanceCompute(Integer tchTag, Record record, Attendance attendance);

    /**
     * TODO
     * @param id 教师id
     * @author smilePlus
     * @date 2020/12/4 20:33
     * @return java.util.List<com.smile.entity.Attendance>
     */

    List<Attendance> getTchAttendance(String id);
}
