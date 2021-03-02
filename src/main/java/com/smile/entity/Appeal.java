package com.smile.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author thePassionate
 * @since 2020-11-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Appeal implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    private String recordId;

    private String reason;

    private String feedback;

    private Integer status;

    @TableField(fill = FieldFill.INSERT)
    private Date gmtCreate;

    private Date gmtSolve;

    private String year;
}
