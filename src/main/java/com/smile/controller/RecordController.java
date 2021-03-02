package com.smile.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.smile.entity.Appeal;
import com.smile.entity.Record;
import com.smile.entity.common.other.Result;
import com.smile.service.AppealService;
import com.smile.service.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/record")
public class RecordController {

    private final RecordService recordService;
    private final AppealService appealService;

    @Autowired
    public RecordController(RecordService recordService, AppealService appealService) {
        this.recordService = recordService;
        this.appealService = appealService;
    }

    @PostMapping("/getDepartmentRecord")
    public Result getDepartmentRecord(@RequestParam("userId") String userId) {
        return recordService.getDepartmentRecord(userId);
    }

    @PostMapping("/getTchRecord")
    public Result getTchRecord(@RequestParam("id") String id) {
        return recordService.getTchRecord(id);
    }

    @GetMapping("/getRecordYearOption")
    public Result getRecordYearOption() {
        ArrayList<String> years = new ArrayList<>();
        List<Record> yearList = recordService.list(new QueryWrapper<Record>().select("year").groupBy("year"));
        for (Record record : yearList) {
            years.add(record.getYear());
        }
        return Result.success(years);
    }

    @PostMapping("/admitRemoveRecordBatch")
    public Result admitRemoveRecordBatch(@RequestBody List<String> recordIds) {
        UpdateWrapper<Appeal> appealUpdateWrapper = new UpdateWrapper<>();
        appealUpdateWrapper.in("record_id",recordIds);
        appealService.remove(appealUpdateWrapper);
        recordService.removeByIds(recordIds);
        return Result.success("删除成功");
    }
}

