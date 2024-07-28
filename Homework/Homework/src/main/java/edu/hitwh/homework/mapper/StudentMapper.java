package edu.hitwh.homework.mapper;

import edu.hitwh.homework.entity.Student;
import edu.hitwh.homework.entity.Score;
import org.apache.ibatis.annotations.*;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

@Mapper
public interface StudentMapper {

    // 查询学生列表
    List<Student> list(String name, String stuId, Short education, Short classId);

    // 新增学生
    @Insert("INSERT INTO student (name, stu_id, class_id, gender, telephone, education, update_time) " +
            "VALUES (#{name}, #{stuId}, #{classId}, #{gender}, #{telephone}, #{education}, #{updateTime})")
    void insert(Student student) throws SQLIntegrityConstraintViolationException;

    // 更新学生信息
    void update(Student student) throws SQLIntegrityConstraintViolationException;

    // 根据ID查询学生信息
    @Select("SELECT * FROM student WHERE id = #{id}")
    Student findByID(Integer id);

    // 删除学生
    void delete(List<Integer> ids);

    // 学生积分更新
    @Update("UPDATE student SET discipline_num = discipline_num + 1, discipline_score = discipline_score + #{points} WHERE id = #{id}")
    void points(Score studentPoints);

    // 查询指定性别的学生数量
    @Select("SELECT COUNT(*) FROM student WHERE gender=#{gender}")
    int genderCount(Integer gender);

    // 查询指定学历的学生数量
    @Select("SELECT COUNT(*) FROM student WHERE education=#{education}")
    int educationCount(String education);
}
