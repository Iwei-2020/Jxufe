package com.smile.utils.listen;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.smile.entity.User;
import com.smile.entity.common.dto.UserDto;
import com.smile.service.UserService;
import org.springframework.beans.BeanUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * @author smilePlus
 */
public class UserListen extends AnalysisEventListener<UserDto> {

    private static final int BATCH_COUNT = 5;
    private final UserService userService;
    List<UserDto> userDtos = new ArrayList<>();
    List<List<String>> departmentList = new LinkedList<>();


    public UserListen(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void invoke(UserDto userDto, AnalysisContext analysisContext) {
        String departments = userDto.getDepartment();
        String[] departmentArray = departments.split("、");
        departmentList.add(Arrays.asList(departmentArray));
        userDtos.add(userDto);
        if (userDtos.size() >= BATCH_COUNT) {
            for (int i = 0; i < userDtos.size(); i++) {
                User newUser = new User();
                BeanUtils.copyProperties(userDtos.get(i), newUser);
                userService.addUser(newUser,departmentList.get(i));
            }
            userDtos.clear();
            departmentList.clear();
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        for (int i = 0; i < userDtos.size(); i++) {
            User newUser = new User();
            BeanUtils.copyProperties(userDtos.get(i), newUser);
            userService.addUser(newUser,departmentList.get(i));
        }
    }
}
