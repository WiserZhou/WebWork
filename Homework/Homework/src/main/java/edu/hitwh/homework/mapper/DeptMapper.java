package edu.hitwh.homework.mapper;

import edu.hitwh.homework.entity.Dept;
import org.apache.ibatis.annotations.*;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

@Mapper
public interface DeptMapper {

    // 查询部门列表
    @Select("SELECT * FROM dept")
    List<Dept> list();

    // 根据部门ID删除部门信息
    @Delete("DELETE FROM dept WHERE id=#{id}")
    void deleteById(Integer id);

    // 新增部门信息
    @Insert("INSERT INTO dept (name, create_time, update_time) VALUES (#{name}, #{createTime}, #{updateTime})")
    void add(Dept dept) throws SQLIntegrityConstraintViolationException;

    // 更新部门信息
    @Update("UPDATE dept SET name=#{name}, update_time=#{updateTime} WHERE id=#{id}")
    void update(Dept dept) throws SQLIntegrityConstraintViolationException;
}
