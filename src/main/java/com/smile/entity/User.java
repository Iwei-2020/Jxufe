package com.smile.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author thePassionate
 * @since 2020-11-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class User implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    @ExcelProperty("教工编号")
    private String no;

    @ExcelProperty("姓名")
    private String name;

    @ExcelProperty("性别")
    private Integer gender;

    @ExcelProperty("用户名")
    private String username;

    @ExcelProperty("密码")
    private String password;

    @ExcelProperty("电话")
    private String phone;

    @ExcelProperty("专项平均积分")
    private float scoreSpecialAverage;

    @ExcelProperty("年度积分")
    private float scoreAnnual;

    @ExcelProperty("平均年度积分")
    private float scoreAnnualAverage;

    private Integer logo;

    private Integer tagTch;

    @TableLogic
    private Integer deleted;

}
