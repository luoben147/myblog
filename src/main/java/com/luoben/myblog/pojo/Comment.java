package com.luoben.myblog.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_comment")
public class Comment {

    private Long id;
    private String nickname;
    private String email;
    private String content;
    //头像
    private String avatar;
    private Date createTime;

    private Long blogId;
    private Long parentCommentId;
    private String parentNickname;

    @TableField(exist = false)
    //回复评论
    private List<Comment> replyComments = new ArrayList<>();

    @TableField(exist = false)
    private Comment parentComment;

    @TableField(exist = false)
    private Blog blog;

}
