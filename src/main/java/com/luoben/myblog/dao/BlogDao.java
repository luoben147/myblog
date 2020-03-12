package com.luoben.myblog.dao;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.luoben.myblog.handler.MybatisTypeHandler;
import com.luoben.myblog.pojo.Blog;
import com.luoben.myblog.pojo.BlogAndTag;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface BlogDao extends BaseMapper<Blog> {

    /**
     * Mybatis多表查询 结合 Mybatis-Plus 的自定义sql
     * ${ew.customSqlSegment}为 前面构造的查询条件queryWrapper  拼接 where
     * @param page
     * @param queryWrapper
     * @return
     */
    @Select("select b.*,t.name " +
            "from t_blog as b " +
            "left join t_type as t on  t.id=b.type_id " +
            "${ew.customSqlSegment} ")
    @Results(id="blogResult",value ={
            @Result(column = "create_time",property = "createTime",javaType = Date.class,jdbcType =JdbcType.TIMESTAMP),
            @Result(column = "update_time",property = "updateTime",javaType = Date.class,jdbcType =JdbcType.TIMESTAMP),
            @Result(column = "recommend",property ="recommend",javaType =Boolean.class,jdbcType =JdbcType.INTEGER,typeHandler = MybatisTypeHandler.class),
            @Result(column ="type_id",property = "type",
            one = @One(select = "com.luoben.myblog.dao.TypeDao.selectById")),
            @Result(column = "user_id",property = "user",
                    one=@One(select = "com.luoben.myblog.dao.UserDao.selectById")),
    })
    IPage<Blog> searchBlog(IPage<Blog> page,@Param(Constants.WRAPPER) Wrapper<Blog> queryWrapper);


    @Options(useGeneratedKeys = true,keyProperty = "id",keyColumn = "id")
    @Insert("insert into t_blog (title, content, first_picture, flag,\n" +
            "views, appreciation, share_statement, commentabled,published,\n" +
            "recommend, create_time, update_time, type_id, user_id, description)\n" +
            "values (#{title},#{content},#{firstPicture},#{flag},#{views}," +
            "#{appreciation,jdbcType=INTEGER,typeHandler = com.luoben.myblog.handler.MybatisTypeHandler}," +
            "#{shareStatement,jdbcType=INTEGER,typeHandler = com.luoben.myblog.handler.MybatisTypeHandler}," +
            "#{commentabled,jdbcType=INTEGER,typeHandler = com.luoben.myblog.handler.MybatisTypeHandler}," +
            "#{published,jdbcType=INTEGER,typeHandler = com.luoben.myblog.handler.MybatisTypeHandler}," +
            "#{recommend,jdbcType=INTEGER,typeHandler = com.luoben.myblog.handler.MybatisTypeHandler}," +
            "#{createTime},\n" +
            "#{updateTime},#{typeId},#{userId},#{description});")
    Boolean saveBlog(Blog blog);


    Integer saveBlogAndTag(List<BlogAndTag> blogAndTags);

    @Select("select * from t_blog where recommend=true order by update_time limit 0,#{size}")
    List<Blog> listRecommendBlogTop(Integer size);

    @Select("select * from t_blog")
    @Results(value = {
            @Result(column = "user_id",property = "user",
                one=@One(select = "com.luoben.myblog.dao.UserDao.selectById")),
            @Result(column = "type_id",property = "type",
                one = @One(select = "com.luoben.myblog.dao.TypeDao.selectById"))
    })
    IPage<Blog> listBlog(IPage<Blog> blogs);


    @Select("select * from t_blog where title like CONCAT('%',#{query},'%') or content like CONCAT('%',#{query},'%')")
    @Results(value = {
            @Result(column = "user_id",property = "user",
                    one=@One(select = "com.luoben.myblog.dao.UserDao.selectById")),
            @Result(column = "type_id",property = "type",
                    one = @One(select = "com.luoben.myblog.dao.TypeDao.selectById"))
    })
    IPage<Blog> listBlogByQuery(IPage<Blog> blogs,@Param("query") String query);

    @Select("select * from t_blog where id=#{id}")
    @Results(value = {
            @Result(column = "user_id",property = "user",
                    one=@One(select = "com.luoben.myblog.dao.UserDao.selectById")),
            @Result(column = "type_id",property = "type",
                    one = @One(select = "com.luoben.myblog.dao.TypeDao.selectById")),
            @Result(column = "id",property = "tags",
                     many = @Many(select = "com.luoben.myblog.dao.TagDao.getTagsByBlogId"))
    })
    Blog getBlogById(Long id);


    @Update("update t_blog set views=views+1 where id=#{id}")
    Integer updateViews(Long id);



    @Select("select a.* from t_blog as a " +
            "left join t_blog_tags as b on b.blog_id=a.id " +
            "where b.tag_id=#{tagId}")
    @Results(value ={
            @Result(column = "id",property = "id"),
            @Result(column = "recommend",property ="recommend",javaType =Boolean.class,jdbcType =JdbcType.INTEGER,typeHandler = MybatisTypeHandler.class),
            @Result(column ="type_id",property = "type",
                    one = @One(select = "com.luoben.myblog.dao.TypeDao.selectById")),
            @Result(column = "user_id",property = "user",
                    one=@One(select = "com.luoben.myblog.dao.UserDao.selectById")),
            @Result(column = "id",property = "tags",
            many = @Many(select = "com.luoben.myblog.dao.TagDao.getTagsByBlogId"))
    })
    IPage<Blog> listBlogByTagId(IPage<Blog> blogs,@Param("tagId") Long tagId);

    @Select("select DATE_FORMAT(create_time,'%Y') as year from t_blog GROUP BY year ORDER BY year desc")
    List<String> findGroupYear();

    @Select("select * from t_blog where DATE_FORMAT(create_time,'%Y')=#{year}")
    List<Blog> findBlogByYear(String year);
}
