package edu.hitwh.homework.controller;

import edu.hitwh.homework.entity.Course;
import edu.hitwh.homework.entity.utils.PageBean;
import edu.hitwh.homework.entity.utils.Result;
import edu.hitwh.homework.service.CourseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@CrossOrigin
@RequestMapping("/course")
public class CourseController {

    @Autowired
    private CourseService courseService;

    // 分页查询课程
    @GetMapping
    public Result page(@RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "10") Integer pageSize,
                       String name, Short attribute, String classRoom) {
        log.info("课程分页查询，参数:{},{},{},{},{}", page, pageSize, name, attribute, classRoom);
        PageBean pageBean = courseService.page(page, pageSize, name, attribute, classRoom);
        return Result.success(pageBean);
    }

    // 新增课程
    @PostMapping
    public Result save(@RequestBody Course course) {
        log.info("新增课程，course:{}", course);
        try {
            courseService.save(course);
            return Result.success();
        } catch (RuntimeException e) {
            return Result.fail("该课程代码已存在!");
        }
    }

    // 根据课程ID获取课程信息
    @GetMapping("/{id}")
    public Result getById(@PathVariable Integer id) {
        Course course = courseService.getById(id);
        return Result.success(course);
    }

    // 修改课程信息
    @PutMapping
    public Result update(@RequestBody Course course) {
        log.info("修改课程，:{}", course);
        try {
            courseService.update(course);
            return Result.success();
        } catch (RuntimeException e) {
            return Result.fail("该课程代码已存在!");
        }
    }

    // 删除课程
    @DeleteMapping("/{ids}")
    public Result delete(@PathVariable List<Integer> ids) {
        courseService.delete(ids);
        return Result.success();
    }
}
