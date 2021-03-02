package com.smile.service;

import com.smile.entity.Rule;
import com.baomidou.mybatisplus.extension.service.IService;
import com.smile.entity.common.other.Result;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author thePassionate
 * @since 2020-11-11
 */
public interface RuleService extends IService<Rule> {

    /**
     * TODO
     * @param userId 用户id
     * @author smilePlus
     * @date 2021/1/3 6:33
     * @return java.util.List<com.smile.entity.Rule>
     */
    List<Rule> getRule(String userId);

    /**
     * TODO 获取classify
     * @author smilePlus
     * @date 2021/1/3 16:34
     * @return java.util.List<java.lang.String>
     */
    List<String> getClassify();

    /**
     * TODO
     * @param classify 删除的计分分类系列名
     * @author smilePlus
     * @date 2021/1/4 10:45
     * @return com.smile.entity.common.other.Result
     */
    Result deleteClassifyRule(String classify);
}
