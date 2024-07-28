package edu.hitwh.homework.controller;

import edu.hitwh.homework.entity.Classes;
import edu.hitwh.homework.entity.utils.PageBean;
import edu.hitwh.homework.entity.utils.Result;
import edu.hitwh.homework.service.ClassService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@CrossOrigin
@RequestMapping("/class")
public class ClassController {

    @Autowired
    private ClassService classService;

    // 获取所有班级列表
    @GetMapping("/list")
    public Result getAll() {
        log.info("获取班级列表");
        List<Classes> classList = classService.getAll();
        return Result.success(classList);
    }

    // 根据班级ID获取班级信息
    @GetMapping("/{id}")
    public Result getById(@PathVariable Integer id) {
        log.info("获取对应班级ID的班级信息");
        Classes classes = classService.getById(id);
        return Result.success(classes);
    }

    // 新增班级
    @PostMapping
    public Result save(@RequestBody Classes classes) {
        log.info("新增班级：class:{}", classes);
        try {
            classService.save(classes);
            return Result.success();
        } catch (RuntimeException e) {
            return Result.fail("该班级名称已存在!");
        }
    }

    // 分页查询班级列表
    @GetMapping
    public Result page(@RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "10") Integer pageSize,
                       String name,
                       @DateTimeFormat(pattern = "YYYY-MM-DD") LocalDate begin,
                       @DateTimeFormat(pattern = "YYYY-MM-DD") LocalDate end) {
        PageBean pageBean = classService.page(page, pageSize, name, begin, end);
        return Result.success(pageBean);
    }

    // 修改班级信息
    @PutMapping
    public Result update(@RequestBody Classes classes) {
        log.info("修改班级，class:{}", classes);
        try {
            classService.update(classes);
            return Result.success();
        } catch (RuntimeException e) {
            return Result.fail("该班级名称已存在!");
        }
    }

    // 删除班级
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        try {
            classService.delete(id);
            return Result.success();
        } catch (RuntimeException e) {
            return Result.fail("该班级内仍然有学员，无法成功删除，请在调整完学员所属班级后再次删除！");
        }
    }
}
