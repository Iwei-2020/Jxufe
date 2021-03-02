package com.smile.entity.common.other;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author smileplus
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result {
    private Integer code;
    private String description;
    private Object data;

    public static Result success(String description) {
        return new Result(1,description,null);
    }

    public static Result success(Object data) {
        return new Result(1,null,data);
    }

    public static Result success(String description, Object data) {
        return new Result(1,description,data);
    }

    public static Result error(String description) {
        return new Result(0,description,null);
    }
}
