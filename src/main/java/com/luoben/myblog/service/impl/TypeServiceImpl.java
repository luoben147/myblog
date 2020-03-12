package com.luoben.myblog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.luoben.myblog.dao.TypeDao;
import com.luoben.myblog.pojo.Type;
import com.luoben.myblog.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TypeServiceImpl extends ServiceImpl<TypeDao,Type> implements TypeService {

    @Autowired
    private TypeDao typeDao;


    @Override
    public List<Type> getAllTypeAndTotal() {
        return typeDao.getAllTypeAndTotal();
    }
}
