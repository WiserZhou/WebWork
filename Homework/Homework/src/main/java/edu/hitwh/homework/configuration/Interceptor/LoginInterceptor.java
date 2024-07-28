package edu.hitwh.homework.configuration.Interceptor;

import com.alibaba.fastjson.JSONObject;
import edu.hitwh.homework.entity.utils.JwtUtils;
import edu.hitwh.homework.entity.utils.Result;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
public class LoginInterceptor implements HandlerInterceptor {
    /**
     * 在请求处理之前进行拦截
     *
     * @param request  HTTP请求对象
     * @param response HTTP响应对象
     * @param handler  请求处理对象
     * @return 是否通过拦截
     * @throws Exception 可能抛出的异常
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 从请求头中获取token
        String token = request.getHeader("token");
        // 如果token为空
        if (!StringUtils.hasLength(token)) {
            log.info("Token不存在");
            // 构建未登录的返回结果
            Result responseResult = Result.fail("未登录，无法访问！");
            // 将结果转换为JSON字符串
            String json = JSONObject.toJSONString(responseResult);
            // 设置响应类型为JSON
            response.setContentType("application/json;charset=utf-8");
            // 将JSON写入响应体
            response.getWriter().write(json);
            // 拦截请求，不继续执行
            return false;
        }
        try {
            // 尝试解析token，若解析失败则抛出异常
            JwtUtils.parseJWT(token);
        } catch (Exception e) {
            log.info("令牌解析失败!");
            // 构建未登录的返回结果
            Result responseResult = Result.fail("未登录，无法访问！");
            // 将结果转换为JSON字符串
            String json = JSONObject.toJSONString(responseResult);
            // 设置响应类型为JSON
            response.setContentType("application/json;charset=utf-8");
            // 将JSON写入响应体
            response.getWriter().write(json);
            // 拦截请求，不继续执行
            return false;
        }
        // 通过拦截，继续执行请求
        return true;
    }
}