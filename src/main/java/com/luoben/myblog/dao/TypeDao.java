package com.luoben.myblog.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.luoben.myblog.pojo.Type;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TypeDao extends BaseMapper<Type> {

    List<Type> getAllTypeAndTotal();
}
