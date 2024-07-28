package edu.hitwh.homework.service;

import edu.hitwh.homework.entity.Employee;
import edu.hitwh.homework.entity.utils.PageBean;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * 员工管理服务接口，定义了对员工信息进行查询、新增、修改、删除等操作的方法
 */
public interface EmpService {

    /**
     * 条件分页查询员工信息
     *
     * @param page     页码
     * @param pageSize 每页展示记录数
     * @param name     姓名
     * @param gender   性别
     * @param begin    开始时间
     * @param end      结束时间
     * @return 分页后的员工信息列表
     */
    PageBean page(Integer page, Integer pageSize, String name, Short gender, LocalDate begin, LocalDate end);

    /**
     * 获取所有员工信息
     *
     * @return 所有员工信息列表
     */
    List<Employee> getAll();

    /**
     * 保存员工信息
     *
     * @param employee 要保存的员工对象
     */
    void save(Employee employee);

    /**
     * 修改员工信息
     *
     * @param employee 要修改的员工对象
     */
    void update(Employee employee);

    /**
     * 根据ID查询员工信息
     *
     * @param id 员工ID
     * @return 对应ID的员工对象
     */
    Employee getById(Integer id);

    /**
     * 删除员工信息
     *
     * @param ids 要删除的员工ID列表
     */
    void delete(List<Integer> ids);

    /**
     * 统计员工性别
     *
     * @return 包含员工性别统计信息的Map，键为性别，值为对应性别的员工数量
     */
    Map<String, Integer> genderCount();

    /**
     * 统计员工职位
     *
     * @return 包含员工职位统计信息的Map，键为职位名称，值为对应职位的员工数量
     */
    Map<String, Integer> jobCount();

    /**
     * 用户登录
     *
     * @param employee 包含用户名和密码的员工对象
     * @return 登录成功的员工对象，若登录失败返回null
     */
    Employee login(Employee employee);
}
