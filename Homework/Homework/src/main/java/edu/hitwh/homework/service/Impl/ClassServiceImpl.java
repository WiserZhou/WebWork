package edu.hitwh.homework.service.Impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import edu.hitwh.homework.mapper.ClassMapper;
import edu.hitwh.homework.entity.Classes;
import edu.hitwh.homework.entity.utils.PageBean;
import edu.hitwh.homework.service.ClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDate;
import java.util.List;

/**
 * 班级管理服务实现类，实现了对班级信息的增删改查等操作
 */
@Service
public class ClassServiceImpl implements ClassService {

    @Autowired
    private ClassMapper classMapper;

    /**
     * 分页查询班级信息
     *
     * @param page     页码
     * @param pageSize 每页记录数
     * @param name     班级名称
     * @param begin    开始时间
     * @param end      结束时间
     * @return 分页后的班级信息
     */
    @Override
    public PageBean page(Integer page, Integer pageSize, String name, LocalDate begin, LocalDate end) {
        PageHelper.startPage(page, pageSize);
        List<Classes> classList = classMapper.list(name, begin, end);
        PageInfo<Classes> pageInfo = new PageInfo<>(classList);
        return new PageBean(pageInfo.getTotal(), pageInfo.getList());
    }

    /**
     * 获取所有班级信息
     *
     * @return 所有班级信息列表
     */
    @Override
    public List<Classes> getAll() {
        return classMapper.getAll();
    }

    /**
     * 根据ID查询班级信息
     *
     * @param id 班级ID
     * @return 对应ID的班级信息
     */
    @Override
    public Classes getById(Integer id) {
        return classMapper.findByID(id);
    }

    /**
     * 保存班级信息
     *
     * @param classes 要保存的班级对象
     */
    @Override
    public void save(Classes classes) {
        try {
            classMapper.insert(classes);
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new RuntimeException("该班级名称已存在", e);
        }
    }

    /**
     * 修改班级信息
     *
     * @param classes 要修改的班级对象
     */
    @Override
    public void update(Classes classes) {
        try {
            classMapper.update(classes);
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new RuntimeException("该班级名称已存在", e);
        }
    }

    /**
     * 删除班级信息
     *
     * @param id 班级ID
     */
    @Override
    public void delete(Integer id) {
        try {
            classMapper.delete(id);
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new RuntimeException("仍有学员归属该班级，无法直接删除，请在调整完学员所属班级后再次删除！", e);
        }
    }
}
