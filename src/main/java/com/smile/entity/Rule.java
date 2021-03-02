package com.smile.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.TableId;
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
public class Rule implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    private String classify;

    private String reason;

    private String addLimit;

    private String desLimit;

    private String illustrate;

}
