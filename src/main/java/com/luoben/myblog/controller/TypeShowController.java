package com.luoben.myblog.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.luoben.myblog.pojo.Blog;
import com.luoben.myblog.pojo.Type;
import com.luoben.myblog.service.BlogService;
import com.luoben.myblog.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class TypeShowController {

    @Autowired
    private TypeService typeService;

    @Autowired
    private BlogService blogService;

    @GetMapping("/types/{id}")
    public String types(@RequestParam(defaultValue = "1", value = "pageNum") Long pageNum,
                        @RequestParam(defaultValue = "5", value = "pageSize") Long pageSize,
                        Model model, @PathVariable Long id){

        List<Type> list = typeService.getAllTypeAndTotal();
        if(id==-1){
            id=list.get(0).getId();
        }

        IPage<Blog> page = new Page<>(pageNum, pageSize);
        //Mybatis构造查询条件
        QueryWrapper<Blog> wrapper = new QueryWrapper<>();
        wrapper.eq( "type_id", id);
        IPage<Blog> blogs = blogService.searchBlog(page, wrapper);

        model.addAttribute("types",list);
        model.addAttribute("page",blogs);
        model.addAttribute("activeTypeId",id);
        return "types";
    }

}
