package com.luoben.myblog.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.luoben.myblog.dao.BlogDao;
import com.luoben.myblog.dao.TagDao;
import com.luoben.myblog.pojo.Blog;
import com.luoben.myblog.pojo.BlogAndTag;
import com.luoben.myblog.pojo.Tag;
import com.luoben.myblog.service.BlogService;
import com.luoben.myblog.util.MarkdownUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class BlogServiceImpl extends ServiceImpl<BlogDao,Blog> implements BlogService {

    @Autowired
    private BlogDao blogDao;

    @Autowired
    private TagDao tagDao;

    @Override
    public IPage<Blog> searchBlog(IPage<Blog> page, Wrapper<Blog> queryWrapper) {
        return blogDao.searchBlog(page,queryWrapper);
    }

    @Override
    public Blog getBlogById(Long id) {
        return blogDao.getBlogById(id);
    }

    @Transactional
    @Override
    public Blog getBlogAndConvertById(Long id) {
        Blog blog = blogDao.getBlogById(id);
       if(blog!=null){
           blog.setId(id);
           String content = blog.getContent();
           blog.setContent(MarkdownUtils.markdownToHtmlExtensions(content));
           blogDao.updateViews(id);
       }
        return blog;
    }

    @Transactional
    @Override
    public Boolean saveBlog(Blog blog) {
        blog.setCreateTime(new Date());
        blog.setUpdateTime(new Date());
        blog.setViews(0);
        Boolean result=blogDao.saveBlog(blog);
        if(blog.getTags().size()>0){
            blogDao.saveBlogAndTag(getBlogTag(blog));
        }
        return result;
    }

    @Transactional
    @Override
    public Boolean updateBlog(Blog blog) {
        blog.setUpdateTime(new Date());
        //删除原来的tag与blog的对应关系
        tagDao.deleteBlogAndTag(blog.getId());
        if(blog.getTags().size()>0){
            //保存新的对应关系
            blogDao.saveBlogAndTag(getBlogTag(blog));
        }
        //保存blog数据
        return blogDao.updateById(blog)==1;
    }

    @Override
    public Boolean deleteBlog(Long id) {
        tagDao.deleteBlogAndTag(id);
        return blogDao.deleteById(id)==1;
    }

    @Override
    public List<Blog> listRecommendBlogTop(Integer size) {
        return blogDao.listRecommendBlogTop(size);
    }

    @Override
    public IPage<Blog> listBlog(IPage<Blog> blogs) {
        return blogDao.listBlog(blogs);
    }

    @Override
    public IPage<Blog> listBlogByQuery(IPage<Blog> blogs, String query) {
        return blogDao.listBlogByQuery(blogs,query);
    }

    @Override
    public IPage<Blog> listBlogByTagId(IPage<Blog> blogs, Long tagId) {
        return blogDao.listBlogByTagId(blogs,tagId);
    }

    @Override
    public Map<String, List<Blog>> archiveBlog() {
        List<String> years=blogDao.findGroupYear();
        Map<String,List<Blog>> map=new LinkedHashMap<>();
        for(String year:years){
            map.put(year,blogDao.findBlogByYear(year));
        }
        return map;
    }



    private List<BlogAndTag> getBlogTag(Blog blog){
        List<BlogAndTag> blogAndTags=new ArrayList<>();
        for (Tag item:blog.getTags()){
            BlogAndTag blogAndTag=new BlogAndTag();
            blogAndTag.setBlogId(blog.getId());
            blogAndTag.setTagId(item.getId());
            blogAndTags.add(blogAndTag);
        }
        return blogAndTags;
    }

}
