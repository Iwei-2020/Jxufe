package com.smile.controller;


import com.alibaba.excel.EasyExcel;
import com.smile.entity.Attendance;
import com.smile.entity.common.other.Result;
import com.smile.service.AttendanceService;
import com.smile.utils.listen.AttendanceListen;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author thePassionate
 * @since 2020-11-30
 */
@RestController
@RequestMapping("/attendance")
public class AttendanceController {

    private final AttendanceService attendanceService;

    public AttendanceController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    @PostMapping("/uploadAttendanceFile")
    @Transactional(rollbackFor = RuntimeException.class)
    public Result uploadAttendanceFile(@RequestParam("file") MultipartFile file,
                                       @RequestParam("executorId") String executorId) {
        try { EasyExcel.read(file.getInputStream(),
                Attendance.class, new AttendanceListen(attendanceService,executorId)).doReadAll();
            return Result.success("考勤信息导入成功");
        } catch (IOException e) {
            e.printStackTrace();
            return Result.error("考勤信息导入失败");
        }
    }

    @PostMapping("/getSpecialAttendance")
    public Result getSpecialAttendance(@RequestParam("id") String id) {
        return Result.success(attendanceService.getById(id));
    }

    @PostMapping("/getTchAttendance")
    public Result getTchAttendance(@RequestParam("id") String id) {
        return Result.success(attendanceService.getTchAttendance(id));
    }
}

