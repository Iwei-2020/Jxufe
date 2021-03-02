package com.smile.service.impl;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.smile.entity.Appeal;
import com.smile.entity.Record;
import com.smile.entity.Rule;
import com.smile.entity.User;
import com.smile.entity.common.dto.IdDto;
import com.smile.entity.common.other.Result;
import com.smile.mapper.*;
import com.smile.service.AppealService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.smile.utils.MyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
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
public class AppealServiceImpl extends ServiceImpl<AppealMapper, Appeal> implements AppealService {

    private final AppealMapper appealMapper;
    private final RecordMapper recordMapper;
    private final UserMapper userMapper;
    private final RuleMapper ruleMapper;
    private final SystemYearMapper systemYearMapper;

    @Autowired
    public AppealServiceImpl(AppealMapper appealMapper, RecordMapper recordMapper, UserMapper userMapper, RuleMapper ruleMapper, SystemYearMapper systemYearMapper) {
        this.appealMapper = appealMapper;
        this.recordMapper = recordMapper;
        this.userMapper = userMapper;
        this.ruleMapper = ruleMapper;
        this.systemYearMapper = systemYearMapper;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public Result tchAppeal(Appeal appeal) {
        appeal.setYear(systemYearMapper.getSystemYear());
        String recordId = appeal.getRecordId();
        UpdateWrapper<Record> recordUpdateWrapper = new UpdateWrapper<>();
        recordUpdateWrapper.eq("id",recordId).set("status",1);
        recordMapper.update(null,recordUpdateWrapper);
        appealMapper.insert(appeal);
        return Result.success("申诉成功");
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public Result cancelAppeal(String recordId) {
        UpdateWrapper<Record> recordUpdateWrapper = new UpdateWrapper<>();
        recordUpdateWrapper.eq("id",recordId).set("status",0);
        recordMapper.update(null, recordUpdateWrapper);
        QueryWrapper<Appeal> appealQueryWrapper = new QueryWrapper<>();
        appealQueryWrapper.eq("record_id",recordId);
        appealMapper.delete(appealQueryWrapper);
        return Result.success("取消申诉成功");
    }

    @Override
    public Result seeAppeal(String recordId) {
        QueryWrapper<Appeal> appealQueryWrapper = new QueryWrapper<>();
        appealQueryWrapper.eq("record_id",recordId);
        return Result.success(null,appealMapper.selectOne(appealQueryWrapper).getReason());
    }

    @Override
    public Result seeFeedback(String recordId) {
        QueryWrapper<Appeal> appealQueryWrapper = new QueryWrapper<>();
        appealQueryWrapper.eq("record_id",recordId);
        return Result.success(null,appealMapper.selectOne(appealQueryWrapper).getFeedback());
    }

    @Override
    public Result getDepartmentAppeal(String userId) {
        List<IdDto> idDtos;
        HashSet<String> ids = new HashSet<>();
        User user = userMapper.selectById(userId);
        switch (user.getLogo()) {
            case 1:
                List<Appeal> appeals = appealMapper.selectList(null);
                return Result.success(appealFormat(appeals));
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
        ArrayList<String> recordIds = new ArrayList<>();
        for (Record record : records) {
            recordIds.add(record.getId());
        }
        QueryWrapper<Appeal> appealQueryWrapper = new QueryWrapper<>();
        appealQueryWrapper.in("record_id",recordIds);
        List<Appeal> appeals = appealMapper.selectList(appealQueryWrapper);
        return Result.success(appealFormat(appeals));
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public Result handleAppeal(String appealId, String feedback) {
        String today= DateUtil.today();
        Date date = DateUtil.parse(today);
        UpdateWrapper<Record> recordUpdateWrapper = new UpdateWrapper<>();
        UpdateWrapper<Appeal> appealUpdateWrapper = new UpdateWrapper<>();
        recordUpdateWrapper.eq("id",appealMapper.selectById(appealId).getRecordId()).set("status",2);
        appealUpdateWrapper
                .eq("id",appealId)
                .set("feedback",feedback).set("status",1)
                .set("gmt_solve",date);
        recordMapper.update(null,recordUpdateWrapper);
        appealMapper.update(null,appealUpdateWrapper);
        return Result.success("申诉处理成功");
    }

    private JSONArray appealFormat(List<Appeal> appealList) {
        JSONArray jsonArray = new JSONArray();
        if (appealList == null) {
            return null;
        }
        for (Appeal appeal : appealList) {
            JSONObject jsonObject = (JSONObject) JSONObject.toJSON(appeal);
            Record record = recordMapper.selectById(appeal.getRecordId());
            User tch = userMapper.selectById(record.getTchId());
            User executor = userMapper.selectById(record.getExecutorId());
            Rule rule = ruleMapper.selectById(record.getRuleId());
            jsonObject.put("tchName",tch.getName());
            jsonObject.put("executorName",executor.getName());
            jsonObject.put("classify",rule.getClassify());
            jsonObject.put("ruleReason",rule.getReason());
            jsonObject.put("addScore",record.getAddScore());
            jsonObject.put("desScore",record.getDesScore());
            jsonObject.put("remark",record.getRemark());
            jsonObject.put("gmtRecord",record.getGmtRecord());
            jsonObject.put("year",appeal.getYear());
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }
}
