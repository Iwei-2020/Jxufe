package com.smile.entity;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
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
 * @since 2020-11-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Schedule implements Serializable {

    private static final long serialVersionUID=1L;

    @ExcelIgnore
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    @ExcelIgnore
    private String userId;

    @ExcelProperty(value = "节",index = 0)
    private String no;

    @ExcelProperty(value = "星期一",index = 1)
    private String monday;

    @ExcelProperty(value = "星期二",index = 2)
    private String tuesday;

    @ExcelProperty(value = "星期三",index = 3)
    private String wednesday;

    @ExcelProperty(value = "星期四",index = 4)
    private String thursday;

    @ExcelProperty(value = "星期五",index = 5)
    private String friday;

}
