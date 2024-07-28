package edu.hitwh.homework.controller;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.PutObjectRequest;
import edu.hitwh.homework.entity.Employee;
import edu.hitwh.homework.entity.utils.PageBean;
import edu.hitwh.homework.entity.utils.Result;
import edu.hitwh.homework.service.EmpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

/**
 * 员工管理Controller
 */
@Slf4j
@RestController
@CrossOrigin
@RequestMapping("/emp")
public class EmpController {

    @Autowired
    private EmpService empService;


    // 分页查询员工列表
    @GetMapping
    public Result page(@RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "10") Integer pageSize,
                       String name, Short gender,
                       @DateTimeFormat(pattern = "YYYY-MM-DD") LocalDate begin,
                       @DateTimeFormat(pattern = "YYYY-MM-DD") LocalDate end) {
        log.info("分页查询，参数:{},{},{},{},{},{}", page, pageSize, name, gender, begin, end);
        PageBean pageBean = empService.page(page, pageSize, name, gender, begin, end);
        return Result.success(pageBean);
    }

    // 获取所有员工列表
    @GetMapping("/list")
    public Result getAll() {
        log.info("获取所有的员工列表");
        List<Employee> allEmp = empService.getAll();
        return Result.success(allEmp);
    }

    // 新增员工
    @PostMapping
    public Result save(@RequestBody Employee employee) {
        log.info("新增员工，employee:{}", employee);
        try {
            empService.save(employee);
            return Result.success();
        } catch (RuntimeException e) {
            return Result.fail("该用户名已存在!");
        }
    }

    // 根据员工ID获取员工信息
    @GetMapping("/{id}")
    public Result getById(@PathVariable Integer id) {
        Employee employee = empService.getById(id);
        return Result.success(employee);
    }

    // 修改员工信息
    @PutMapping
    public Result update(@RequestBody Employee employee) {
        log.info("修改员工，employee:{}", employee);
        empService.update(employee);
        return Result.success();
    }

    // 删除员工
    @DeleteMapping("/{ids}")
    public Result delete(@PathVariable List<Integer> ids) {
        try {
            empService.delete(ids);
            return Result.success();
        } catch (RuntimeException e) {
            return Result.fail("该教师或存在教师仍有班主任任务，故而删除失败！请调整后再次删除！");
        }
    }

    // 上传员工头像
    @PostMapping("/upload")
    public Result upload(MultipartFile image) {

        // 指定 .env 文件的路径
        String envFilePath = "C:\\Users\\21029\\Desktop\\Homework\\Homework\\src\\main\\resources\\.env";

        // 创建 Properties 对象
        Properties props = new Properties();
        String accessKeyId = "";
        String accessKeySecret = "";
        try {
            // 从文件中加载属性列表
            props.load(new FileInputStream(envFilePath));

            // 获取 key 对应的值
//            String value = props.getProperty("YOUR_KEY");
            accessKeyId = props.getProperty("ALIYUN_ACCESS_KEY_ID");
            accessKeySecret = props.getProperty("ALIYUN_ACCESS_KEY_SECRET");
            // 打印值
//            System.out.println("Value for YOUR_KEY: " + value);
        } catch (IOException e) {
            System.err.println("Error reading .env file: " + e.getMessage());
        }
        final String endpoint = "oss-cn-beijing.aliyuncs.com";

        final String bucketName = "wiserx";
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        try {
            String originalFilename = image.getOriginalFilename();
            String extname = originalFilename.substring(originalFilename.lastIndexOf("."));
            String newFileName = "zyf/" + UUID.randomUUID().toString() + extname;

            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, newFileName, image.getInputStream());

            ossClient.putObject(putObjectRequest);

            String imageUrl = "https://" + bucketName + "." + endpoint + "/" + newFileName;

            return Result.success(imageUrl);
        } catch (IOException e) {
            e.printStackTrace();
            return Result.fail("文件上传失败：" + e.getMessage());
        } finally {
            ossClient.shutdown();
        }
    }

    // 统计员工性别数量
    @GetMapping("/genderCount")
    public Result genderCount() {
        log.info("员工性别数量统计");
        Map<String, Integer> countMap = empService.genderCount();
        return Result.success(countMap);
    }

    // 统计员工职位数量
    @GetMapping("/jobCount")
    public Result jobCount() {
        log.info("员工职位统计");
        Map<String, Integer> countMap = empService.jobCount();
        return Result.success(countMap);
    }
}
