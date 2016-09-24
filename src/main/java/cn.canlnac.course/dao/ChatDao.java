package cn.canlnac.course.dao;

import cn.canlnac.course.entity.Chat;
import org.springframework.stereotype.Component;

/**
 * Created by cecil on 2016/9/20.
 */
@Component
public interface ChatDao {

    /**
     * @author can
     * 根据话题id获取话题内容
     * @param id
     * @return Chat
     */
    Chat getById(int id);

    int insert(Chat chat);
}
