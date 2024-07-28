package edu.hitwh.homework.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Student {
    private Integer id; // 学生ID
    private String name; // 姓名
    private String stuId; // 学号
    private Short gender; // 性别
    private Integer classId; // 班级ID
    private String telephone; // 电话号码
    private String education; // 学历
    private Integer disciplineNum; // 违纪次数
    private Integer disciplineScore; // 违纪分数
    private LocalDateTime updateTime; // 更新时间
}
