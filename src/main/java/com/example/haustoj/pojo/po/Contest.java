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
@TableName("contest")
public class Contest {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String title;
    private String rule;
    private String description;
    private Date startTime;
    private Date endTime;
    private Integer status;


}
