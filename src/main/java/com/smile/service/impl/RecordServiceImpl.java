package com.smile.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.smile.entity.Record;
import com.smile.entity.Rule;
import com.smile.entity.User;
import com.smile.entity.common.dto.IdDto;
import com.smile.entity.common.other.Result;
import com.smile.mapper.*;
import com.smile.service.RecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.smile.utils.MyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author thePassionate
 * @since 2020-11-11
 */
@Service
public class RecordServiceImpl extends ServiceImpl<RecordMapper, Record> implements RecordService {

    private final RecordMapper recordMapper;
    private final UserMapper userMapper;
    private final RuleMapper ruleMapper;

    @Autowired
    public RecordServiceImpl(RecordMapper recordMapper, UserMapper userMapper, RuleMapper ruleMapper) {
        this.recordMapper = recordMapper;
        this.userMapper = userMapper;
        this.ruleMapper = ruleMapper;
    }

    @Override
    public Result getDepartmentRecord(String userId) {
        HashSet<String> ids = new HashSet<>();
        List<IdDto> idDtos;
        User user = userMapper.selectById(userId);
        switch (user.getLogo()) {
            case 1:
                List<Record> records = recordMapper.selectList(null);
                return Result.success(recordFormat(records));
            case 2:
                idDtos = userMapper.getDepartments(user.getId(), null);
                ids = MyUtils.idDtoUtils(idDtos,true);
                break;
            case 3:
                idDtos = userMapper.getDepartments(null, user.getId());
                ids = MyUtils.idDtoUtils(idDtos,false);
                break;
            case 4:
                ids.add(user.getId());
                break;
            default:
                break;
        }
        QueryWrapper<Record> recordQueryWrapper = new QueryWrapper<>();
        recordQueryWrapper.in("executor_id",ids);
        List<Record> records = recordMapper.selectList(recordQueryWrapper);
        return Result.success(recordFormat(records));
    }

    @Override
    public Result getTchRecord(String id) {
        QueryWrapper<Record> recordQueryWrapper = new QueryWrapper<>();
        recordQueryWrapper.eq("tch_id",id);
        List<Record> records = recordMapper.selectList(recordQueryWrapper);
        return Result.success(recordFormat(records));
    }


    private JSONArray recordFormat(List<Record> records) {
        JSONArray jsonArray = new JSONArray();
        for (Record record : records) {
            JSONObject jsonObject = (JSONObject) JSONObject.toJSON(record);
            User tch = userMapper.selectById(record.getTchId());
            jsonObject.put("tchNo",tch.getNo());
            jsonObject.put("tchName",tch.getName());
            User executor = userMapper.selectById(record.getExecutorId());
            jsonObject.put("executorName",executor.getName());
            Rule rule = ruleMapper.selectById(record.getRuleId());
            jsonObject.put("classify",rule.getClassify());
            jsonObject.put("reason",rule.getReason());
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }
}
