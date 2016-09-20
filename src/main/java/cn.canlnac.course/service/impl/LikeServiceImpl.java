package cn.canlnac.course.service.impl;

import cn.canlnac.course.dao.LikeDao;
import cn.canlnac.course.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by cecil on 2016/9/20.
 */
@Transactional
@Component(value = "LikeService")
public class LikeServiceImpl implements LikeService {
    @Autowired
    private LikeDao likeDao;
}
