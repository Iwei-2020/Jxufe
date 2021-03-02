package com.smile.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.smile.entity.Appeal;
import com.smile.entity.Record;
import com.smile.entity.common.other.Result;
import com.smile.service.AppealService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author thePassionate
 * @since 2020-11-11
 */
@RestController
@RequestMapping("/appeal")
public class AppealController {

    private final AppealService appealService;

    public AppealController(AppealService appealService) {
        this.appealService = appealService;
    }

    @PostMapping("/tchAppeal")
    public Result tchAppeal(@RequestBody Appeal appeal) {
        return appealService.tchAppeal(appeal);
    }

    @PostMapping("/cancelAppeal")
    public Result cancelAppeal(@RequestParam("recordId") String recordId) {
        return appealService.cancelAppeal(recordId);
    }

    @PostMapping("/seeAppeal")
    public Result seeAppeal(@RequestParam("recordId") String recordId) {
        return appealService.seeAppeal(recordId);
    }

    @PostMapping("/seeFeedback")
    public Result seeFeedback(@RequestParam("recordId") String recordId) {
        return appealService.seeFeedback(recordId);
    }

    @PostMapping("/getDepartmentAppeal")
    public Result getDepartmentAppeal(@RequestParam("userId") String userId) {
        return appealService.getDepartmentAppeal(userId);
    }

    @PostMapping("/handleAppeal")
    public Result handleAppeal(@RequestParam("appealId") String appealId,
                               @RequestParam("feedback") String feedback) {

        return appealService.handleAppeal(appealId,feedback);
    }

    @GetMapping("/getAppealYearOption")
    public Result getAppealYearOption() {
        ArrayList<String> years = new ArrayList<>();
        List<Appeal> yearList = appealService.list(new QueryWrapper<Appeal>().select("year").groupBy("year"));
        for (Appeal appeal : yearList) {
            years.add(appeal.getYear());
        }
        return Result.success(years);
    }

    @Transactional(rollbackFor = RuntimeException.class)
    @PostMapping("/admitRemoveAppealBatch")
    public Result admitRemoveAppealBatch(@RequestBody List<String> appealIds) {
        appealService.removeByIds(appealIds);
        return Result.success("删除成功");
    }
}

