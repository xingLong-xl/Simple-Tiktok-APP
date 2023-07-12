package com.imooc.service;

import com.imooc.bo.CommentBO;
import com.imooc.pojo.Comment;
import com.imooc.utils.PagedGridResult;
import com.imooc.vo.CommentVO;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentService {

    /*
    发表评论
     */
    public CommentVO createComment(CommentBO commentBO);

    /*
    查询评论列表
   */
    public PagedGridResult queryVlogComments(String vlogId,
                                             String userId,
                                             Integer page,
                                             Integer pageSize);

    /*
    删除评论
   */
    public void deleteComment(String commentUserId,
                              String commentId,
                              String vlogId);

    public Comment getComment(String id);
}
