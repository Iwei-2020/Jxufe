package com.smile.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.smile.entity.SystemYear;
import com.smile.mapper.SystemYearMapper;
import com.smile.service.SystemYearService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author thePassionate
 * @since 2020-12-29
 */
@Service
public class SystemYearServiceImpl extends ServiceImpl<SystemYearMapper, SystemYear> implements SystemYearService {

    private final SystemYearMapper systemYearMapper;

    @Autowired
    public SystemYearServiceImpl(SystemYearMapper systemYearMapper) {
        this.systemYearMapper = systemYearMapper;
    }

    @Override
    public String getSystemYear() {
        return systemYearMapper.getSystemYear();
    }
}
