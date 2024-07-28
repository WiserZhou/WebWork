package edu.hitwh.homework.entity.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result {
    private Integer code; // 响应状态码
    private String msg; // 响应消息
    private Object data; // 响应数据

    /**
     * 创建一个成功的 Result 对象，不带数据
     *
     * @return Result 对象
     */
    public static Result success() {
        return new Result(200, "success", null);
    }

    /**
     * 创建一个成功的 Result 对象，带数据
     *
     * @param data 响应数据
     * @return Result 对象
     */
    public static Result success(Object data) {
        return new Result(200, "success", data);
    }

    /**
     * 创建一个失败的 Result 对象，带错误消息
     *
     * @param msg 错误消息
     * @return Result 对象
     */
    public static Result fail(String msg) {
        return new Result(400, msg, null);
    }
}
