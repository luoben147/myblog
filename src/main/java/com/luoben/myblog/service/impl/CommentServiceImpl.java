package com.luoben.myblog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.luoben.myblog.dao.CommentDao;
import com.luoben.myblog.pojo.Comment;
import com.luoben.myblog.service.CommentService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl extends ServiceImpl<CommentDao, Comment> implements CommentService {

    @Autowired
    private CommentDao commentDao;

    private List<Comment> commentList;

    @Override
    public List<Comment> findByBlogIdParentId(Long blogId) {

        //获取该博客所有评论
        commentList = commentDao.findByBlogId(blogId);
        List<Comment> comments = builTree();
        List<Comment> comments1 = eachComment(comments);
        return comments1;
    }


    //建立树形结构
    public List<Comment> builTree() {
        List<Comment> treeMenus = new ArrayList<>();
        for (Comment menuNode : getRootNode()) {
            menuNode = buildChilTree(menuNode);
            treeMenus.add(menuNode);
        }
        commentList.clear();
        return treeMenus;
    }

    //递归，建立子树形结构
    private Comment buildChilTree(Comment pNode) {
        Comment parent= new Comment();
        BeanUtils.copyProperties(pNode, parent);
        List<Comment> chilMenus = new ArrayList<Comment>();
        for (Comment menuNode : commentList) {
            if (menuNode.getParentCommentId()==pNode.getId()) {
                //赋值父节点值
                menuNode.setParentComment(parent);
                chilMenus.add(buildChilTree(menuNode));
            }
        }
        pNode.setReplyComments(chilMenus);
        return pNode;
    }

    //获取根节点
    private List<Comment> getRootNode() {
        List<Comment> rootMenuLists = new ArrayList<>();
        for (Comment menuNode : commentList) {
            if (menuNode.getParentCommentId()==-1) {
                rootMenuLists.add(menuNode);
            }
        }
        return rootMenuLists;
    }

    /**
     * 循环每个顶级的评论节点
     *
     * @param comments
     * @return
     */
    private List<Comment> eachComment(List<Comment> comments) {
        List<Comment> commentsView = new ArrayList<>();
        for (Comment comment : comments) {
            Comment c = new Comment();
            BeanUtils.copyProperties(comment, c);
            commentsView.add(c);
        }
        //合并评论的各层子代到第一级子代集合中
        combineChildren(commentsView);
        return commentsView;
    }

    /**
     * @param comments root根节点，blog不为空的对象集合
     * @return
     */
    private void combineChildren(List<Comment> comments) {

        for (Comment comment : comments) {
            List<Comment> replys1 = comment.getReplyComments();
            for (Comment reply1 : replys1) {
                //循环迭代，找出子代，存放在tempReplys中
                recursively(reply1);
            }
            //修改顶级节点的reply集合为迭代处理后的集合
            comment.setReplyComments(tempReplys);
            //清除临时存放区
            tempReplys = new ArrayList<>();
        }
    }

    //存放迭代找出的所有子代的集合
    private List<Comment> tempReplys = new ArrayList<>();

    /**
     * 递归迭代，剥洋葱
     *
     * @param comment 被迭代的对象
     * @return
     */
    private void recursively(Comment comment) {
        List<Comment> collect = tempReplys.stream().filter(t -> t.getId() == comment.getId()).collect(Collectors.toList());
        if(collect.size()==0){
            tempReplys.add(comment);//顶节点添加到临时存放集合
        }
        if (comment.getReplyComments().size() > 0) {
            List<Comment> replys = comment.getReplyComments();
            for (Comment reply : replys) {
                tempReplys.add(reply);
                if (reply.getReplyComments().size() > 0) {
                    recursively(reply);
                }
            }
        }
    }

}
