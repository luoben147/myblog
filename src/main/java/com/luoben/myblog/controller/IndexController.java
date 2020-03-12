package com.luoben.myblog.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.luoben.myblog.pojo.Blog;
import com.luoben.myblog.pojo.Tag;
import com.luoben.myblog.pojo.Type;
import com.luoben.myblog.service.BlogService;
import com.luoben.myblog.service.TagService;
import com.luoben.myblog.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class IndexController {

    @Autowired
    private BlogService blogService;

    @Autowired
    private TypeService typeService;

    @Autowired
    private TagService tagService;

    @GetMapping("/")
    public String index(@RequestParam(defaultValue = "1", value = "pageNum") Long pageNum,
                        @RequestParam(defaultValue = "5", value = "pageSize") Long pageSize,
                        Model model){

        IPage<Blog> blogs = new Page<>(pageNum, pageSize);
        blogs = blogService.listBlog(blogs);
        List<Type> types = typeService.getAllTypeAndTotal();
        List<Tag> tags = tagService.getAllTagAndTotal();
        List<Blog> recommendBlogs= blogService.listRecommendBlogTop(8);
        model.addAttribute("page",blogs);
        model.addAttribute("types",types);
        model.addAttribute("tags",tags);
        model.addAttribute("recommendBlogs",recommendBlogs);
        return "index";
    }

    @PostMapping("/search")
    public String search(@RequestParam(defaultValue = "1", value = "pageNum") Long pageNum,
                         @RequestParam(defaultValue = "5", value = "pageSize") Long pageSize,
                         @RequestParam String query,
                         Model model){
        IPage<Blog> blogs = new Page<>(pageNum, pageSize);
        blogs = blogService.listBlogByQuery(blogs,query);
        model.addAttribute("page", blogs);
        model.addAttribute("query", query);
        return "search";
    }


    @GetMapping("/blog/{id}")
    public String blog(@PathVariable Long id ,Model model) {
        Blog blog = blogService.getBlogAndConvertById(id);
        model.addAttribute("blog", blog);
        return "blog";
    }

}
