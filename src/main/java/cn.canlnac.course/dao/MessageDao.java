package cn.canlnac.course.dao;

import org.apache.ibatis.annotations.Param;
import cn.canlnac.course.entity.Message;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 消息数据接口
 */
@Component
public interface MessageDao {
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
    int setRead(@Param("id") int id);

    /**
     * 统计自己的消息数目
     * @param toUserId  接收者用户ID
     * @param isRead    已读：Y，未读：N
     * @return          消息数目
     */
    int count(
            @Param("toUserId")int toUserId,
            @Param("isRead")String isRead
    );

    /**
     * 获取指定类型下的消息
     * @param start     分页开始位置
     * @param count     分页返回数目
     * @param toUserId  接收者用户ID
     * @param isRead    已读：Y，未读：N
     * @return          消息列表
     */
    List<Message> getMessages(
            @Param("start")int start,
            @Param("count")int count,
            @Param("toUserId")int toUserId,
            @Param("isRead")String isRead
    );

    /**
     * 删除消息
     * @param id    消息ID
     * @return      删除成功数目
     */
    int delete (int id);
}