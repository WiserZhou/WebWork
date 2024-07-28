package edu.hitwh.homework.mapper;

import edu.hitwh.homework.entity.Course;
import org.apache.ibatis.annotations.*;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

@Mapper
public interface CourseMapper {

    // 查询列表
    List<Course> list(@Param("name") String name, @Param("attribute") Short attribute, @Param("classRoom") String classRoom);

    // 新增课程
    @Insert("INSERT INTO course (course_id, name, attribute, class_room, score, total_time) " +
            "VALUES (#{courseId}, #{name}, #{attribute}, #{classRoom}, #{score}, #{totalTime})")
    void insert(Course course) throws SQLIntegrityConstraintViolationException;

    // 更新课程信息
    void update(Course course) throws SQLIntegrityConstraintViolationException;

    // 根据课程ID查询课程信息
    @Select("SELECT * FROM course WHERE id = #{id}")
    Course findByID(Integer id);

    // 批量删除课程
    void delete(List<Integer> ids);

    // 更新课程的教师信息
    @Update("UPDATE course SET teacher = #{teacher} WHERE id = #{id}")
    void updateTeacher(@Param("teacher") Integer teacher, @Param("id") Integer id);
}
