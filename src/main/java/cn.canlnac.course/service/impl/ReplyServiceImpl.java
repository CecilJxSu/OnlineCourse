package cn.canlnac.course.service.impl;

import cn.canlnac.course.dao.ReplyDao;
import cn.canlnac.course.entity.Reply;
import cn.canlnac.course.service.ReplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 评论回复事务接口实现
 */
@Transactional
@Component(value = "ReplyService")
public class ReplyServiceImpl implements ReplyService {
    @Autowired
    private ReplyDao replyDao;

    /**
     * 创建回复内容
     * @param reply 回复评论的内容
     * @return      成功创建数目
     */
    @Override
    public int create (Reply reply) {
        return replyDao.create(reply);
    }

    /**
     * 获取评论下的所有回复内容
     * @param commentId 评论ID
     * @return          回复列表
     */
    @Override
    public List<Reply> getReplies(int commentId) {
        return replyDao.getReplies(commentId);
    }

    /**
     * 获取自己发表的回复
     * @param userId    用户ID
     * @return          回复列表
     */
    @Override
    public List<Reply> getOwnReplies(int userId) {
        return replyDao.getOwnReplies(userId);
    }

    /**
     * 删除回复
     * @param id    回复ID
     * @return      删除数目
     */
    @Override
    public int delete (int id) {
        return replyDao.delete(id);
    }
}