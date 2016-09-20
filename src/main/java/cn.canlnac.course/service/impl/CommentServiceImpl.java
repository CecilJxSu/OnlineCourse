package cn.canlnac.course.service.impl;

import cn.canlnac.course.dao.CommentDao;
import cn.canlnac.course.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by cecil on 2016/9/20.
 */
@Transactional
@Component(value = "CommentService")
public class CommentServiceImpl implements CommentService {
    @Autowired
    private CommentDao commentDao;
}
