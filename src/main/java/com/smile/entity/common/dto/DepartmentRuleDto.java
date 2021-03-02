package com.smile.entity.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * TODO
 *
 * @author smilePlus
 * @version 1.0
 * @date 2021/1/3 14:00
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentRuleDto {
    private String department;
    private List<String> ruleCheckList;
}
