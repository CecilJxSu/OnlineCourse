package cn.canlnac.course.service.impl;

import cn.canlnac.course.dao.MessageDao;
import cn.canlnac.course.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by cecil on 2016/9/20.
 */
@Transactional
@Component(value = "MessageService")
public class MessageServiceImpl implements MessageService {
    @Autowired
    private MessageDao messageDao;
}
