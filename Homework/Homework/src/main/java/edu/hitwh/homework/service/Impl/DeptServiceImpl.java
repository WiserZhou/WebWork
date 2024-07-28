package edu.hitwh.homework.service.Impl;

import edu.hitwh.homework.mapper.DeptMapper;
import edu.hitwh.homework.entity.Dept;
import edu.hitwh.homework.service.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class DeptServiceImpl implements DeptService {

    @Autowired
    private DeptMapper deptMapper;

    /**
     * 查询部门列表
     *
     * @return 部门列表
     */
    @Override
    public List<Dept> list() {
        return deptMapper.list();
    }

    /**
     * 删除部门
     *
     * @param id 部门ID
     */
    @Override
    public void delete(Integer id) {
        deptMapper.deleteById(id);
    }

    /**
     * 新增部门
     *
     * @param dept 待新增的部门信息
     */
    @Override
    public void add(Dept dept) {
        // 补全数据
        dept.setCreateTime(LocalDateTime.now());
        dept.setUpdateTime(LocalDateTime.now());
        // 调用持久层增加功能
        try {
            deptMapper.add(dept);
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new RuntimeException("部门名称不能重复", e);
        }
    }

    /**
     * 修改部门信息
     *
     * @param dept 待修改的部门信息
     */
    @Override
    public void update(Dept dept) {
        dept.setUpdateTime(LocalDateTime.now());
        try {
            deptMapper.update(dept);
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new RuntimeException("部门名称不能重复", e);
        }
    }
}
