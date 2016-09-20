package cn.canlnac.course.service.impl;

import cn.canlnac.course.dao.ReplyDao;
import cn.canlnac.course.service.ReplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by cecil on 2016/9/20.
 */
@Transactional
@Component(value = "ReplyService")
public class ReplyServiceImpl implements ReplyService {
    @Autowired
    private ReplyDao replyDao;
}
