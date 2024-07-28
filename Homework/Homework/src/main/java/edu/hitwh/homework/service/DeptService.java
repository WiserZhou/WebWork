package edu.hitwh.homework.service;

import edu.hitwh.homework.entity.Dept;

import java.util.List;

/**
 * 部门服务接口，定义了对部门信息进行查询、新增、修改和删除操作的方法
 */
public interface DeptService {

    /**
     * 查询部门列表
     *
     * @return 部门列表
     */
    List<Dept> list();

    /**
     * 删除部门
     *
     * @param id 要删除的部门ID
     */
    void delete(Integer id);

    /**
     * 新增部门
     *
     * @param dept 要新增的部门对象
     */
    void add(Dept dept);

    /**
     * 修改部门
     *
     * @param dept 要修改的部门对象
     */
    void update(Dept dept);
}
