package cn.canlnac.course.service.impl;

import cn.canlnac.course.dao.MessageDao;
import cn.canlnac.course.entity.Message;
import cn.canlnac.course.service.MessageService;
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
     * 获取指定的消息
     * @param id    消息ID
     * @return      消息
     */
    @Override
    public Message findByID(int id) {
        return messageDao.findByID(id);
    }

    /**
     * 将未读消息设置成已读
     * @param id    消息ID
     * @return      修改成功数目
     */
    @Override
    public int setRead(int id) {
        return messageDao.setRead(id);
    }

    /**
     * 统计自己的、指定类型下的消息数目
     * @param toUserId  接收者用户ID
     * @param isRead    已读：Y，未读：N
     * @return          消息数目
     */
    @Override
    public int count(int toUserId, String isRead) {
        return messageDao.count(toUserId, isRead);
    }

    /**
     * 获取指定类型下的消息
     * @param start     分页开始位置
     * @param count     分页返回数目
     * @param toUserId  接收者用户ID
     * @param isRead    已读：Y，未读：N
     * @return          消息列表
     */
    @Override
    public List<Message> getMessages(
            int start,
            int count,
            int toUserId,
            String isRead
    ) {
        return messageDao.getMessages(start, count, toUserId, isRead);
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