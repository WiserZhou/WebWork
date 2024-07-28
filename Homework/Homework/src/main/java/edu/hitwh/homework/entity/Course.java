package edu.hitwh.homework.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Course {
    private Integer id; // 课程ID
    private String courseId; // 课程编号
    private String name; // 课程名称
    private Short attribute; // 课程属性
    private String classRoom; // 上课教室
    private double score; // 课程学分
    private Integer totalTime; // 总学时
}
