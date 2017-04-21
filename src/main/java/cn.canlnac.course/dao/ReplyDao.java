package cn.canlnac.course.dao;

import cn.canlnac.course.entity.Reply;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 评论回复数据接口
 */
@Component
public interface ReplyDao {
    /**
     * 创建回复内容
     * @param reply 回复评论的内容
     * @return      成功创建数目
     */
    int create (Reply reply);

    /**
     * 获取指定回复
     * @param id    回复ID
     * @return      回复
     */
    Reply findByID(int id);

    /**
     * 获取评论下的所有回复内容
     * @param commentId 评论ID
     * @return          回复列表
     */
    List<Reply> getReplies(int commentId);

    /**
     * 获取自己发表的回复
     * @param userId    用户ID
     * @return          回复列表
     */
    List<Reply> getOwnReplies(int userId);

    /**
     * 删除回复
     * @param id    回复ID
     * @return      删除数目
     */
    int delete (int id);
}