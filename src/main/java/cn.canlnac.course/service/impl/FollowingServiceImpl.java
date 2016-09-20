package cn.canlnac.course.service.impl;

import cn.canlnac.course.dao.FollowingDao;
import cn.canlnac.course.service.FollowingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by cecil on 2016/9/20.
 */
@Transactional
@Component(value = "FollowingService")
public class FollowingServiceImpl implements FollowingService {
    @Autowired
    private FollowingDao followingDao;
}
