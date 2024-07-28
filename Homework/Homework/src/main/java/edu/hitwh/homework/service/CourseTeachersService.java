package edu.hitwh.homework.service;

import edu.hitwh.homework.entity.utils.PageBean;

import java.util.List;

/**
 * 课程安排服务接口，定义了对课程安排信息进行查询和更新操作的方法
 */
public interface CourseTeachersService {

    /**
     * 分页查询课程安排信息
     *
     * @param page     当前页码
     * @param pageSize 每页显示数量
     * @param name     课程名称（可选）
     * @param arranged 是否已安排教师（可选）
     * @param teacher  教师ID（可选）
     * @return 分页结果对象
     */
    PageBean page(Integer page, Integer pageSize, String name, Short arranged, Short teacher);

    /**
     * 更新课程的教师安排
     *
     * @param courseId   课程ID
     * @param teachersId 教师ID列表
     */
    void update(Integer courseId, List<Integer> teachersId);
}
