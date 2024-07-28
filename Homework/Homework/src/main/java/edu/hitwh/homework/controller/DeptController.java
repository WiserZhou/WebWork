package edu.hitwh.homework.controller;

import edu.hitwh.homework.entity.Dept;
import edu.hitwh.homework.entity.utils.Result;
import edu.hitwh.homework.service.DeptService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@CrossOrigin
@RequestMapping("/dept")
public class DeptController {

    @Autowired
    private DeptService deptService;

    // 获取部门列表
    @GetMapping
    public Result list() {
        List<Dept> depts = deptService.list();
        return Result.success(depts);
    }

    // 删除部门
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        // 记录日志
        log.info("根据id删除部门");
        // 调用service层功能
        deptService.delete(id);
        // 响应
        return Result.success();
    }

    // 新增部门
    @PostMapping
    public Result add(@RequestBody Dept dept) {
        log.info("新增部门:{}", dept);
        try {
            deptService.add(dept);
            return Result.success();
        } catch (RuntimeException e) {
            return Result.fail(dept.getName() + "已存在!");
        }
    }

    // 修改部门
    @PutMapping
    public Result update(@RequestBody Dept dept) {
        log.info("修改部门:{}", dept);
        try {
            deptService.update(dept);
            return Result.success();
        } catch (RuntimeException e) {
            return Result.fail(dept.getName() + "已存在!");
        }
    }
}
