package edu.hitwh.homework.service;

import edu.hitwh.homework.entity.utils.PageBean;
import edu.hitwh.homework.entity.Student;
import edu.hitwh.homework.entity.Score;

import java.util.List;
import java.util.Map;

/**
 * 学员管理服务接口，定义了对学员信息进行查询、新增、修改、删除、违纪扣分等操作的方法
 */
public interface StudentService {

    /**
     * 分页查询学员信息
     *
     * @param page      页码
     * @param pageSize  每页展示记录数
     * @param name      姓名
     * @param stuId     学号
     * @param education 学历
     * @param classId   班级ID
     * @return 分页后的学员信息列表
     */
    PageBean page(Integer page, Integer pageSize, String name, String stuId, Short education, Short classId);

    /**
     * 保存学员信息
     *
     * @param student 要保存的学员对象
     */
    void save(Student student);

    /**
     * 修改学员信息
     *
     * @param student 要修改的学员对象
     */
    void update(Student student);

    /**
     * 根据ID查询学员信息
     *
     * @param id 学员ID
     * @return 对应ID的学员对象
     */
    Student getById(Integer id);

    /**
     * 删除学员信息
     *
     * @param ids 要删除的学员ID列表
     */
    void delete(List<Integer> ids);

    /**
     * 违纪扣分操作
     *
     * @param studentPoints 包含学员ID和扣分数的对象
     */
    void points(Score studentPoints);

    /**
     * 统计学员性别
     *
     * @return 包含学员性别统计信息的Map，键为性别，值为对应性别的学员数量
     */
    Map<String, Integer> genderCount();

    /**
     * 统计学员教育情况
     *
     * @return 包含学员教育情况统计信息的Map，键为教育程度，值为对应教育程度的学员数量
     */
    Map<String, Integer> educationCount();
}
