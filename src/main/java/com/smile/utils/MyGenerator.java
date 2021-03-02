package com.smile.utils;


import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.po.TableFill;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

import java.util.ArrayList;

public class MyGenerator {
    public static void main(String[] args) {
        // 构建一个代码自动生成器对象
        AutoGenerator autoGenerator = new AutoGenerator();
        // 1、全局配置
        GlobalConfig gc = new GlobalConfig();
        // 获取项目的路径
        String projectPath = System.getProperty("user.dir");
        // 设置代码生成的路径
        gc.setOutputDir(projectPath + "/src/main/java");
        // 设置作者
        gc.setAuthor("thePassionate");
        // 是否打开文件资源管理器
        gc.setOpen(false);
        // 是否覆盖原来生成的
        gc.setFileOverride(false);
        // 去除service的I前缀
        gc.setServiceName("%sService");
        // 主键生成策略
        gc.setIdType(IdType.ASSIGN_ID);
        // 设置日期的类型
        gc.setDateType(DateType.ONLY_DATE);
        // 是否生成Swagger
        gc.setSwagger2(false);

        // 2、配置数据源
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl("jdbc:mysql://localhost:3306/cdfz?useSSL=false&useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true");
        dsc.setDriverName("com.mysql.cj.jdbc.Driver");
        dsc.setUsername("root");
        dsc.setPassword("thePassionate");
        dsc.setDbType(DbType.MYSQL);

        // 3、包的配置
        PackageConfig pc = new PackageConfig();
        // 设置模块名
        pc.setParent("com.smile");
        pc.setEntity("entity");
        pc.setMapper("mapper");
        pc.setService("service");
        pc.setController("controller");

        // 4、策略的配置
        StrategyConfig strategyConfig = new StrategyConfig();
        // 设置要映射的表
        strategyConfig.setInclude("department_rule");
        // 设置行列下划线转驼峰
        strategyConfig.setNaming(NamingStrategy.underline_to_camel);
        strategyConfig.setColumnNaming(NamingStrategy.underline_to_camel);
        // 自动生成lombok
        strategyConfig.setEntityLombokModel(true);
        // 设置逻辑删除字段
        strategyConfig.setLogicDeleteFieldName("deleted");
        // 自动填充字段的设置
        TableFill gmtCreate = new TableFill("gmt_create", FieldFill.INSERT);
//        TableFill gmtSolve = new TableFill("gmt_solve", FieldFill.INSERT);
        ArrayList<TableFill> tableFills = new ArrayList<>();
        tableFills.add(gmtCreate);
//        tableFills.add(gmtSolve);
        strategyConfig.setTableFillList(tableFills);
        // 乐观锁
        strategyConfig.setVersionFieldName("version");
        // 将controller设置为rest风格
        strategyConfig.setRestControllerStyle(true);
        // localhost:8080/hello_id_2
        strategyConfig.setControllerMappingHyphenStyle(true);

        // 5、注入配置
        // 将全局配置注入autoGenerator
        autoGenerator.setGlobalConfig(gc);
        // 将数据源注入autoGenerator
        autoGenerator.setDataSource(dsc);
        // 将包配置注入autoGenerator
        autoGenerator.setPackageInfo(pc);
        // 将策略注入autoGenerator
        autoGenerator.setStrategy(strategyConfig);
        // 执行代码生成器
        autoGenerator.execute();
    }
}
