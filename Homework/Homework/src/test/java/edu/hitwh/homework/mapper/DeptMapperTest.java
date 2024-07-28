package edu.hitwh.homework.mapper;

import edu.hitwh.homework.entity.Dept;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class DeptMapperTest {
    @Autowired
    private DeptMapper deptMapper;
    @Test
    void list(){
        List<Dept> depts = deptMapper.list();
        depts.stream().forEach(dept -> {
            System.out.println(dept);
        });
    }
}