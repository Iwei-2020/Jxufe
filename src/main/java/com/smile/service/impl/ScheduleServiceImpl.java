package com.smile.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.smile.entity.Rule;
import com.smile.entity.Schedule;
import com.smile.entity.common.other.Result;
import com.smile.mapper.ScheduleMapper;
import com.smile.service.ScheduleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author thePassionate
 * @since 2020-11-29
 */
@Service
public class ScheduleServiceImpl extends ServiceImpl<ScheduleMapper, Schedule> implements ScheduleService {
    private final ScheduleMapper scheduleMapper;

    public ScheduleServiceImpl(ScheduleMapper scheduleMapper) {
        this.scheduleMapper = scheduleMapper;
    }

    @Override
    public List<Schedule> getUserSchedule(String userId) {
        QueryWrapper<Schedule> scheduleQueryWrapper = new QueryWrapper<>();
        scheduleQueryWrapper.eq("user_id",userId);
        return scheduleMapper.selectList(scheduleQueryWrapper);
    }
}
