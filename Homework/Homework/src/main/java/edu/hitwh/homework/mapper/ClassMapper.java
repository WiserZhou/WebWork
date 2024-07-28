package edu.hitwh.homework.mapper;

import edu.hitwh.homework.entity.Classes;
import org.apache.ibatis.annotations.*;

import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDate;
import java.util.List;

@Mapper
public interface ClassMapper {

    // 根据条件查询班级列表
    List<Classes> list(@Param("name") String name, @Param("begin") LocalDate begin, @Param("end") LocalDate end);

    // 插入班级信息
    @Insert("INSERT INTO class (name, classroom, start_time, end_time, teacher_id) " +
            "VALUES (#{name}, #{classroom}, #{startTime}, #{endTime}, #{teacherId})")
    void insert(Classes classes) throws SQLIntegrityConstraintViolationException;

    // 更新班级信息
    void update(Classes classes) throws SQLIntegrityConstraintViolationException;

    // 删除班级信息
    @Delete("DELETE FROM class WHERE id = #{id}")
    void delete(Integer id) throws SQLIntegrityConstraintViolationException;

    // 获取所有班级列表
    @Select("SELECT * FROM class")
    List<Classes> getAll();

    // 根据ID查询班级信息
    @Select("SELECT * FROM class WHERE id = #{id}")
    Classes findByID(Integer id);
}
