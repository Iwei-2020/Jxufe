package com.smile.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.NumberUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.smile.entity.*;
import com.smile.entity.common.dto.*;
import com.smile.entity.common.other.Result;
import com.smile.mapper.*;
import com.smile.service.RuleService;
import com.smile.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.smile.utils.MyUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author thePassionate
 * @since 2020-11-11
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private final UserMapper userMapper;
    private final DepartmentUserMapper departmentUserMapper;
    private final RuleMapper ruleMapper;
    private final RecordMapper recordMapper;
    private final ScheduleMapper scheduleMapper;
    private final AttendanceMapper attendanceMapper;
    private final AppealMapper appealMapper;
    private final SystemYearMapper systemYearMapper;
    private final UserScoreMapper userScoreMapper;
    private final Affiliate1Mapper affiliate1Mapper;
    private final Affiliate2Mapper affiliate2Mapper;
    private final RuleService ruleService;

    @Autowired
    public UserServiceImpl(UserMapper userMapper, DepartmentUserMapper departmentUserMapper, RuleMapper ruleMapper
            , RecordMapper recordMapper, ScheduleMapper scheduleMapper, AttendanceMapper attendanceMapper, AppealMapper appealMapper
            , SystemYearMapper systemYearMapper, UserScoreMapper userScoreMapper
            , Affiliate1Mapper affiliate1Mapper, Affiliate2Mapper affiliate2Mapper, RuleService ruleService) {
        this.userMapper = userMapper;
        this.departmentUserMapper = departmentUserMapper;
        this.ruleMapper = ruleMapper;
        this.recordMapper = recordMapper;
        this.scheduleMapper = scheduleMapper;
        this.attendanceMapper = attendanceMapper;
        this.appealMapper = appealMapper;
        this.systemYearMapper = systemYearMapper;
        this.userScoreMapper = userScoreMapper;
        this.affiliate1Mapper = affiliate1Mapper;
        this.affiliate2Mapper = affiliate2Mapper;
        this.ruleService = ruleService;
    }

    @Override
    public Result login(LoginDto loginDto) {
        String username = loginDto.getUsername();
        String password = loginDto.getPassword();
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("username",username);
        User user = userMapper.selectOne(userQueryWrapper);
        if (user != null) {
            JSONObject jsonObjectUser = (JSONObject) JSONObject.toJSON(user);
            jsonObjectUser.put("systemYear",systemYearMapper.getSystemYear());
            jsonObjectUser.put("token",getToken(user));
            // 如果用户存在并且密码正确。
            if (user.getPassword().equals(password)) {
                // 当用户的登录角色为教师时
                if (user.getLogo() == -1) {
                    // 查询教师的成绩
                    JSONObject jsonObject = new JSONObject();
                    userGetScoreData(user.getId(),jsonObject);
                    jsonObject.putAll(jsonObjectUser);
                    return Result.success("登录成功", jsonObject);
                } else {
                    // 当用户的登录角色为非教师时
                    return Result.success("登录成功",jsonObjectUser);
                }
            }
        }
        return Result.error("用户名或者密码不存在");
    }

    @Override
    @Transactional(rollbackFor=RuntimeException.class)
    public Result addUser(User newUser,List<String> departments) {
        // 当用户编号不存在时插入user
        if (!existUser(newUser.getNo(),newUser.getUsername())) {
            // 将新用户插入user表
            userMapper.insert(newUser);
            // user_score表初始化操作
            String year = systemYearMapper.getSystemYear();
            QueryWrapper<Rule> ruleQueryWrapper = new QueryWrapper<>();
            ruleQueryWrapper.select("classify").groupBy("classify");
            List<Rule> rules = ruleMapper.selectList(ruleQueryWrapper);
            for (Rule rule : rules) {
                UserScore userScore = new UserScore(null,newUser.getId(),year,rule.getClassify(),(float) 0);
                userScoreMapper.insert(userScore);
            }
            // 将用户插入department_user表
            ArrayList<String> ids = new ArrayList<>();
            for (String department : departments) {
                QueryWrapper<User> userQueryWrapper
                        = new QueryWrapper<User>().eq("name", department);
                ids.add(userMapper.selectOne(userQueryWrapper).getId());
            }
            for (String id : ids) {
                departmentUserMapper.insert(new DepartmentUser(id,newUser.getId()));
            }
            return Result.success("添加用户成功");
        } else {
            // 当用户编号存在时，应该提示用户该用户编号已经存在，添加失败
            return Result.error("添加用户失败！(原因：该教工编号或用户名已经存在)");
        }
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public Result getAllUser(String userId) {
        JSONArray jsonArray = new JSONArray();
        User user = userMapper.selectById(userId);
        switch (user.getLogo()) {
            case 1:
                QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
                userQueryWrapper.ne("logo",1);
                List<User> userList = userMapper.selectList(userQueryWrapper);
                for (User user1 : userList) {
                    JSONObject jsonObject = (JSONObject) JSONObject.toJSON(user1);
                    if (user1.getLogo() == -1) {
                        userGetScoreData(user1.getId(),jsonObject);
                        getUserDepartments(user1.getId(),jsonObject);
                    }
                    jsonArray.add(jsonObject);
                }
                break;
            case 2:
                jsonArray = getAllUserUtils(userId, null, user.getLogo());
                break;
            case 3:
                jsonArray = getAllUserUtils(null, userId, user.getLogo());
                break;
            case 4:
                QueryWrapper<DepartmentUser> departmentUserQueryWrapper = new QueryWrapper<>();
                departmentUserQueryWrapper.eq("department_id",user.getId());
                List<DepartmentUser> departmentUsers = this.departmentUserMapper.selectList(departmentUserQueryWrapper);
                for (DepartmentUser departmentUser : departmentUsers) {
                    User user1 = userMapper.selectById(departmentUser.getUserId());
                    JSONObject jsonObject = (JSONObject) JSONObject.toJSON(user1);
                    getUserDepartments(user1.getId(),jsonObject);
                    userGetScoreData(user1.getId(),jsonObject);
                    jsonArray.add(jsonObject);
                }
                break;
            default:
                break;
        }
        return Result.success(jsonArray);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public Result modifyScore(Record record) {
        User tch = userMapper.selectById(record.getTchId());
        Rule rule = ruleMapper.selectById(record.getRuleId());
        String classify = rule.getClassify();
        calculateSco(classify, tch, record);
        return Result.success("积分修改成功");
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public Result modifyScoreBatch(List<User> tchList, Record record) {
        Rule rule = ruleMapper.selectById(record.getRuleId());
        String classify = rule.getClassify();
        for (User tch : tchList) {
            Record recordCopy = new Record();
            BeanUtils.copyProperties(record, recordCopy);
            recordCopy.setTchId(tch.getId());
            calculateSco(classify, tch, recordCopy);
        }
        return Result.success("积分修改成功");
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public Result modifyInfo(String id,String changePro, String proValue) {
        UpdateWrapper<User> userUpdateWrapper = new UpdateWrapper<>();
        userUpdateWrapper.eq("id",id);
        userUpdateWrapper.set(changePro,proValue);
        userMapper.update(null,userUpdateWrapper);
        return Result.success("修改成功!");
    }

    @Override
    public Result getRoleList(String id) {
        return null;
    }


    @Override
    public Result getSpecialAverage() {
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<User>().eq("logo", -1);
        List<User> tchList = userMapper.selectList(userQueryWrapper);
        int tchNum = tchList.size();
        JSONArray jsonArray = new JSONArray();
        ArrayList<String> titles = new ArrayList<>();

        QueryWrapper<UserScore> userScoreQueryWrapper = new QueryWrapper<>();
        userScoreQueryWrapper.groupBy("classify");
        List<UserScore> userScores = userScoreMapper.selectList(userScoreQueryWrapper);

        // 专项积分统计
        for (UserScore userScore : userScores) {
            titles.add(userScore.getClassify());
        }
        for (String title : titles) {
            float sum = 0;
            JSONObject jsonObject = new JSONObject();
            QueryWrapper<UserScore> userScoreQueryWrapper1 = new QueryWrapper<>();
            userScoreQueryWrapper1.eq("classify",title);
            List<UserScore> userScores1 = userScoreMapper.selectList(userScoreQueryWrapper1);
            for (UserScore userScore : userScores1) {
                sum += userScore.getScore();
            }
            jsonObject.put("title",title+"平均分");
            jsonObject.put("num",NumberUtil.round(sum/tchNum,2));
            jsonArray.add(jsonObject);
        }

        // 其他三项积分统计
        float sumScoreSpecialAverage = 0;
        float sumScoreAnnual = 0;
        float sumScoreAnnualAverage = 0;
        for (User user : tchList) {
            sumScoreSpecialAverage += user.getScoreSpecialAverage();
            sumScoreAnnual += user.getScoreAnnual();
            sumScoreAnnualAverage += user.getScoreSpecialAverage();
        }

        // 其他三项积分
        JSONObject jsonObject1 = new JSONObject();
        jsonObject1.put("title","平均专项积分平均分");
        jsonObject1.put("num",NumberUtil.round(sumScoreSpecialAverage/tchNum,2));

        JSONObject jsonObject2 = new JSONObject();
        jsonObject2.put("title","年度积分平均分");
        jsonObject2.put("num",NumberUtil.round(sumScoreAnnual/tchNum,2));

        JSONObject jsonObject3 = new JSONObject();
        jsonObject3.put("title","平均年度积分平均分");
        jsonObject3.put("num",NumberUtil.round(sumScoreAnnualAverage/tchNum,2));

        jsonArray.add(jsonObject2);
        jsonArray.add(jsonObject1);
        jsonArray.add(jsonObject3);


        return Result.success(jsonArray);
    }

    @Override
    public Result getGraphData(List<Integer>paramArray, String classify) {
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < paramArray.size() - 1; i++) {
            int left = paramArray.get(i);
            int right = paramArray.get(i+1);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name",paramFormat(left,right));
            jsonObject.put("value",getAreaValue(left,right,classify));
            jsonArray.add(jsonObject);
        }
        return Result.success(jsonArray);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void deleteUser(String userId) {
        // 1. 删除user_score表中的对应数据
        UpdateWrapper<UserScore> userScoreUpdateWrapper = new UpdateWrapper<>();
        userScoreUpdateWrapper.eq("user_id",userId);
        userScoreMapper.delete(userScoreUpdateWrapper);
        // 2. 删除department_user表中的数据
        UpdateWrapper<DepartmentUser> departmentUserUpdateWrapper = new UpdateWrapper<>();
        departmentUserUpdateWrapper.eq("user_id",userId);
        departmentUserMapper.delete(departmentUserUpdateWrapper);

        ArrayList<String> recordIds = new ArrayList<>();
        List<Record> records = recordMapper.selectList(new QueryWrapper<Record>().eq("tch_id", userId));
        for (Record record : records) {
            recordIds.add(record.getId());
        }

        // 3. 删除appeal表对应数据
        if (recordIds.size() > 0) {
            UpdateWrapper<Appeal> appealUpdateWrapper = new UpdateWrapper<>();
            appealUpdateWrapper.in("record_id",recordIds);
            appealMapper.delete(appealUpdateWrapper);
        }

        // 4. 删除record表对应的数据
        UpdateWrapper<Record> recordUpdateWrapper = new UpdateWrapper<>();
        recordUpdateWrapper.eq("tch_id",userId);
        recordMapper.delete(recordUpdateWrapper);

        // 5.删除schedule表中的对应数据
        UpdateWrapper<Schedule> scheduleUpdateWrapper = new UpdateWrapper<>();
        scheduleUpdateWrapper.eq("user_id",userId);
        scheduleMapper.delete(scheduleUpdateWrapper);

        // 6.删除attendance表中的对应数据
        UpdateWrapper<Attendance> attendanceUpdateWrapper = new UpdateWrapper<>();
        attendanceUpdateWrapper.eq("user_id",userId);
        attendanceMapper.delete(attendanceUpdateWrapper);

        // 7.删除user表中的对应数据
        userMapper.deleteById(userId);
    }

    @Override
    public Result addUserRole(String userId, String role, String department) {
//        // 1. 判断需要增加的是什么角色，如果是教师，则不限添加
//        if (Final.ADMIN.equals(role)) {
//            // 2. 当用户的添加的角色是管理员时，如果长度大于1添加失败
//            QueryWrapper<DepartmentUser> departUserQueryWrapper = new QueryWrapper<>();
//            departUserQueryWrapper.eq("user_id",userId).eq("role_name",Final.ADMIN);
//            List<DepartmentUser> departmentUsers = departmentUserMapper.selectList(departUserQueryWrapper);
//            if (departmentUsers.size() > 0) {
//                return Result.error("添加失败！一个管理员只能管理一个部门");
//            }
//        }
//        // 3. 当用户只有一个角色，操作depart_user表为用户添加角色
//        DepartmentUser departmentUser = new DepartmentUser(getDepartId(department),userId,role);
//        try {
//            departmentUserMapper.insert(departmentUser);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return Result.error("失败！可能的原因：重复添加相同的数据");
//        }
//        return Result.success("添加成功");
        return null;
    }

    @Override
    public Result changeDepartment(String userId, String oriDepartment, String nowDepartment) {
//        String oriDepartmentId = getDepartId(oriDepartment);
//        String nowDepartmentId = getDepartId(nowDepartment);
//        UpdateWrapper<DepartmentUser> updateWrapper = null;
//        try {
//            updateWrapper = new UpdateWrapper<DepartmentUser>()
//                    .eq("depart_id", oriDepartmentId)
//                    .set("depart_id", nowDepartmentId);
//            departmentUserMapper.update(null,updateWrapper);
//            return Result.success("修改成功");
//        } catch (Exception e) {
//            e.printStackTrace();
//            return Result.error("失败！可能的原因：重复添加相同的数据");
//        }
        return null;
    }

    @Override
    public Result getDepartments() {
        // 依次查询logo为2、3、4的部门
        JSONArray jsonArray = new JSONArray();
        List<IdDto> idDtos = userMapper.getDepartments(null,null);
        for (IdDto idDto : idDtos) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name",userMapper.selectById(idDto.getId1()).getName());
            String id2 = idDto.getId2();
            String id3 = idDto.getId3();
            User userId2 = userMapper.selectById(id2);
            User userId3 = userMapper.selectById(id3);
            if (userId2 != null) {
                jsonObject.put("affiliate1",userId2.getName());
            }
            if (userId3 != null) {
                jsonObject.put("affiliate2",userId3.getName());
            }
            jsonArray.add(jsonObject);
        }
        return Result.success(jsonArray);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public Result addDepartment(NewDepartment newDepartment) {
        // 先插入user表
        User user = new User();
        BeanUtils.copyProperties(newDepartment,user);
        userMapper.insert(user);
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        switch (user.getLogo()) {
            case 3:
                userQueryWrapper.eq("name", newDepartment.getSupDepartment());
                User user1 = userMapper.selectOne(userQueryWrapper);
                Affiliate1 affiliate1 = new Affiliate1(user1.getId(),user.getId());
                affiliate1Mapper.insert(affiliate1);
                break;
            case 4:
                userQueryWrapper.eq("name",newDepartment.getSupDepartment());
                User supUser = userMapper.selectOne(userQueryWrapper);
                Affiliate2 affiliate2 = new Affiliate2(supUser.getId(),user.getId());
                affiliate2Mapper.insert(affiliate2);
                break;
            default:
                break;
        }
        return Result.success("添加部门成功");
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public Result userModify(TempUserDto tempUser) {
        User user = new User();
        BeanUtils.copyProperties(tempUser,user);
        userMapper.updateById(user);
        List<String> newDepartments = tempUser.getNewDepartments();
        if (user.getLogo() == -1 && newDepartments != null && newDepartments.size() > 0) {
            for (String newDepartment : newDepartments) {
                QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
                userQueryWrapper.eq("name", newDepartment);
                User user1 = userMapper.selectOne(userQueryWrapper);
                DepartmentUser departmentUser = new DepartmentUser(user1.getId(), tempUser.getId());
                departmentUserMapper.insert(departmentUser);
            }
        }
        return Result.success("修改成功");
    }

    public String getToken(User user) {
        String token="";
        token= JWT.create().withAudience(user.getId())
                .sign(Algorithm.HMAC256(user.getPassword()));
        return token;
    }

    private void calculateSco(String classify, User tch, Record record) {
        Float addScore = record.getAddScore();
        Float desScore = record.getDesScore();
        List<String> classifyList = ruleService.getClassify();
        int index = classifyList.indexOf(classify);
        String modifyClassify = classifyList.get(index);
        float sum = 0;
        float score = 0;
        String systemYear = systemYearMapper.getSystemYear();
        int annualSize = userScoreMapper.selectList(new QueryWrapper<UserScore>().groupBy("year")).size();
        QueryWrapper<UserScore> userScoreQueryWrapper = new QueryWrapper<>();
        userScoreQueryWrapper.eq("user_id",tch.getId());
        List<UserScore> userScores = userScoreMapper.selectList(userScoreQueryWrapper);

        for (UserScore userScore : userScores) {
            if (userScore.getClassify().equals(modifyClassify)) {
                score = userScore.getScore();
            }
            sum += userScore.getScore();
        }

        UpdateWrapper<UserScore> userScoreUpdateWrapper = new UpdateWrapper<>();
        userScoreUpdateWrapper
                .eq("user_id",tch.getId())
                .eq("classify",modifyClassify)
                .eq("year",systemYear)
                .set("score",score+addScore+desScore);
        userScoreMapper.update(null,userScoreUpdateWrapper);

        sum += addScore + desScore;

        tch.setScoreSpecialAverage(sum / classifyList.size());
        tch.setScoreAnnual(sum);
        tch.setScoreAnnualAverage(sum / annualSize);
        record.setYear(systemYear);
        userMapper.updateById(tch);
        recordMapper.insert(record);
    }

    private boolean existUser(String no,String username) {
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("no",no).or().eq("username",username);
        User user = userMapper.selectOne(userQueryWrapper);
        return user != null;
    }

    private int getAreaValue(int left, int right, String classify) {
        QueryWrapper<UserScore> userScoreQueryWrapper = new QueryWrapper<>();
        userScoreQueryWrapper.eq("classify",classify)
                .ge("score",left).lt("score",right);
        return userScoreMapper.selectList(userScoreQueryWrapper).size();
    }

    private String paramFormat(int left, int right) {
        return "[" + left + "," + right + ")";
    }

    private JSONArray getAllUserUtils(String userId,String p2,Integer logo) {
        JSONArray jsonArray = new JSONArray();
        List<IdDto> idDtos = null;
        boolean tempBool = false;
        if (logo == 2) {
            tempBool = true;
            idDtos = userMapper.getDepartments(userId,null);
        } else if (logo == 3) {
            idDtos = userMapper.getDepartments(null,p2);
        }
        HashSet<String> ids = MyUtils.idDtoUtils(idDtos,tempBool);
        QueryWrapper<DepartmentUser> departmentUserQueryWrapper1 = new QueryWrapper<>();
        departmentUserQueryWrapper1.in("department_id",ids);
        List<DepartmentUser> departmentUsers = departmentUserMapper.selectList(departmentUserQueryWrapper1);
        HashSet<String> idsResult = new HashSet<>(); // 使用Java集合去重
        for (DepartmentUser departmentUser : departmentUsers) {
            idsResult.add(departmentUser.getUserId());
        }
        for (String id : idsResult) {
            User user1 = userMapper.selectById(id);
            JSONObject jsonObject = (JSONObject) JSONObject.toJSON(user1);
            if (user1.getLogo() == -1) {
                getUserDepartments(user1.getId(),jsonObject);
                userGetScoreData(user1.getId(),jsonObject);
            }
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }

    private void getUserDepartments(String userId, JSONObject jsonObject) {
        List<String> departments = new ArrayList<>();
        QueryWrapper<DepartmentUser> departmentUserQueryWrapper1 = new QueryWrapper<>();
        departmentUserQueryWrapper1.eq("user_id",userId);
        List<DepartmentUser> departmentUsers = departmentUserMapper.selectList(departmentUserQueryWrapper1);
        for (DepartmentUser departmentUser : departmentUsers) {
            String department = userMapper.selectById(departmentUser.getDepartmentId()).getName();
            departments.add(department);
        }
        jsonObject.put("departments",CollUtil.join(departments, "、"));
    }

    private void userGetScoreData(String userId, JSONObject jsonObject) {
        QueryWrapper<UserScore> userScoreQueryWrapper = new QueryWrapper<>();
        userScoreQueryWrapper
                .eq("user_id",userId)
                .eq("year",systemYearMapper.getSystemYear())
                .select("year","classify","score");
        List<UserScore> userScoreList = userScoreMapper.selectList(userScoreQueryWrapper);

        List<HashMap<String, Object>> mapList = new ArrayList<>();
        for (UserScore userScore : userScoreList) {
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("classify",userScore.getClassify()+"专项积分");
            hashMap.put("score",userScore.getScore());
            mapList.add(hashMap);
        }
        jsonObject.put("mapList",mapList);
    }
}
