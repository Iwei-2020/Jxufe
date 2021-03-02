package com.smile.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.smile.entity.Rule;
import com.smile.entity.User;
import com.smile.entity.UserScore;
import com.smile.entity.common.other.Result;
import com.smile.service.RuleService;
import com.smile.service.SystemYearService;
import com.smile.service.UserScoreService;
import com.smile.service.UserService;
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
@RequestMapping("/rule")
public class RuleController {

    private final RuleService ruleService;
    private final UserService userService;
    private final UserScoreService userScoreService;
    private final SystemYearService systemYearService;

    public RuleController(RuleService ruleService, UserService userService, UserScoreService userScoreService, SystemYearService systemYearService) {
        this.ruleService = ruleService;
        this.userService = userService;
        this.userScoreService = userScoreService;
        this.systemYearService = systemYearService;
    }

    @PostMapping("/getRule")
    public Result getRule(@RequestParam("userId") String userId) {
        return Result.success(ruleService.getRule(userId));
    }

    @PostMapping("/saveRule")
    public Result saveRule(@RequestBody List<Rule> rules) {
        ruleService.saveOrUpdateBatch(rules);
        return Result.success("保存成功");
    }

    @PostMapping("/addNewClassifyRule")
    public Result addNewClassifyRule(@RequestBody Rule rule) {
        ruleService.save(rule);
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<User>().eq("logo", -1);
        for (User user : userService.list(userQueryWrapper)) {
            UserScore userScore =
                    new UserScore(null, user.getId(), systemYearService.getSystemYear(), rule.getClassify(), (float) 0);
            userScoreService.save(userScore);
        }
        return Result.success("添加成功");
    }

    @PostMapping("/deleteRule")
    public Result deleteRule(@RequestParam("ruleId") String ruleId) {
        ruleService.removeById(ruleId);
        return Result.success("删除成功");
    }

    @GetMapping("/getClassify")
    public Result getClassify() {
        return Result.success(ruleService.getClassify());
    }

    @PostMapping("/deleteClassifyRule")
    public Result deleteClassifyRule(@RequestParam("classify") String classify) {
        return ruleService.deleteClassifyRule(classify);
    }
}

