package com.smile.controller;


import com.smile.entity.Schedule;
import com.smile.entity.common.other.Result;
import com.smile.service.ScheduleService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author thePassionate
 * @since 2020-11-29
 */
@RestController
@RequestMapping("/schedule")
public class ScheduleController {

    private final ScheduleService scheduleService;

    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @PostMapping("/saveSchedule")
    public Result saveSchedule(@RequestBody List<Schedule> schedules) {
        scheduleService.saveOrUpdateBatch(schedules);
        return Result.success("保存成功");
    }

    @PostMapping("/getUserSchedule")
    public Result getUserSchedule(@RequestParam("userId") String userId) {
        return Result.success(scheduleService.getUserSchedule(userId));
    }

}

