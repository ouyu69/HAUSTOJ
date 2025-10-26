package com.example.haustoj.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @FileName TestCase
 * @Description
 * @Author ouyu
 * @Date 2025-10-24
 **/
@Data
@TableName("test_case")
public class TestCase {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long problemId;
    private String inputFilepath;
    private String outputFilepath;
}
