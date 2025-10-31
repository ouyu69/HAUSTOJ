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
@TableName("submission")
public class Submission {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long problemId;
    private String uid;
    private Long contestId;
    private String language;
    private String code;
    private Integer status;
    private Data createTime;
}
