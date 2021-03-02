package com.smile.entity.common.dto;

import lombok.Data;

/**
 * TODO 新增部门的数据传输类
 * @author smilePlus
 * @version 1.0
 * @date 2021/1/2 10:22
 */
@Data
public class NewDepartment {
    private String name;
    private String supSupDepartment;
    private String supDepartment;
    private String username;
    private String password;
    private String phone;
    private Integer logo;
}
