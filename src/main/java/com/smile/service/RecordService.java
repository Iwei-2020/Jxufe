package com.smile.service;

import com.smile.entity.Record;
import com.baomidou.mybatisplus.extension.service.IService;
import com.smile.entity.common.other.Result;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author thePassionate
 * @since 2020-11-11
 */
public interface RecordService extends IService<Record> {
    /**
     * TODO 获取部门对应的用户记录
     * @param userId 用户Id
     * @author smilePlus
     * @date 2021/1/4 8:32
     * @return com.smile.entity.common.other.Result
     */
    Result getDepartmentRecord(String userId);
    Result getTchRecord(String id);
}
