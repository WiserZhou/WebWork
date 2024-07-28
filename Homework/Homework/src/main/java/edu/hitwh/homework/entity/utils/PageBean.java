package edu.hitwh.homework.entity.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageBean<T> {
    private Long total; // 总记录数
    private List<T> rows; // 当前页数据列表
}
