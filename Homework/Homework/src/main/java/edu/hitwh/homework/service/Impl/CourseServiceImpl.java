package edu.hitwh.homework.service.Impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import edu.hitwh.homework.mapper.CourseMapper;
import edu.hitwh.homework.entity.Course;
import edu.hitwh.homework.entity.utils.PageBean;
import edu.hitwh.homework.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseMapper courseMapper;

    /**
     * 分页查询课程信息
     *
     * @param page      页码
     * @param pageSize  每页记录数
     * @param name      课程名称
     * @param attribute 课程属性
     * @param classRoom 教室名称
     * @return 分页后的课程信息
     */
    @Override
    public PageBean page(Integer page, Integer pageSize, String name, Short attribute, String classRoom) {
        PageHelper.startPage(page, pageSize);
        List<Course> courseList = courseMapper.list(name, attribute, classRoom);
        PageInfo<Course> pageInfo = new PageInfo<>(courseList);
        return new PageBean(pageInfo.getTotal(), pageInfo.getList());
    }

    /**
     * 保存课程信息
     *
     * @param course 待保存的课程信息
     */
    @Override
    public void save(Course course) {
        try {
            courseMapper.insert(course);
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new RuntimeException("该课程代码已存在", e);
        }
    }

    /**
     * 更新课程信息
     *
     * @param course 待更新的课程信息
     */
    @Override
    public void update(Course course) {
        try {
            courseMapper.update(course);
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new RuntimeException("该课程代码已存在", e);
        }
    }

    /**
     * 根据课程ID查询课程信息
     *
     * @param id 课程ID
     * @return 查询到的课程信息
     */
    @Override
    public Course getById(Integer id) {
        return courseMapper.findByID(id);
    }

    /**
     * 批量删除课程信息
     *
     * @param ids 待删除的课程ID列表
     */
    @Override
    public void delete(List<Integer> ids) {
        courseMapper.delete(ids);
    }
}
