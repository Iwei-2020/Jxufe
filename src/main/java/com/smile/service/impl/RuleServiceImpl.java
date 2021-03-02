package com.smile.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.smile.entity.*;
import com.smile.entity.common.other.Result;
import com.smile.mapper.*;
import com.smile.service.RuleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author thePassionate
 * @since 2020-11-11
 */
@Service
public class RuleServiceImpl extends ServiceImpl<RuleMapper, Rule> implements RuleService {

    private final RuleMapper ruleMapper;
    private final UserMapper userMapper;
    private final UserScoreMapper userScoreMapper;
    private final RecordMapper recordMapper;
    private final AppealMapper appealMapper;
    private final DepartmentRuleMapper departmentRuleMapper;

    @Autowired
    public RuleServiceImpl(RuleMapper ruleMapper, UserMapper userMapper, UserScoreMapper userScoreMapper, RecordMapper recordMapper, AppealMapper appealMapper, DepartmentRuleMapper departmentRuleMapper) {
        this.ruleMapper = ruleMapper;
        this.userMapper = userMapper;
        this.userScoreMapper = userScoreMapper;
        this.recordMapper = recordMapper;
        this.appealMapper = appealMapper;
        this.departmentRuleMapper = departmentRuleMapper;
    }

    @Override
    public List<Rule> getRule(String userId) {
        List<Rule> list = null;
        ArrayList<String> ruleIds = new ArrayList<>();
        User user = userMapper.selectById(userId);
        if (user.getLogo() == 1) {
            list = ruleMapper.selectList(null);
        } else {
            QueryWrapper<DepartmentRule> departmentUserQueryWrapper = new QueryWrapper<>();
            departmentUserQueryWrapper.eq("department_id",userId);
            List<DepartmentRule> departmentRules = departmentRuleMapper.selectList(departmentUserQueryWrapper);
            if (departmentRules.size() == 0) {
                return null;
            }
            for (DepartmentRule departmentRule : departmentRules) {
                ruleIds.add(departmentRule.getRuleId());
            }
            QueryWrapper<Rule> ruleQueryWrapper = new QueryWrapper<>();
            ruleQueryWrapper.in("id",ruleIds);
            list = ruleMapper.selectList(ruleQueryWrapper);
        }
        return list;
    }

    @Override
    public List<String> getClassify() {
        QueryWrapper<Rule> ruleQueryWrapper =
                new QueryWrapper<Rule>().select("classify").groupBy("classify");
        List<Rule> list = ruleMapper.selectList(ruleQueryWrapper);
        ArrayList<String> result = new ArrayList<>();
        for (Rule rule : list) {
            result.add(rule.getClassify());
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public Result deleteClassifyRule(String classify) {
        ArrayList<String> ruleIds = new ArrayList<>();
        ArrayList<String> recordIds = new ArrayList<>();

        QueryWrapper<Rule> ruleQueryWrapper = new QueryWrapper<>();
        ruleQueryWrapper.eq("classify",classify);
        List<Rule> ruleList = ruleMapper.selectList(ruleQueryWrapper);
        for (Rule rule : ruleList) {
            ruleIds.add(rule.getId());
        }

        if (ruleIds.size() > 0) {
            QueryWrapper<Record> recordQueryWrapper = new QueryWrapper<>();
            recordQueryWrapper.in("rule_id",ruleIds);
            List<Record> records = recordMapper.selectList(recordQueryWrapper);
            for (Record record : records) {
                recordIds.add(record.getId());
            }
            // 更新appeal要在更新record之前
            if (recordIds.size() > 0) {
                UpdateWrapper<Appeal> appealUpdateWrapper = new UpdateWrapper<>();
                appealUpdateWrapper.in("record_id",recordIds);
                appealMapper.delete(appealUpdateWrapper);
            }
            // 更新record
            UpdateWrapper<Record> recordUpdateWrapper = new UpdateWrapper<>();
            recordUpdateWrapper.in("rule_id",ruleIds);
            recordMapper.delete(recordUpdateWrapper);
            // 更新department_rule
            UpdateWrapper<DepartmentRule> departmentRuleUpdateWrapper = new UpdateWrapper<>();
            departmentRuleUpdateWrapper.in("rule_id",ruleIds);
            departmentRuleMapper.delete(departmentRuleUpdateWrapper);
        }

        // 更新user_score
        UpdateWrapper<UserScore> userScoreUpdateWrapper = new UpdateWrapper<>();
        userScoreUpdateWrapper.eq("classify",classify);
        userScoreMapper.delete(userScoreUpdateWrapper);

        // 更新rule
        ruleMapper.delete(ruleQueryWrapper);

        return Result.success("删除系列规则成功");
    }
}
