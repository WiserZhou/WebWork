package edu.hitwh.homework.controller;

import edu.hitwh.homework.entity.utils.PageBean;
import edu.hitwh.homework.entity.utils.Result;
import edu.hitwh.homework.service.CourseTeachersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@CrossOrigin
@RequestMapping("/courseTeachers")
public class CourseTeachersController {

    @Autowired
    private CourseTeachersService courseTeachersService;

    // 分页查询课程安排
    @GetMapping
    public Result page(@RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "10") Integer pageSize,
                       String name, Short arranged, Short teacher) {
        log.info("课程安排分页查询，参数:{},{},{},{},{}", page, pageSize, name, arranged, teacher);
        PageBean pageBean = courseTeachersService.page(page, pageSize, name, arranged, teacher);
        return Result.success(pageBean);
    }

    // 更新课程安排
    @PostMapping
    public Result update(@RequestBody Map<String, Object> request) {
        Integer courseId = Integer.parseInt(request.get("courseId").toString());
        List<Integer> teachersId = (List<Integer>) request.get("teachersId");
        log.info("更新参数:{},{}", courseId, teachersId);
        courseTeachersService.update(courseId, teachersId);
        return Result.success();
    }
}
