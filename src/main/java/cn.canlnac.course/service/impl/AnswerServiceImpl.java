package cn.canlnac.course.service.impl;

import cn.canlnac.course.dao.AnswerDao;
import cn.canlnac.course.service.AnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by cecil on 2016/9/20.
 */
@Transactional
@Component(value = "AnswerService")
public class AnswerServiceImpl implements AnswerService {
    @Autowired
    private AnswerDao answerDao;
}
