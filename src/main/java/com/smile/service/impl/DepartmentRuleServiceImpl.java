package com.smile.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.smile.entity.DepartmentRule;
import com.smile.entity.Rule;
import com.smile.entity.User;
import com.smile.entity.common.dto.DepartmentRuleDto;
import com.smile.entity.common.dto.IdDto;
import com.smile.entity.common.other.Result;
import com.smile.mapper.DepartmentRuleMapper;
import com.smile.mapper.RuleMapper;
import com.smile.mapper.UserMapper;
import com.smile.service.DepartmentRuleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.smile.utils.MyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author thePassionate
 * @since 2021-01-03
 */
@Service
public class DepartmentRuleServiceImpl extends ServiceImpl<DepartmentRuleMapper, DepartmentRule> implements DepartmentRuleService {

    private final DepartmentRuleMapper departmentRuleMapper;
    private final UserMapper userMapper;
    private final RuleMapper ruleMapper;

    @Autowired
    public DepartmentRuleServiceImpl(DepartmentRuleMapper departmentRuleMapper, UserMapper userMapper, RuleMapper ruleMapper) {
        this.departmentRuleMapper = departmentRuleMapper;
        this.userMapper = userMapper;
        this.ruleMapper = ruleMapper;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public Result departmentAddRule(DepartmentRuleDto departmentRuleDto) {
        String department = departmentRuleDto.getDepartment();

        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("name",department);
        User user = userMapper.selectOne(userQueryWrapper);

        // 1. 先删除departmentRule对应的数据
        UpdateWrapper<DepartmentRule> departmentRuleUpdateWrapper = new UpdateWrapper<>();
        departmentRuleUpdateWrapper.eq("department_id",user.getId());
        departmentRuleMapper.delete(departmentRuleUpdateWrapper);

        // 2. 再插入
        List<String> ruleCheckList = departmentRuleDto.getRuleCheckList();
        for (String reason : ruleCheckList) {
            QueryWrapper<Rule> ruleQueryWrapper = new QueryWrapper<>();
            ruleQueryWrapper.eq("reason",reason);
            Rule rule = ruleMapper.selectOne(ruleQueryWrapper);
            departmentRuleMapper.insert(new DepartmentRule(user.getId(),rule.getId()));
        }
        return Result.success("分配权限成功");
    }

    @Override
    public Result getCheckRuleList(String department) {
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("name",department);
        User user = userMapper.selectOne(userQueryWrapper);

        List<IdDto> idDtos;
        HashSet<String> ids = new HashSet<>();
        HashSet<String> checkList = new HashSet<>();

        switch (user.getLogo()) {
            case 2:
                idDtos = userMapper.getDepartments(user.getId(), null);
                ids = MyUtils.idDtoUtils(idDtos,true);
                break;
            case 3:
                idDtos = userMapper.getDepartments(null, user.getId());
                ids = MyUtils.idDtoUtils(idDtos,false);
                break;
            case 4:
                ids.add(user.getId());
                break;
            default:
                break;
        }

        for (String id : ids) {
            QueryWrapper<DepartmentRule> departmentRuleQueryWrapper = new QueryWrapper<>();
            departmentRuleQueryWrapper.eq("department_id",id);
            List<DepartmentRule> departmentRules = departmentRuleMapper.selectList(departmentRuleQueryWrapper);
            if (departmentRules == null) {
                return Result.success(null);
            }
            for (DepartmentRule departmentRule : departmentRules) {
                Rule rule = ruleMapper.selectById(departmentRule.getRuleId());
                checkList.add(rule.getReason());
            }
        }
        return Result.success(checkList);
    }
}
