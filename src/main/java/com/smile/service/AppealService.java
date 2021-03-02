package com.smile.service;

import com.smile.entity.Appeal;
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
public interface AppealService extends IService<Appeal> {
    Result tchAppeal(Appeal appeal);
    Result cancelAppeal(String recordId);
    Result seeAppeal(String recordId);
    Result seeFeedback(String recordId);
    /**
     * TODO 获取部门对应的申诉
     * @param userId 部门id
     * @author smilePlus
     * @date 2021/1/4 9:11
     * @return com.smile.entity.common.other.Result
     */
    Result getDepartmentAppeal(String userId);
    Result handleAppeal(String appealId,String feedback);
}
