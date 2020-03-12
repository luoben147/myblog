package com.luoben.myblog.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.luoben.myblog.pojo.Comment;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentDao extends BaseMapper<Comment> {

    @Select("select * from t_comment where blog_id=#{blogId} order by create_time asc")
    List<Comment> findByBlogId(@Param("blogId") Long blogId);
}
