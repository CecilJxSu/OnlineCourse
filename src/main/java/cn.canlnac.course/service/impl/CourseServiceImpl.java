package cn.canlnac.course.service.impl;

import cn.canlnac.course.dao.CourseDao;
import cn.canlnac.course.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by cecil on 2016/9/23.
 */
@Transactional
@Component(value = "CourseService")
public class CourseServiceImpl implements CourseService {
    @Autowired
    private CourseDao courseDao;
}
