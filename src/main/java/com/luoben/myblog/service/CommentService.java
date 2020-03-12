package com.luoben.myblog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.luoben.myblog.pojo.Comment;

import java.util.List;

public interface CommentService extends IService<Comment> {

    List<Comment> findByBlogIdParentId(Long blogId);

}
