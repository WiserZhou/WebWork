package edu.hitwh.homework.mapper;

import edu.hitwh.homework.entity.Employee;
import org.apache.ibatis.annotations.*;

import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDate;
import java.util.List;

/**
 * 员工管理
 */
@Mapper
public interface EmpMapper {

    // 查询员工列表
    List<Employee> list(String name, Short gender, LocalDate begin, LocalDate end);

    // 获取所有员工
    @Select("SELECT * FROM emp")
    List<Employee> getAll();

    // 新增员工
    @Insert("INSERT INTO emp (username, name, gender, image, job, entry_date, dept_id, create_time, update_time) " +
            "VALUES (#{username}, #{name}, #{gender}, #{image}, #{job}, #{entryDate}, #{deptId}, #{createTime}, #{updateTime})")
    void insert(Employee employee) throws SQLIntegrityConstraintViolationException;

    // 修改员工信息
    void update(Employee employee);

    // 根据ID查询员工信息
    @Select("SELECT * FROM emp WHERE id = #{id}")
    Employee findByID(Integer id);

    // 删除员工
    void delete(List<Integer> ids) throws SQLIntegrityConstraintViolationException;

    // 查询指定性别的员工数量
    @Select("SELECT COUNT(*) FROM emp WHERE gender=#{gender}")
    int genderCount(Integer gender);

    // 查询指定职务的员工数量
    @Select("SELECT COUNT(*) FROM emp WHERE job=#{job}")
    int jobCount(Integer job);

    // 根据用户名和密码查询员工信息
    @Select("SELECT id, username, password, name, gender, image, job, entry_date, dept_id, create_time, update_time " +
            "FROM emp WHERE username=#{username} AND password=#{password}")
    Employee getByUsernameAndPassword(Employee employee);
}
