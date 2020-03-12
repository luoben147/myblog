package com.luoben.myblog.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.luoben.myblog.pojo.*;
import com.luoben.myblog.service.BlogService;
import com.luoben.myblog.service.TagService;
import com.luoben.myblog.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class BlogController {

    private static final String INPUT = "admin/blogs-input";
    private static final String LIST = "admin/blogs";
    private static final String REDIRECT_LIST = "redirect:/admin/blogs";

    @Autowired
    private BlogService blogService;

    @Autowired
    private TypeService typeService;
    @Autowired
    private TagService tagService;


    @GetMapping("/blogs")
    public String list(BlogQuery blogQuery, @RequestParam(defaultValue = "1", value = "pageNum") Long pageNum,
                       @RequestParam(defaultValue = "5", value = "pageSize") Long pageSize,
                       Model model) {
        //获取分类
        List<Type> types = typeService.list();
        model.addAttribute("types", types);

        //分页条件
        IPage<Blog> page = new Page<>(pageNum, pageSize);
        //Mybatis构造查询条件
        QueryWrapper<Blog> wrapper = new QueryWrapper<>();
        wrapper.like(!StringUtils.isEmpty(blogQuery.getTitle()), "title", blogQuery.getTitle())
                .eq(blogQuery.getTypeId() != null, "type_id", blogQuery.getTypeId())
                .eq(blogQuery.getRecommend() != null, "recommend", blogQuery.getRecommend())
                .orderByDesc("update_time");

        IPage<Blog> list = blogService.searchBlog(page, wrapper);

        model.addAttribute("pageInfo", list);
        return LIST;
    }

    @PostMapping("/blogs/search")
    public String search(BlogQuery blogQuery, @RequestParam(defaultValue = "1", value = "pageNum") Long pageNum,
                         @RequestParam(defaultValue = "5", value = "pageSize") Long pageSize,
                         Model model) {

        //分页条件
        IPage<Blog> page = new Page<>(pageNum, pageSize);
        //Mybatis构造查询条件
        QueryWrapper<Blog> wrapper = new QueryWrapper<>();
        wrapper.like(!StringUtils.isEmpty(blogQuery.getTitle()), "title", blogQuery.getTitle())
                .eq(blogQuery.getTypeId() != null, "type_id", blogQuery.getTypeId())
                .eq(blogQuery.getRecommend() != null, "recommend", blogQuery.getRecommend())
                .orderByDesc("update_time");

        IPage<Blog> list = blogService.searchBlog(page, wrapper);

        model.addAttribute("pageInfo", list);
        return "admin/blogs::blogList";
    }


    @GetMapping("/blogs/input")
    public String input(Model model) {
        setTypeAndTag(model);
        model.addAttribute("blog", new Blog());
        return INPUT;
    }

    //初始化标签，分类
    private void setTypeAndTag(Model model) {
        model.addAttribute("types", typeService.list());
        model.addAttribute("tags", tagService.list());
    }


    @PostMapping("/blogs")
    public String post(Blog blog, RedirectAttributes attributes, HttpSession session) {
        blog.setUser((User) session.getAttribute("user"));
        blog.setType(typeService.getById(blog.getTypeId()));
        //给blog中的List<Tag>赋值
        List<Tag> tags = (List<Tag>) tagService.listByIds(Arrays.asList(blog.getTagIds().split(",")));
        blog.setTags(tags);

        //设置用户id
        blog.setUserId(blog.getUser().getId());
        Boolean b;
        if (blog.getId() == null) {
            b = blogService.saveBlog(blog);
            if (b) {
                attributes.addFlashAttribute("message", "新增成功");
            } else {
                attributes.addFlashAttribute("message", "新增失败");
            }
        } else {
            b = blogService.updateBlog(blog);
            if (b) {
                attributes.addFlashAttribute("message", "修改成功");
            } else {
                attributes.addFlashAttribute("message", "修改失败");
            }
        }

        return REDIRECT_LIST;
    }


    @GetMapping("/blogs/{id}/input")
    public String editInput(@PathVariable Long id, Model model) {
        setTypeAndTag(model);
        Blog blog = blogService.getById(id);

        String tagIds = tagService.getTagsByBlogId(id).stream().map(tag -> tag.getId().toString()).collect(Collectors.joining(","));
        blog.setTagIds(tagIds);
        model.addAttribute("blog", blog);
        return INPUT;
    }

    @GetMapping("/blogs/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes attributes) {
        Boolean b = blogService.deleteBlog(id);
        if (b) {
            attributes.addFlashAttribute("message", "删除成功");
        } else {
            attributes.addFlashAttribute("message", "删除失败");
        }
        return "redirect:/admin/blogs";
    }

}
