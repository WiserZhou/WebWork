package edu.hitwh.homework.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Classes {
    private Integer id; // 班级ID
    private String name; // 班级名称
    private String classroom; // 上课教室
    private LocalDate startTime; // 开始时间
    private LocalDate endTime; // 结束时间
    private Integer teacherId; // 教师ID
}
