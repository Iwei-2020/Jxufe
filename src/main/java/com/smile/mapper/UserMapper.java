package com.smile.mapper;

import com.smile.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.smile.entity.common.dto.GroupDepartmentDto;
import com.smile.entity.common.dto.IdDto;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author thePassionate
 * @since 2020-11-11
 */
@Repository
public interface UserMapper extends BaseMapper<User> {

    /**
     * TODO 获取全部部门
     * @param userId 部门id
     * @param p2 logo为3时需要的参数
     * @author smilePlus
     * @date 2021/1/2 11:28
     * @return java.util.List<com.smile.entity.common.dto.IdDto>
     */
    List<IdDto> getDepartments(String userId,String p2);
}
