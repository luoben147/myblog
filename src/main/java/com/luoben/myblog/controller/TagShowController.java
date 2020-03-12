package com.luoben.myblog.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.luoben.myblog.pojo.Blog;
import com.luoben.myblog.pojo.Tag;
import com.luoben.myblog.service.BlogService;
import com.luoben.myblog.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class TagShowController {

    @Autowired
    private TagService tagService;

    @Autowired
    private BlogService blogService;

    @GetMapping("/tags/{id}")
    public String tags(@RequestParam(defaultValue = "1", value = "pageNum") Long pageNum,
                        @RequestParam(defaultValue = "5", value = "pageSize") Long pageSize,
                        Model model, @PathVariable Long id){

        List<Tag> list = tagService.getAllTagAndTotal();
        if(id==-1){
            id=list.get(0).getId();
        }

        IPage<Blog> page = new Page<>(pageNum, pageSize);
        page = blogService.listBlogByTagId(page, id);


        model.addAttribute("tags",list);
        model.addAttribute("page",page);
        model.addAttribute("activeTagId",id);
        return "tags";
    }

}
