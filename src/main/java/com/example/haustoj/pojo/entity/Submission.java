package com.example.haustoj.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @FileName Submission
 * @Description
 * @Author ouyu
 * @Date 2025-10-24
 **/
@Data
@TableName("submission")
public class Submission {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long problemId;
    private Long userId;
    private Long contestId;
    private String language;

    private String code;
    private Integer status;
    private Data createTime;
}
