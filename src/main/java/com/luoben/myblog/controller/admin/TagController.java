package com.luoben.myblog.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.luoben.myblog.pojo.Tag;
import com.luoben.myblog.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping("/admin")
public class TagController {

    @Autowired
    private TagService tagService;

    @GetMapping("/tags")
    public String list(Model model,@RequestParam(defaultValue = "1", value = "pageNum") Long pageNum,
                       @RequestParam(defaultValue = "5", value = "pageSize") Long pageSize){

        IPage<Tag> page = new Page<>(pageNum, pageSize);
        page = tagService.page(page);
        model.addAttribute("pageInfo", page);
        return "admin/tags";
    }

    @GetMapping("/tags/input")
    public String input(Model model) {
        model.addAttribute("tag", new Tag());
        return "admin/tags-input";
    }

    @GetMapping("/tags/{id}/input")
    public String editInput(@PathVariable Long id, Model model) {
        model.addAttribute("tag", tagService.getById(id));
        return "admin/tags-input";
    }


    /**
     * @Valid 表示校验前端传过来的参数
     * BindingResult 为后端校验的返回结果
     * @return
     */
    @PostMapping("/tags")
    public String addTag(@Valid Tag tag, BindingResult result, RedirectAttributes attributes){

        QueryWrapper<Tag> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", tag.getName());

        Tag nTag = tagService.getOne(queryWrapper);
        if (nTag != null) {
            result.rejectValue("name","nameError","不能添加重复的分类");
        }

        //后端校验出错
        if(result.hasErrors()){
            return "admin/tags-input";
        }

        boolean b = tagService.save(tag);
        if(b){
            attributes.addFlashAttribute("message","新增成功");
        }else {
            attributes.addFlashAttribute("message","新增失败");
        }
        return "redirect:/admin/tags";
    }


    //进行修改
    @PostMapping("/tags/{id}")
    public String editPost(@Valid Tag tag, BindingResult result, @PathVariable Long id, RedirectAttributes attributes) {

        QueryWrapper<Tag> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", tag.getName());

        Tag nTag = tagService.getOne(queryWrapper);
        if (nTag != null) {
            result.rejectValue("name","nameError","不能添加重复的分类");
        }

        //后端校验出错
        if(result.hasErrors()){
            return "admin/types-input";
        }

        System.out.println(tag);
        boolean b = tagService.updateById(tag);
        if(b){
            attributes.addFlashAttribute("message","更新成功");
        }else {
            attributes.addFlashAttribute("message","更新失败");
        }
        return "redirect:/admin/tags";
    }


    @GetMapping("/tags/{id}/delete")
    public String delete(@PathVariable Long id,RedirectAttributes attributes){
        boolean b = tagService.removeById(id);
        if(b){
            attributes.addFlashAttribute("message","删除成功");
        }else {
            attributes.addFlashAttribute("message","删除失败");
        }
        return "redirect:/admin/tags";
    }


}
