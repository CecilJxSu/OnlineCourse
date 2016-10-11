package cn.canlnac.course.service;

import cn.canlnac.course.entity.Chat;

import java.util.List;
import java.util.Map;

/**
 * 话题事务接口
 */
public interface ChatService {
    /**
     * 创建话题
     * @param chat  话题
     * @return      创建成功数目
     */
    int create(Chat chat);

    /**
     * 更新话题
     * @param chat  话题
     * @return      更新成功数目
     */
    int update(Chat chat);

    /**
     * 获取指定话题
     * @param id    话题ID
     * @return      话题
     */
    Chat findByID(int id);

    /**
     * 统计话题
     * @param conditions    条件：userId?：作者ID，可为空
     * @return              话题数目
     */
    int count(Map<String, Object> conditions);

    /**
     * 获取话题列表
     * @param start         分页开始位置
     * @param count         分页返回数目
     * @param sort          排序：日期date，热度rank
     * @param conditions    条件：userId?：作者ID，可为空
     * @return              话题列表
     */
    List<Chat> getList(
            int start,
            int count,
            String sort,
            Map<String, Object> conditions
    );

    /**
     * 删除话题
     * @param id    话题ID
     * @return      删除成功数目
     */
    int delete(int id);
}