package com.smile;

import com.smile.entity.UserScore;
import com.smile.entity.common.dto.GroupDepartmentDto;
import com.smile.entity.common.dto.IdDto;
import com.smile.mapper.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class CdfzApplicationTests {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserScoreMapper userScoreMapper;
    @Autowired
    private SystemYearMapper systemYearMapper;

    @Test
    void contextLoads() {
        String year = systemYearMapper.getSystemYear();
        System.out.println(year);
    }

    @Test
    void initData() {
        UserScore userScore1 =
                new UserScore(null,"1341724061226524674",
                        systemYearMapper.getSystemYear(),"工作努力",(float)0);
        UserScore userScore2 =
                new UserScore(null,"1341724721846181890",
                        systemYearMapper.getSystemYear(),"工作努力",(float)0);
        userScoreMapper.insert(userScore1);
        userScoreMapper.insert(userScore2);
    }
}
