package edu.hitwh.homework.service.Impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import edu.hitwh.homework.mapper.CourseTeachersMapper;
import edu.hitwh.homework.mapper.CourseMapper;
import edu.hitwh.homework.entity.utils.PageBean;
import edu.hitwh.homework.entity.CourseTeachers;
import edu.hitwh.homework.service.CourseTeachersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CourseTeachersServiceImpl implements CourseTeachersService {

    @Autowired
    private CourseTeachersMapper courseTeachersMapper;

    @Autowired
    private CourseMapper courseMapper;

    /**
     * 分页查询课程安排信息
     *
     * @param page     页码
     * @param pageSize 每页记录数
     * @param name     课程名称
     * @param arranged 是否已安排
     * @param teacher  教师ID
     * @return 分页后的课程安排信息
     */
    @Override
    public PageBean page(Integer page, Integer pageSize, String name, Short arranged, Short teacher) {
        PageHelper.startPage(page, pageSize);
        List<CourseTeachers> courseTeacherList = courseTeachersMapper.list(name, arranged, teacher);
        PageInfo<CourseTeachers> pageInfo = new PageInfo<>(courseTeacherList);
        return new PageBean(pageInfo.getTotal(), pageInfo.getList());
    }

    /**
     * 更新课程安排信息
     *
     * @param courseId   课程ID
     * @param teachersId 要安排的教师ID列表
     */
    @Override
    public void update(Integer courseId, List<Integer> teachersId) {
        if (teachersId == null || teachersId.isEmpty()) {
            // 如果没有教师ID，删除该课程的所有安排记录，并更新课程的教师信息为0
            courseTeachersMapper.deleteAll(courseId);
            courseMapper.updateTeacher(0, courseId);
        } else {
            // 获取原始的教师ID列表
            List<Integer> originalTeachers = courseTeachersMapper.findByCourse(courseId);
            // 需要添加的教师ID列表
            List<Integer> teachersToAdd = new ArrayList<>(teachersId);
            teachersToAdd.removeAll(originalTeachers);
            // 需要删除的教师ID列表
            List<Integer> teachersToDelete = new ArrayList<>(originalTeachers);
            teachersToDelete.removeAll(teachersId);

            // 添加需要添加的教师ID到数据库
            for (Integer teacherId : teachersToAdd) {
                courseTeachersMapper.insert(courseId, teacherId);
            }
            // 删除需要删除的教师ID对应的记录
            for (Integer teacherId : teachersToDelete) {
                courseTeachersMapper.delete(courseId, teacherId);
            }
            // 更新课程的教师信息为1，表示已安排教师
            courseMapper.updateTeacher(1, courseId);
        }
    }
}
