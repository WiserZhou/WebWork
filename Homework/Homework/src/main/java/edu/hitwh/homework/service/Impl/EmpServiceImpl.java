package edu.hitwh.homework.service.Impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import edu.hitwh.homework.mapper.EmpMapper;
import edu.hitwh.homework.entity.Employee;
import edu.hitwh.homework.entity.utils.PageBean;
import edu.hitwh.homework.service.EmpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class EmpServiceImpl implements EmpService {

    @Autowired
    private EmpMapper empMapper;

    /**
     * 分页查询员工信息
     *
     * @param page     页码
     * @param pageSize 每页展示记录数
     * @param name     姓名
     * @param gender   性别
     * @param begin    开始时间
     * @param end      结束时间
     * @return 分页结果
     */
    @Override
    public PageBean page(Integer page, Integer pageSize, String name, Short gender, LocalDate begin, LocalDate end) {
        PageHelper.startPage(page, pageSize);
        List<Employee> empList = empMapper.list(name, gender, begin, end);
        PageInfo<Employee> p = new PageInfo<Employee>(empList);
        PageBean pageBean = new PageBean(p.getTotal(), p.getList());
        return pageBean;
    }

    /**
     * 获取所有员工信息
     *
     * @return 员工列表
     */
    @Override
    public List<Employee> getAll() {
        return empMapper.getAll();
    }

    /**
     * 保存员工信息
     *
     * @param employee 待保存的员工信息
     */
    @Override
    public void save(Employee employee) {
        employee.setCreateTime(LocalDateTime.now());
        employee.setUpdateTime(LocalDateTime.now());
        try {
            if (employee.getImage() == null) employee.setImage("https://wiserx.oss-cn-beijing.aliyuncs.com/zyf/image.png");
            empMapper.insert(employee);
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new RuntimeException("用户名已存在", e);
        }
    }

    /**
     * 更新员工信息
     *
     * @param employee 待更新的员工信息
     */
    @Override
    public void update(Employee employee) {
        employee.setUpdateTime(LocalDateTime.now());
        empMapper.update(employee);
    }

    /**
     * 根据员工ID查询员工信息
     *
     * @param id 员工ID
     * @return 员工信息
     */
    @Override
    public Employee getById(Integer id) {
        return empMapper.findByID(id);
    }

    /**
     * 删除员工
     *
     * @param ids 待删除的员工ID列表
     */
    @Override
    public void delete(List<Integer> ids) {
        try {
            empMapper.delete(ids);
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new RuntimeException("存在教师仍有班主任任务，故而删除失败！", e);
        }
    }

    /**
     * 统计员工性别
     *
     * @return 性别统计结果
     */
    @Override
    public Map<String, Integer> genderCount() {
        Map<String, Integer> countMap = new HashMap<>();
        int male = empMapper.genderCount(1);
        int female = empMapper.genderCount(2);
        countMap.put("男性员工", male);
        countMap.put("女性员工", female);
        return countMap;
    }

    /**
     * 统计员工职位
     *
     * @return 职位统计结果
     */
    @Override
    public Map<String, Integer> jobCount() {
        Map<String, Integer> countMap = new HashMap<>();
        String[] jobNames = {"班主任", "讲师", "学工主管", "教研主管", "咨询师"};
        int[] jobIds = {1, 2, 3, 4, 5};
        for (int i = 0; i < jobNames.length; i++) {
            int count = empMapper.jobCount(jobIds[i]);
            countMap.put(jobNames[i], count);
        }
        return countMap;
    }

    /**
     * 用户登录验证
     *
     * @param employee 包含用户名和密码的员工对象
     * @return 登录成功返回员工信息，否则返回null
     */
    @Override
    public Employee login(Employee employee) {
        return empMapper.getByUsernameAndPassword(employee);
    }
}
