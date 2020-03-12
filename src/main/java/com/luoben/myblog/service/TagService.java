package com.luoben.myblog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.luoben.myblog.pojo.Tag;

import java.util.List;

public interface TagService extends IService<Tag> {

    List<Tag> getTagsByBlogId(Long blogId);

    List<Tag> getAllTagAndTotal();
}
