package com.luoben.myblog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.luoben.myblog.pojo.User;

public interface UserService extends IService<User> {

    User checkUser(String username, String password);
}
