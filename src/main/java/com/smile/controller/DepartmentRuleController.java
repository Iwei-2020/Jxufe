package com.smile.controller;


import com.smile.entity.common.dto.DepartmentRuleDto;
import com.smile.entity.common.other.Result;
import com.smile.service.DepartmentRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author thePassionate
 * @since 2021-01-03
 */
@RestController
@RequestMapping("/department-rule")
public class DepartmentRuleController {

    private final DepartmentRuleService departmentRuleService;

    @Autowired
    public DepartmentRuleController(DepartmentRuleService departmentRuleService) {
        this.departmentRuleService = departmentRuleService;
    }

    @PostMapping("/departmentAddRule")
    public Result departmentAddRule(@RequestBody DepartmentRuleDto departmentRuleDto) {
        return departmentRuleService.departmentAddRule(departmentRuleDto);
    }

    @PostMapping("/getCheckRuleList")
    public Result getCheckRuleList(@RequestParam("department") String department) {
        return departmentRuleService.getCheckRuleList(department);
    }
}

