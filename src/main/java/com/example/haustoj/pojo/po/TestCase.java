package com.example.haustoj.pojo.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @Author: ouyu69
 * @Date: 2025-10-27 18:39
 * @Description:
 */
@Data
@TableName("test_case")
public class TestCase {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long problemId;
    private String inputFilepath;
    private String outputFilepath;
    private Integer status;
}
