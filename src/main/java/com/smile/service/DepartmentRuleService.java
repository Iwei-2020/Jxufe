package com.smile.service;

import com.smile.entity.DepartmentRule;
import com.baomidou.mybatisplus.extension.service.IService;
import com.smile.entity.common.dto.DepartmentRuleDto;
import com.smile.entity.common.other.Result;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author thePassionate
 * @since 2021-01-03
 */
public interface DepartmentRuleService extends IService<DepartmentRule> {

    /**
     * TODO
     * @param departmentRuleDto 实体DepartmentRule的数据传输对象
     * @author smilePlus
     * @date 2021/1/3 14:04
     * @return com.smile.entity.common.other.Result
     */
    Result departmentAddRule(DepartmentRuleDto departmentRuleDto);

    /**
     * TODO
     * @param department 实质是user.name
     * @author smilePlus
     * @date 2021/1/3 14:32
     * @return com.smile.entity.common.other.Result
     */
    Result getCheckRuleList(String department);
}
