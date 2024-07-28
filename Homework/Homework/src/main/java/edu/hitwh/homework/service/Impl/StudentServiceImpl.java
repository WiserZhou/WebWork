package edu.hitwh.homework.service.Impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import edu.hitwh.homework.mapper.StudentMapper;
import edu.hitwh.homework.entity.utils.PageBean;
import edu.hitwh.homework.entity.Student;
import edu.hitwh.homework.entity.Score;
import edu.hitwh.homework.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentMapper studentMapper;

    /**
     * 分页查询学员信息
     *
     * @param page      页码
     * @param pageSize  每页展示记录数
     * @param name      姓名
     * @param stuId     学号
     * @param education 学历
     * @param classId   班级ID
     * @return 分页结果
     */
    @Override
    public PageBean page(Integer page, Integer pageSize, String name, String stuId, Short education, Short classId) {
        PageHelper.startPage(page, pageSize);
        List<Student> studentList = studentMapper.list(name, stuId, education, classId);
        PageInfo<Student> p = new PageInfo<Student>(studentList);
        PageBean pageBean = new PageBean(p.getTotal(), p.getList());
        return pageBean;
    }

    /**
     * 保存学员信息
     *
     * @param student 待保存的学员信息
     */
    @Override
    public void save(Student student) {
        student.setUpdateTime(LocalDateTime.now());
        try {
            studentMapper.insert(student);
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new RuntimeException("该学号已存在", e);
        }
    }

    /**
     * 更新学员信息
     *
     * @param student 待更新的学员信息
     */
    @Override
    public void update(Student student) {
        student.setUpdateTime(LocalDateTime.now());
        try {
            studentMapper.update(student);
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new RuntimeException("该学号已存在", e);
        }
    }

    /**
     * 根据学员ID查询学员信息
     *
     * @param id 学员ID
     * @return 学员信息
     */
    @Override
    public Student getById(Integer id) {
        return studentMapper.findByID(id);
    }

    /**
     * 删除学员
     *
     * @param ids 待删除的学员ID列表
     */
    @Override
    public void delete(List<Integer> ids) {
        studentMapper.delete(ids);
    }

    /**
     * 学员违纪扣分操作
     *
     * @param studentPoints 学员违纪扣分记录
     */
    @Override
    public void points(Score studentPoints) {
        studentMapper.points(studentPoints);
    }

    /**
     * 统计学员性别
     *
     * @return 性别统计结果
     */
    @Override
    public Map<String, Integer> genderCount() {
        Map<String, Integer> countMap = new HashMap<>();
        int male = studentMapper.genderCount(1);
        int female = studentMapper.genderCount(2);
        countMap.put("男性学员", male);
        countMap.put("女性学员", female);
        return countMap;
    }

    /**
     * 统计学员学历情况
     *
     * @return 学历统计结果
     */
    @Override
    public Map<String, Integer> educationCount() {
        Map<String, Integer> countMap = new HashMap<>();
        String[] eduNames = {"初中", "高中", "大专", "本科", "硕士", "博士"};
        int[] eduIds = {1, 2, 3, 4, 5, 6};
        for (int i = 0; i < eduNames.length; i++) {
            int count = studentMapper.educationCount(eduNames[i]);
            countMap.put(eduNames[i], count);
        }
        return countMap;
    }
}
