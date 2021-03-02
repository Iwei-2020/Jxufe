package com.smile.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.smile.entity.SystemYear;
import com.smile.entity.User;
import com.smile.entity.UserScore;
import com.smile.entity.common.other.Result;
import com.smile.service.RuleService;
import com.smile.service.SystemYearService;
import com.smile.service.UserScoreService;
import com.smile.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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
 * @since 2020-12-29
 */
@RestController
@RequestMapping("/system-year")
public class SystemYearController {
    private final SystemYearService systemYearService;
    private final UserScoreService userScoreService;
    private final RuleService ruleService;
    private final UserService userService;

    @Autowired
    public SystemYearController(SystemYearService systemYearService, UserScoreService userScoreService, RuleService ruleService, UserService userService) {
        this.systemYearService = systemYearService;
        this.userScoreService = userScoreService;
        this.ruleService = ruleService;
        this.userService = userService;
    }
    @PostMapping("/updateSystemYear")
    @Transactional(rollbackFor = RuntimeException.class)
    public Result updateSystemYear(@RequestParam("nextSystemYear") String nextSystemYear) {
        UpdateWrapper<SystemYear> systemYearUpdateWrapper = new UpdateWrapper<>();
        systemYearUpdateWrapper
                .eq("id", "1343829079194914817")
                .set("year",nextSystemYear);
        systemYearService.update(systemYearUpdateWrapper);
        // 网UserScore插入记录
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("logo",-1);
        List<User> userList = userService.list(userQueryWrapper);
        List<String> classify = ruleService.getClassify();
        List<UserScore> userScores = new ArrayList<>();
        for (User user : userList) {
            for (String c : classify) {
                UserScore userScore = new UserScore();
                userScore.setUserId(user.getId());
                userScore.setClassify(c);
                userScore.setScore((float) 0);
                userScore.setYear(nextSystemYear);
                userScores.add(userScore);
            }
        }
        userScoreService.saveBatch(userScores);
        return Result.success("更新学年成功");
    }
}

