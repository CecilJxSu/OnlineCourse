package cn.canlnac.course.dao;

import cn.canlnac.course.entity.Chat;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 话题数据接口
 */
@Component
public interface ChatDao {
    /**
     * 创建话题
     * @param chat  话题
     * @return      话题ID
     */
    int create(Chat chat);

    /**
     * 获取指定话题
     * @param id    话题ID
     * @return      话题
     */
    Chat findByID(int id);

    /**
     * 统计话题
     * @param conditions    条件：userId?：作者ID，可为空。[Object需为]
     * @return              话题数目
     */
    int count(@Param("conditions") Map<String, Object> conditions);

    /**
     * 获取话题列表
     * @param start         分页开始位置
     * @param count         分页返回数目
     * @param sort          排序：日期date，热度rank
     * @param conditions    条件：userId?：作者ID，可为空
     * @return              话题列表
     */
    List<Chat> getList(
            @Param("start") int start,
            @Param("count") int count,
            @Param("sort") String sort,
            @Param("conditions") Map<String, Object> conditions
    );

    /**
     * 删除话题
     * @param id    话题ID
     * @return      删除成功数目
     */
    int delete(int id);
}