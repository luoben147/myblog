package com.luoben.myblog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.luoben.myblog.dao.UserDao;
import com.luoben.myblog.pojo.User;
import com.luoben.myblog.service.UserService;
import com.luoben.myblog.util.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserDao,User> implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    public User checkUser(String username, String password) {
        return userDao.queryByUsernameAndPassword(username,MD5Utils.code(password));
    }
}
