package com.luoben.myblog.controller;

import com.luoben.myblog.pojo.Comment;
import com.luoben.myblog.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Date;

@Controller
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Value("${comment.avatar}")
    private String avatar;

    @GetMapping("/comments/{blogId}")
    public String comment(@PathVariable Long blogId, Model model){

        model.addAttribute("comments",commentService.findByBlogIdParentId(blogId));
        return "blog::commentList";//thymeleaf fragment 局部刷新
    }

    @PostMapping("/comments")
    public String postComment(Comment comment){
        comment.setAvatar(avatar);
        comment.setCreateTime(new Date());
        commentService.save(comment);
        return "redirect:/comments/" + comment.getBlogId();
    }

}
