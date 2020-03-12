package com.luoben.myblog.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.luoben.myblog.pojo.Type;
import com.luoben.myblog.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping("/admin")
public class TypeController {

    @Autowired
    private TypeService typeService;

    @GetMapping("/types")
    public ModelAndView list(@RequestParam(defaultValue = "1", value = "pageNum") Long pageNum,
                             @RequestParam(defaultValue = "5", value = "pageSize") Long pageSize) {
        IPage<Type> page = new Page<>(pageNum, pageSize);
        page = typeService.page(page);

        ModelAndView mv = new ModelAndView();
        mv.addObject("pageInfo", page);
        mv.setViewName("admin/types");
        return mv;
    }


    @GetMapping("/types/input")
    public String toAddType(Model model){
        //传给前端一个空对象做校验
        model.addAttribute("type",new Type());
        return "admin/types-input";
    }

    /**
     * @Valid 表示校验前端传过来的参数
     * BindingResult 为后端校验的返回结果
     * @return
     */
    @PostMapping("/types")
    public String addType(@Valid Type type, BindingResult result, RedirectAttributes attributes){

        QueryWrapper<Type> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", type.getName());

        Type nType = typeService.getOne(queryWrapper);
        if (nType != null) {
            result.rejectValue("name","nameError","不能添加重复的分类");
        }

        //后端校验出错
        if(result.hasErrors()){
            return "admin/types-input";
        }

        boolean b = typeService.save(type);
        if(b){
            attributes.addFlashAttribute("message","新增成功");
        }else {
             attributes.addFlashAttribute("message","新增失败");
        }
        return "redirect:/admin/types";
    }


    //到修改页面
    @GetMapping("/types/{id}/input")
    public String editInput(@PathVariable Long id, Model model) {
        System.out.println(id);
        Type type = typeService.getById(id);
        System.out.println(type);
        model.addAttribute("type", type);
        return "admin/types-input";
    }

    //进行修改
    @PostMapping("/types/{id}")
    public String editPost(@Valid Type type, BindingResult result,@PathVariable Long id,RedirectAttributes attributes) {

        QueryWrapper<Type> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", type.getName());

        Type nType = typeService.getOne(queryWrapper);
        if (nType != null) {
            result.rejectValue("name","nameError","不能添加重复的分类");
        }

        //后端校验出错
        if(result.hasErrors()){
            return "admin/types-input";
        }

        System.out.println(type);
        boolean b = typeService.updateById(type);
        if(b){
            attributes.addFlashAttribute("message","更新成功");
        }else {
            attributes.addFlashAttribute("message","更新失败");
        }
        return "redirect:/admin/types";
    }


    @GetMapping("/types/{id}/delete")
    public String delete(@PathVariable Long id,RedirectAttributes attributes){
        boolean b = typeService.removeById(id);
        if(b){
            attributes.addFlashAttribute("message","删除成功");
        }else {
            attributes.addFlashAttribute("message","删除失败");
        }
        return "redirect:/admin/types";
    }

}
