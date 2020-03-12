package com.luoben.myblog.pojo;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor

@TableName("t_blog")
public class Blog {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private String title;
    private String content;
    private String firstPicture;//首图
    private String flag;    //原创| 转载|翻译
    private Integer views;  //浏览次数
    private boolean appreciation;   //开启赞赏
    private boolean shareStatement; //开启转载声明
    private boolean commentabled;   //开启评论
    private boolean published;      //发布 保存
    private boolean recommend;      //开启推荐
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;
    //这个属性用来在mybatis中进行连接查询的
    private Long typeId;
    private Long userId;
    //获取多个标签的id
    @TableField(exist = false)
    private String tagIds;
    private String description; //描述

    @TableField(exist = false)
    private Type type;
    @TableField(exist = false)
    private User user;
    @TableField(exist = false)
    private List<Tag> tags = new ArrayList<>();
    @TableField(exist = false)
    private List<Comment> comments = new ArrayList<>();


}
