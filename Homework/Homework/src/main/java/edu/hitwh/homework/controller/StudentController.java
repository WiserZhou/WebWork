package edu.hitwh.homework.controller;

import edu.hitwh.homework.entity.*;
import edu.hitwh.homework.entity.utils.PageBean;
import edu.hitwh.homework.entity.utils.Result;
import edu.hitwh.homework.service.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@CrossOrigin
@RequestMapping("/student")
public class StudentController {
    @Autowired
    private StudentService studentService;

    /**
     * 分页查询学生列表
     *
     * @param page      页码
     * @param pageSize  每页大小
     * @param name      学生姓名
     * @param stuId     学号
     * @param education 教育程度
     * @param classId   班级ID
     * @return 分页结果
     */
    @GetMapping
    public Result page(@RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "10") Integer pageSize,
                       String name, String stuId, Short education, Short classId) {
        log.info("学生分页查询，参数:{},{},{},{},{},{}", page, pageSize, name, stuId, education, classId);
        PageBean pageBean = studentService.page(page, pageSize, name, stuId, education, classId);
        return Result.success(pageBean);
    }

    /**
     * 新增学员
     *
     * @param student 学生对象
     * @return 操作结果
     */
    @PostMapping
    public Result save(@RequestBody Student student) {
        log.info("新增学员，student:{}", student);
        try {
            studentService.save(student);
            return Result.success();
        } catch (RuntimeException e) {
            return Result.fail("该学号已存在!");
        }
    }

    /**
     * 根据ID获取学员信息
     *
     * @param id 学员ID
     * @return 学员信息
     */
    @GetMapping("/{id}")
    public Result getById(@PathVariable Integer id) {
        Student student = studentService.getById(id);
        return Result.success(student);
    }

    /**
     * 修改学员信息
     *
     * @param student 学员对象
     * @return 操作结果
     */
    @PutMapping
    public Result update(@RequestBody Student student) {
        log.info("修改学员，:{}", student);
        try {
            studentService.update(student);
            return Result.success();
        } catch (RuntimeException e) {
            return Result.fail("该学号已存在!");
        }

    }

    /**
     * 删除学员
     *
     * @param ids 学员ID列表
     * @return 操作结果
     */
    @DeleteMapping("/{ids}")
    public Result delete(@PathVariable List<Integer> ids) {
        studentService.delete(ids);
        return Result.success();
    }

    /**
     * 更新学员违纪记录
     *
     * @param studentPoints 学员违纪记录对象
     * @return 操作结果
     */
    @PutMapping("/points")
    public Result indiscipline(@RequestBody Score studentPoints) {
        studentService.points(studentPoints);
        return Result.success();
    }

    /**
     * 统计学员性别数量
     *
     * @return 性别统计结果
     */
    @GetMapping("/genderCount")
    public Result genderCount() {
        log.info("学员性别数量统计");
        Map<String, Integer> countMap = studentService.genderCount();
        return Result.success(countMap);
    }

    /**
     * 统计学员教育程度数量
     *
     * @return 教育程度统计结果
     */
    @GetMapping("/educationCount")
    public Result jobCount() {
        log.info("学员教育情况统计");
        Map<String, Integer> countMap = studentService.educationCount();
        return Result.success(countMap);
    }
}
