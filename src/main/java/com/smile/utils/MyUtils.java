package com.smile.utils;

import com.smile.entity.common.dto.IdDto;
import org.apache.poi.ss.formula.functions.T;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * TODO
 *
 * @author smilePlus
 * @version 1.0
 * @date 2020/12/2221:52
 */
public class MyUtils {
    public static <T> void removeDuplicate(List<T> list) {
        HashSet<T> hashSet = new HashSet<>(list);
        list.clear();
        list.addAll(hashSet);
    }


    public static HashSet<String> idDtoUtils(List<IdDto> idDtos, boolean tempBool) {
        // tempBool添加id1与否
        HashSet<String> ids = new HashSet<>();
        if (idDtos != null) {
            for (IdDto idDto : idDtos) {
                if (idDto.getId1() != null && tempBool) {
                    ids.add(idDto.getId1());
                }
                if (idDto.getId2() != null) {
                    ids.add(idDto.getId2());
                }
                if (idDto.getId3() != null) {
                    ids.add(idDto.getId3());
                }
            }
        }
        return ids;
    }
}
