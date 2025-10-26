package com.example.haustoj.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.haustoj.pojo.entity.Submission;
import org.springframework.stereotype.Component;

@Component
public interface ContestDao extends BaseMapper<Submission> {
}
