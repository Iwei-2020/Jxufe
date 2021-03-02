package com.smile.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.util.Date;
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
public class Record implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    private String tchId;

    private String executorId;

    private String ruleId;

    private String attendanceId;

    private Float addScore;

    private Float desScore;

    private String remark;

    private Integer status;

    @TableField(fill = FieldFill.INSERT)
    private Date gmtRecord;

    private String year;

    private Integer type;

    @TableLogic
    private Integer deleted;


}
