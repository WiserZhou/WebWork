package edu.hitwh.homework.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Employee {
    private Integer id; // 员工ID
    private String username; // 用户名
    private String password; // 密码
    private String name; // 姓名
    private Short gender; // 性别（0表示女性，1表示男性）
    private String image; // 头像图片路径
    private Short job; // 职位
    private LocalDate entryDate; // 入职日期
    private Integer deptId; // 部门ID
    private LocalDateTime createTime; // 创建时间
    private LocalDateTime updateTime; // 更新时间
}
