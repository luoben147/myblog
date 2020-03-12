package com.luoben.myblog.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 把博客和标签关系存到数据库中使用的类
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_blog_tags")
public class BlogAndTag {

    private Long tagId;

    private Long blogId;
}
