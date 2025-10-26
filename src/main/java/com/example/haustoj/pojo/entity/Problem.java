package com.example.haustoj.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @FileName Problem
 * @Description
 * @Author ouyu
 * @Date 2025-10-24
 **/
@Data
@TableName("problem")
public class Problem {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String title;
    private String description;
    private Date createTime;
    // 0: 正常 1: 隐藏 2: 删除
    private Integer status;

}
