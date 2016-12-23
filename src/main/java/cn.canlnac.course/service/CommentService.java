package cn.canlnac.course.service;

import cn.canlnac.course.entity.Comment;

import java.util.List;

/**
 * 评论事务接口
 */
public interface CommentService {
    /**
     * 创建评论
     * @param comment   评论
     * @return          成功创建数目
     */
    int create(Comment comment);

    /**
     * 更新评论
     * @param comment   评论
     * @return          更新数目
     */
    int update(Comment comment);

    /**
     * 获取指定评论
     * @param id    评论ID
     * @return      评论
     */
    Comment findByID(int id);

    /**
     * 统计评论
     * @param targetType    评论类型，课程：course；话题：comment
     * @param targetId      目标ID
     * @return              评论数目
     */
    int count(
            String targetType,
            int targetId
    );

    /**
     * 获取评论列表
     * @param start         分页开始位置
     * @param count         分页返回数目
     * @param sort          排序：日期date，热度rank
     * @param targetType    评论类型，课程：course；话题：comment
     * @param targetId      目标ID
     * @return              评论列表
     */
    List<Comment> getList(
            int start,
            int count,
            String sort,
            String targetType,
            int targetId
    );

    /**
     * 删除评论
     * @param id    评论ID
     * @return      删除成功数目
     */
    int delete(int id);
}