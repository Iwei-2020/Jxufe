package com.smile.entity.common.dto;

import com.smile.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * TODO
 * @author smilePlus
 * @version 1.0
 * @date 2020/12/23 14:56
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupDepartmentDto {
    private User user;
    private String role;
    private String departments;
}
