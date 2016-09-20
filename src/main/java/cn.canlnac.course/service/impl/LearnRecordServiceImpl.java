package cn.canlnac.course.service.impl;

import cn.canlnac.course.dao.LearnRecordDao;
import cn.canlnac.course.service.LearnRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by cecil on 2016/9/20.
 */
@Transactional
@Component(value = "LearnRecordService")
public class LearnRecordServiceImpl implements LearnRecordService{
    @Autowired
    private LearnRecordDao learnRecordDao;
}
