package cn.canlnac.course.service.impl;

import cn.canlnac.course.dao.ChatDao;
import cn.canlnac.course.entity.Chat;
import cn.canlnac.course.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 话题事务接口实现
 */
@Transactional
@Component(value = "ChatService")
public class ChatServiceImpl implements ChatService {
    @Autowired
    private ChatDao chatDao;

    /**
     * 创建话题
     * @param chat  话题
     * @return      话题ID
     */
    @Override
    public int create(Chat chat) {
        return chatDao.create(chat);
    }

    /**
     * 获取指定话题
     * @param id    话题ID
     * @return      话题
     */
    @Override
    public Chat findByID(int id) {
        return chatDao.findByID(id);
    }

    /**
     * 统计话题
     * @param conditions    条件：userId?：作者ID，可为空
     * @return              话题数目
     */
    @Override
    public int count(Map<String, Object> conditions) {
        return chatDao.count(conditions);
    }

    /**
     * 获取话题列表
     * @param start         分页开始位置
     * @param count         分页返回数目
     * @param sort          排序：日期date，热度rank
     * @param conditions    条件：userId?：作者ID，可为空
     * @return              话题列表
     */
    @Override
    public List<Chat> getList(
            int start,
            int count,
            String sort,
            Map<String, Object> conditions
    ) {
        return chatDao.getList(start, count, sort, conditions);
    }

    /**
     * 删除话题
     * @param id    话题ID
     * @return      删除成功数目
     */
    @Override
    public int delete(int id) {
        return chatDao.delete(id);
    }
}