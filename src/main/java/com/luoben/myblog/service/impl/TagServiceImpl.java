package com.luoben.myblog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.luoben.myblog.dao.TagDao;
import com.luoben.myblog.pojo.Tag;
import com.luoben.myblog.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagServiceImpl extends ServiceImpl<TagDao,Tag> implements TagService {

    @Autowired
    private TagDao tagDao;

    @Override
    public List<Tag> getTagsByBlogId(Long blogId) {
        return tagDao.getTagsByBlogId(blogId);
    }

    @Override
    public List<Tag> getAllTagAndTotal() {
        return tagDao.getAllTagAndTotal();
    }

}
