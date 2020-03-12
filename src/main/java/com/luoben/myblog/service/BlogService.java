package com.luoben.myblog.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.service.IService;
import com.luoben.myblog.pojo.Blog;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface BlogService extends IService<Blog> {

    IPage<Blog> searchBlog(IPage<Blog> page, @Param(Constants.WRAPPER) Wrapper<Blog> queryWrapper);

    Blog getBlogById(Long id);

    Blog getBlogAndConvertById(Long id);

    Boolean saveBlog(Blog blog);

    Boolean updateBlog(Blog blog);

    Boolean deleteBlog(Long id);

    List<Blog> listRecommendBlogTop(Integer size);

    IPage<Blog> listBlog(IPage<Blog> blogs);

    IPage<Blog> listBlogByQuery(IPage<Blog> blogs, String query);

    IPage<Blog> listBlogByTagId(IPage<Blog> blogs,Long tagId);

    Map<String,List<Blog>> archiveBlog();


}
