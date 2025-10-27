package com.example.haustoj.pojo.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @Author: ouyu69
 * @Date: 2025-10-27 18:17
 * @Description:
 */
@Data
@TableName("problem")
public class Problem {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String title;
    private String description;
    // 0: 正常 1: 禁用/隐藏 2: 删除
    private Integer status;
    private Date createTime;
    private Date updateTime;

}
