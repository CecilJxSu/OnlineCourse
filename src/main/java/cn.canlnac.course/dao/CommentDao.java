package cn.canlnac.course.dao;

import cn.canlnac.course.entity.Comment;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 评论数据接口
 */
@Component
public interface CommentDao {
    /**
     * 创建评论
     * @param comment   评论
     * @return          评论ID
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
            @Param("targetType") String targetType,
            @Param("targetId") int targetId
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
            @Param("start") int start,
            @Param("count") int count,
            @Param("sort") String sort,
            @Param("targetType") String targetType,
            @Param("targetId") int targetId
    );

    /**
     * 删除评论
     * @param id    评论ID
     * @return      删除成功数目
     */
    int delete(int id);
}