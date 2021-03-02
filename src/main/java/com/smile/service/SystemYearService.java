package com.smile.service;

import com.smile.entity.SystemYear;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author thePassionate
 * @since 2020-12-29
 */
public interface SystemYearService extends IService<SystemYear> {
    /**
     * TODO 获取系统学年
     * @author smilePlus
     * @date 2021/1/2 18:56
     * @return java.lang.String
     */
    String getSystemYear();
}
