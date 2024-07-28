package edu.hitwh.homework.service;

import edu.hitwh.homework.entity.Classes;
import edu.hitwh.homework.entity.utils.PageBean;

import java.time.LocalDate;
import java.util.List;

/**
 * 班级管理服务接口，定义了对班级信息进行增删改查操作的方法
 */
public interface ClassService {

    /**
     * 分页查询班级信息
     *
     * @param page     当前页码
     * @param pageSize 每页显示数量
     * @param name     班级名称（可选）
     * @param begin    开始时间（可选）
     * @param end      结束时间（可选）
     * @return 分页结果对象
     */
    PageBean page(Integer page, Integer pageSize, String name, LocalDate begin, LocalDate end);

    /**
     * 获取所有班级信息
     *
     * @return 班级列表
     */
    List<Classes> getAll();

    /**
     * 根据班级ID获取班级信息
     *
     * @param id 班级ID
     * @return 班级对象
     */
    Classes getById(Integer id);

    /**
     * 新增班级信息
     *
     * @param classes 班级对象
     */
    void save(Classes classes);

    /**
     * 修改班级信息
     *
     * @param classes 班级对象
     */
    void update(Classes classes);

    /**
     * 删除班级信息
     *
     * @param id 班级ID
     */
    void delete(Integer id);
}
