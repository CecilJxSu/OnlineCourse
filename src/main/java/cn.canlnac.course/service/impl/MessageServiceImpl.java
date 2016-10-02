package cn.canlnac.course.service.impl;

import cn.canlnac.course.dao.MessageDao;
import cn.canlnac.course.service.MessageService;
import org.apache.logging.log4j.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 消息事务接口实现
 */
@Transactional
@Component(value = "MessageService")
public class MessageServiceImpl implements MessageService {
    @Autowired
    private MessageDao messageDao;

    /**
     * 创建消息
     * @param message   消息
     * @return          创建成功数目
     */
    @Override
    public int create (Message message) {
        return messageDao.create(message);
    }

    /**
     * 将未读消息设置成已读
     * @param ids   消息ID列表
     * @return      修改成功数目
     */
    @Override
    public int setRead(List<Integer> ids) {
        return messageDao.setRead(ids);
    }

    /**
     * 根据type分组，统计对应未读消息的数目
     * type，课程：course；话题：chat；评论：comment；系统：system；用户：user
     * @param toUserId  接收消息的用户ID，即获取自己的消息
     * @return          { course: 12, chat: 1, comment: 4, system: 1, user: 5 }
     */
    @Override
    public Map<String, Integer> countUnread(int toUserId) {
        return messageDao.countUnread(toUserId);
    }

    /**
     * 统计自己的、指定类型下的消息数目
     * @param toUserId  接收者用户ID
     * @param type      类型，课程：course；话题：chat；评论：comment；系统：system；用户：user
     * @return          消息数目
     */
    @Override
    public int count(int toUserId, String type) {
        return messageDao.count(toUserId, type);
    }

    /**
     * 获取指定类型下的消息
     * @param start     分页开始位置
     * @param count     分页返回数目
     * @param toUserId  接收者用户ID
     * @param type      类型，课程：course；话题：chat；评论：comment；系统：system；用户：user
     * @return          消息列表
     */
    @Override
    public List<Message> getMessages(
            int start,
            int count,
            int toUserId,
            String type
    ) {
        return messageDao.getMessages(start, count, toUserId, type);
    }

    /**
     * 删除消息
     * @param id    消息ID
     * @return      删除成功数目
     */
    @Override
    public int delete (int id) {
        return messageDao.delete(id);
    }
}