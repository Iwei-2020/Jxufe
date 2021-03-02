package com.smile.mapper;

import com.smile.entity.SystemYear;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author thePassionate
 * @since 2020-12-29
 */
@Repository
public interface SystemYearMapper extends BaseMapper<SystemYear> {
    String getSystemYear();
}
