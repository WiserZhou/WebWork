package edu.hitwh.homework.controller;

import edu.hitwh.homework.entity.utils.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器，用于统一处理Controller层抛出的异常
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理所有异常
     *
     * @param e 异常对象
     * @return 统一格式的错误响应
     */
    @ExceptionHandler(Exception.class)
    public Result handleException(Exception e) {
        e.printStackTrace(); // 打印异常堆栈信息
        return Result.fail("对不起，操作失败，请联系管理员"); // 响应一个标准的错误消息
    }
}
