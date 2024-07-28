package edu.hitwh.homework.controller;

import edu.hitwh.homework.entity.Employee;
import edu.hitwh.homework.entity.utils.JwtUtils;
import edu.hitwh.homework.entity.utils.Result;
import edu.hitwh.homework.service.EmpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 登录控制器，处理用户登录请求
 */
@RestController
@CrossOrigin
public class LoginController {
    @Autowired
    private EmpService empService;

    /**
     * 用户登录接口
     *
     * @param employee 包含用户名和密码的员工对象
     * @return 登录结果，成功返回token，失败返回错误消息
     */
    @PostMapping("/login")
    public Result login(@RequestBody Employee employee) {
        // 调用Service层进行登录验证
        Employee e = empService.login(employee);
        // 如果登录成功
        if (e != null) {
            // 构造JWT的claims
            Map<String, Object> claims = new HashMap<>();
            claims.put("id", e.getId());
            claims.put("username", e.getUsername());
            claims.put("name", e.getName());

            // 生成JWT并返回
            String token = JwtUtils.generateJwt(claims);
            return Result.success(token);
        }
        // 登录失败，返回错误消息
        return Result.fail("用户名或密码错误");
    }
}
