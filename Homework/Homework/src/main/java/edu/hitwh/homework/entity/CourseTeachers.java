package edu.hitwh.homework.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseTeachers {
    private Integer courseId; // 课程ID
    private String courseNo; // 课程编号
    private String courseName; // 课程名称
    private String classRoom; // 上课教室
    private String teacherIds; // 教师ID（可能是多个教师，用逗号分隔）
}
