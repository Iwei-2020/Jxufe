package com.smile.controller;


import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.smile.entity.Record;
import com.smile.entity.Schedule;
import com.smile.entity.User;
import com.smile.entity.common.dto.*;
import com.smile.entity.common.other.Result;
import com.smile.service.ScheduleService;
import com.smile.service.UserService;
import com.smile.utils.JSONUtils;
import com.smile.utils.listen.ScheduleListen;
import com.smile.utils.listen.UserListen;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author thePassionate
 * @since 2020-11-11
 */
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final ScheduleService scheduleService;

    @Autowired
    public UserController(UserService userService, ScheduleService scheduleService) {
        this.userService = userService;
        this.scheduleService = scheduleService;
    }

    @PostMapping("/login")
    public Result login(@RequestBody HashMap<String, Object> map) {
        LoginDto loginDto = JSONUtils.objToJavaBean(map.get("loginDto"), LoginDto.class);
        return userService.login(loginDto);
    }

    @PostMapping("/addUser")
    public Result addUser(@RequestBody NewUserDto newUser) {
        User user = new User();
        BeanUtils.copyProperties(newUser,user);
        return userService.addUser(user,newUser.getDepartments());
    }

    @PostMapping("/addUserBatch")
    public Result addUserBatch(@RequestParam("file") MultipartFile file) {
        try {
            EasyExcel.read(file.getInputStream(), UserDto.class, new UserListen(userService)).sheet().doRead();
            return Result.success("用户数据导入成功");
        } catch (IOException e) {
            e.printStackTrace();
            return Result.error("用户数据导入失败");
        }
    }

    @PostMapping("/addUserRole")
    public Result addUserRole(@RequestParam("id") String userId,
                              @RequestParam("role") String role,
                              @RequestParam("department") String department) {
        return userService.addUserRole(userId, role, department);
    }
    @PostMapping("/changeDepartment")
    public Result changeDepartment(@RequestParam("id") String userId,
                              @RequestParam("oriDepartment") String oriDepartment,
                              @RequestParam("nowDepartment") String nowDepartment) {
        return userService.changeDepartment(userId,oriDepartment,nowDepartment);
    }

    @PostMapping("/getAllUser")
    public Result getAllUser(@RequestParam("userId") String userId) {
        return userService.getAllUser(userId);
    }

    @PostMapping("/deleteUser")
    public Result deleteUser(@RequestParam("id") String userId) {
        userService.deleteUser(userId);
        return Result.success("删除用户成功");
    }

    @PostMapping("/modifyScore")
    public Result modifyScore(@RequestBody Record record) {
        return userService.modifyScore(record);
    }

    @PostMapping("/modifyScoreBatch")
    public Result modifyScoreBatch(@RequestBody HashMap<String,Object> map) {
        Record record = JSONUtils.objToJavaBean(map.get("record"), Record.class);
        List<User> tchList = JSONUtils.objToList(map.get("tchList"), User.class);
        return userService.modifyScoreBatch(tchList, record);
    }

    @PostMapping("/modifyInfo")
    public Result modifyInfo(@RequestParam("changePro") String changePro,
                             @RequestParam("proValue") String proValue,
                             @RequestParam("id") String id) {
        return userService.modifyInfo(id,changePro,proValue);
    }

    @PostMapping("/getRoleList")
    public Result getRoleList(@RequestParam("id") String id) {
        return userService.getRoleList(id);
    }

    @PostMapping("/getSpecialAverage")
    public Result getSpecialAverage() {
        return userService.getSpecialAverage();
    }

    @PostMapping("/getGraphData")
    public Result getGraphData(@RequestBody HashMap<String,Object> map) {
        List<Integer> paramArray = JSONUtils.objToList(map.get("paramArray"), Integer.class);
        String classify = (String) map.get("classify");
        return userService.getGraphData(paramArray,classify);
    }

    @GetMapping("/getDepartments")
    public Result getDepartments() {
        return userService.getDepartments();
    }

    @PostMapping("/addDepartment")
    public Result addDepartment(@RequestBody NewDepartment newDepartment) {
        return userService.addDepartment(newDepartment);
    }

    @GetMapping("/getDepartmentOption")
    public Result getDepartmentOption() {
        JSONArray jsonArray = new JSONArray();
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<User>().ge("logo",2);
        for (User user : userService.list(userQueryWrapper)) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("label",user.getName());
            jsonObject.put("value",user.getName());
            jsonArray.add(jsonObject);
        }
        return Result.success(jsonArray);
    }

    @PostMapping("/userModify")
    public Result userModify(@RequestBody TempUserDto tempUser) {
        return userService.userModify(tempUser);
    }

    @PostMapping("/importSchedule")
    @Transactional(rollbackFor = RuntimeException.class)
    public Result importSchedule(@RequestParam("file") MultipartFile file) {
        try {
            EasyExcel.read(file.getInputStream(), Schedule.class,
                    new ScheduleListen(scheduleService,userService)).doReadAll();;
            return Result.success("课程表导入成功");
        } catch (IOException e) {
            e.printStackTrace();
            return Result.error("课程表导入失败");
        }
    }

    @GetMapping("/getAdminTch")
    public Result getAdminTch() {
        ArrayList<String> nameList = new ArrayList<>();
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("logo", -1).eq("tag_tch", 1).select("name");
        List<User> tagList = userService.list(userQueryWrapper);
        for (User user : tagList) {
            nameList.add(user.getName());
        }
        return Result.success(nameList);
    }

    @PostMapping("/admitTagTch")
    @Transactional(rollbackFor = RuntimeException.class)
    public Result admitTagTch(@RequestBody List<String> nameList) {
        UpdateWrapper<User> userUpdateWrapper = new UpdateWrapper<>();
        userUpdateWrapper.in("name",nameList).set("tag_tch", 1);
        userService.update(userUpdateWrapper);
        return Result.success("标记行政人员成功");
    }

}

