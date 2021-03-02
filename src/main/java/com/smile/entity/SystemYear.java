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
 * @since 2020-12-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SystemYear implements Serializable {
    private static final long serialVersionUID=1L;
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 系统当前学年
     */
    private String year;


}
