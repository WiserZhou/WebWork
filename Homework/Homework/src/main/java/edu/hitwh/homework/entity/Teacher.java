package edu.hitwh.homework.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Teacher {
    private Integer teacherID; // 教师ID
    private String teacherName; // 教师姓名
}
