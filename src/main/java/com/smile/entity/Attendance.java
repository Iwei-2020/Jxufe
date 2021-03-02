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
 * @author thePassionate
 * @since 2020-11-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Attendance implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @ExcelIgnore
    private String id;

    @ExcelIgnore
    private String userId;

    @ExcelProperty("姓名")
    private String name;

    @ExcelProperty("日期")
    private String attDate;

    @ExcelProperty("上班1打卡时间")
    private String timeWork1;

    @ExcelProperty("上班1打卡结果")
    private String workResult1;

    @ExcelProperty("下班1打卡时间")
    private String timeOff1;

    @ExcelProperty("下班1打卡结果")
    private String offResult1;

    @ExcelProperty("上班2打卡时间")
    private String timeWork2;

    @ExcelProperty("上班2打卡结果")
    private String workResult2;

    @ExcelProperty("下班2打卡时间")
    private String timeOff2;

    @ExcelProperty("下班2打卡结果")
    private String offResult2;

}
