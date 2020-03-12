package com.luoben.myblog.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.luoben.myblog.pojo.Tag;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagDao extends BaseMapper<Tag> {

    @Select("select b.* from t_blog_tags as a " +
            "left join t_tag as b on b.id=a.tag_id " +
            "where blog_id=#{blogId}")
    List<Tag> getTagsByBlogId(Long blogId);

    @Delete("delete from t_blog_tags where blog_id=#{blogId}")
    Boolean deleteBlogAndTag(Long blogId);

    @Select("select count(bt.blog_id) as num,t.*\n" +
            "from t_tag as t \n" +
            "left join t_blog_tags as bt on bt.tag_id=t.id\n" +
            "group by bt.tag_id")
    @Results({
            @Result(column = "num",property = "blogNumbyTag")
    })
    List<Tag> getAllTagAndTotal();
}
