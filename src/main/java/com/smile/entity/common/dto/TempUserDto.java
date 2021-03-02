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
 * @date 2021/1/2 23:09
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TempUserDto {
    private String id;
    private String no;
    private String name;
    private Integer gender;
    private String username;
    private String password;
    private String phone;
    private List<String> newDepartments;
    private Integer logo;
}
