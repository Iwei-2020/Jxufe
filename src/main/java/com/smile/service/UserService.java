package com.smile.service;

import com.smile.entity.Record;
import com.smile.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.smile.entity.common.dto.LoginDto;
import com.smile.entity.common.dto.NewDepartment;
import com.smile.entity.common.dto.TempUserDto;
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
public interface UserService extends IService<User> {
    /**
     * TODO
     * @param loginDto 登录数据传输对象
     * @author smilePlus
     * @date 2020/12/29 15:34
     * @return com.smile.entity.common.other.Result
     */
    Result login(LoginDto loginDto);
    /**
     * TODO
     * @param newUser 新增用户的基本信息
     * @param departments 新增用户的所属部门
     * @author smilePlus
     * @date 2021/1/5 21:21
     * @return com.smile.entity.common.other.Result
     */
    Result addUser(User newUser,List<String> departments);
    /**
     * TODO 获取部门全部用户
     * @param userId 用户id
     * @author smilePlus
     * @date 2021/1/2 11:06
     * @return com.smile.entity.common.other.Result
     */
    Result getAllUser(String userId);
    /**
     * TODO
     * @param record 记录
     * @author smilePlus
     * @date 2021/1/3 16:26
     * @return com.smile.entity.common.other.Result
     */
    Result modifyScore(Record record);
    /**
     * TODO
     * @param tchList 修改教工的列表
     * @param record 记录
     * @author smilePlus
     * @date 2021/1/3 19:26
     * @return com.smile.entity.common.other.Result
     */
    Result modifyScoreBatch(List<User> tchList, Record record);
    Result modifyInfo(String id,String changePro,String proValue);
    Result getRoleList(String id);
    Result getSpecialAverage();
    Result getGraphData(List<Integer >paramArray, String classify);
    /**
     * TODO
     * @param userId 用户id
     * @author smilePlus
     * @date 2020/12/22 19:24
     */
    void deleteUser(String userId);
    /**
     * TODO
     * @param userId 要添加角色的用户id
     * @param role 要添加角色
     * @param department 要添加角色的部门
     * @author smilePlus
     * @date 2020/12/22 20:33
     * @return int
     */
    Result addUserRole(String userId, String role, String department);
    /**
     * TODO
     *
     * @param userId 用户id
     * @param oriDepartment 原部门
     * @param nowDepartment 现部门
     * @author smilePlus
     * @date 2020/12/23 23:43
     * @return com.smile.entity.common.other.Result
     */
    Result changeDepartment(String userId, String oriDepartment, String nowDepartment);

    /**
     * TODO 获取部门，实际上是获取用户
     * @author smilePlus
     * @date 2021/1/1 23:17
     * @return com.smile.entity.common.other.Result
     */
    Result getDepartments();

    /**
     * TODO 新增部门，实际是对user表的修改
     * @param newDepartment 新部门
     * @author smilePlus
     * @date 2021/1/2 10:27
     * @return com.smile.entity.common.other.Result
     */
    Result addDepartment(NewDepartment newDepartment);

    /**
     * TODO 修改用户的基本信息
     * @param tempUser tempUser
     * @author smilePlus
     * @date 2021/1/2 23:21
     * @return com.smile.entity.common.other.Result
     */
    Result userModify(TempUserDto tempUser);
}
