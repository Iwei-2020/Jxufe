package com.smile.entity.common.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.smile.utils.converter.CustomStringIntegerConverter;
import lombok.Data;

/**
 * @author smileplus
 */
@Data
public class UserDto {

    @ExcelProperty("教工编号")
    private String no;

    @ExcelProperty("姓名")
    private String name;

    @ExcelProperty("用户名")
    private String username;

    @ExcelProperty("密码")
    private String password;

    @ExcelProperty("部门")
    private String department;

    @ExcelProperty(value = "性别", converter = CustomStringIntegerConverter.class)
    private Integer gender;

    @ExcelProperty("电话")
    private String phone;
}
