package com.smile.service;

import com.smile.entity.Rule;
import com.smile.entity.Schedule;
import com.baomidou.mybatisplus.extension.service.IService;
import com.smile.entity.common.other.Result;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author thePassionate
 * @since 2020-11-29
 */
public interface ScheduleService extends IService<Schedule> {

    /**
     * TODO
     * @param userId 获取用户课程表
     * @author smilePlus
     * @date 2021/1/4 11:46
     * @return java.util.List<com.smile.entity.Schedule>
     */
    List<Schedule> getUserSchedule(String userId);


}
