package com.smile.config;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author smileplus
 * @since 2020-12-29
 * Component 一定要把MyMetaHandlerObject放入spring容器中
 */
@Slf4j
@Component
public class MyMetaHandlerObject implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("insert start fill......");
        String today= DateUtil.today();
        Date date = DateUtil.parse(today);
        this.setFieldValByName("gmtRecord", date, metaObject);
        this.setFieldValByName("gmtCreate", date, metaObject);

    }

    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("update start fill......");
    }
}