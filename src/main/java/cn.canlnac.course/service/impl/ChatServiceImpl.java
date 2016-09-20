package cn.canlnac.course.service.impl;

import cn.canlnac.course.dao.ChatDao;
import cn.canlnac.course.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by cecil on 2016/9/20.
 */
@Transactional
@Component(value = "ChatService")
public class ChatServiceImpl implements ChatService {
    @Autowired
    private ChatDao chatDao;
}
