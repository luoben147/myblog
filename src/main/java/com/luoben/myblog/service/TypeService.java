package com.luoben.myblog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.luoben.myblog.pojo.Type;

import java.util.List;

public interface TypeService extends IService<Type> {

    List<Type> getAllTypeAndTotal();
}
