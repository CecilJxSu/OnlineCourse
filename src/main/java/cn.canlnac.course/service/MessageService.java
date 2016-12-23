package cn.canlnac.course.service;



import cn.canlnac.course.entity.Message;

import java.util.List;
import java.util.Map;

/**
 * 消息事务接口
 */
public interface MessageService {
    /**
     * 创建消息
     * @param message   消息
     * @return          创建成功数目
     */
    int create (Message message);

    /**
     * 获取指定的消息
     * @param id    消息ID
     * @return      消息
     */
    Message findByID(int id);

    /**
     * 将未读消息设置成已读
     * @param id    消息ID
     * @return      修改成功数目
     */
    int setRead(int id);

    /**
     * 统计自己的、指定类型下的消息数目
     * @param toUserId  接收者用户ID
     * @param isRead    已读：Y，未读：N
     * @return          消息数目
     */
    int count(int toUserId, String isRead);

    /**
     * 获取指定类型下的消息
     * @param start     分页开始位置
     * @param count     分页返回数目
     * @param toUserId  接收者用户ID
     * @param isRead    已读：Y，未读：N
     * @return          消息列表
     */
    List<Message> getMessages(int start, int count, int toUserId, String isRead);

    /**
     * 删除消息
     * @param id    消息ID
     * @return      删除成功数目
     */
    int delete (int id);
}