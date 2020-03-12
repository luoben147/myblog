package com.luoben.myblog.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.luoben.myblog.pojo.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao extends BaseMapper<User> {
    User queryByUsernameAndPassword(@Param("username") String username, @Param("password") String password);
}
