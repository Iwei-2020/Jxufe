package com.smile.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author thePassionate
 * @since 2020-12-29
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Affiliate2 implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "user_id", type = IdType.ASSIGN_ID)
    private String userId;

    private String userIdAffiliate;

}
