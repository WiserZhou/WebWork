package edu.hitwh.homework.mapper;

import edu.hitwh.homework.entity.CourseTeachers;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CourseTeachersMapper {

    // 查询列表
    List<CourseTeachers> list(@Param("name") String name, @Param("arranged") Short arranged, @Param("teacher") Short teacher);

    // 根据课程ID查询教师列表
    @Select("SELECT teacher_id FROM course_arrange WHERE course_id = #{id}")
    List<Integer> findByCourse(Integer id);

    // 插入课程安排
    @Insert("INSERT INTO course_arrange (course_id, teacher_id) VALUES (#{courseId}, #{teacherId})")
    void insert(@Param("courseId") Integer courseId, @Param("teacherId") Integer teacherId);

    // 删除课程教师安排
    @Delete("DELETE FROM course_arrange WHERE course_id = #{courseId} AND teacher_id = #{teacherId}")
    void delete(@Param("courseId") Integer courseId, @Param("teacherId") Integer teacherId);

    // 删除课程所有教师安排
    @Delete("DELETE FROM course_arrange WHERE course_id = #{courseId}")
    void deleteAll(Integer courseId);
}
