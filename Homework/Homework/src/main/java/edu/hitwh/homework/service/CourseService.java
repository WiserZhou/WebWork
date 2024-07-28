package edu.hitwh.homework.service;

import edu.hitwh.homework.entity.Course;
import edu.hitwh.homework.entity.utils.PageBean;

import java.util.List;

/**
 * 课程服务接口，定义了对课程信息进行查询、保存、更新和删除操作的方法
 */
public interface CourseService {

    /**
     * 分页查询课程信息
     *
     * @param page      当前页码
     * @param pageSize  每页显示数量
     * @param name      课程名称（可选）
     * @param attribute 课程属性（可选）
     * @param classRoom 教室（可选）
     * @return 分页结果对象
     */
    PageBean page(Integer page, Integer pageSize, String name, Short attribute, String classRoom);

    /**
     * 保存课程信息
     *
     * @param course 要保存的课程对象
     */
    void save(Course course);

    /**
     * 更新课程信息
     *
     * @param course 要更新的课程对象
     */
    void update(Course course);

    /**
     * 根据课程ID查询课程信息
     *
     * @param id 课程ID
     * @return 课程对象
     */
    Course getById(Integer id);

    /**
     * 删除课程信息
     *
     * @param ids 要删除的课程ID列表
     */
    void delete(List<Integer> ids);
}
