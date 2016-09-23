package cn.canlnac.course.service.impl;

import cn.canlnac.course.dao.UserDao;
import cn.canlnac.course.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by cecil on 2016/9/20.
 */
@Transactional
@Component(value = "UserService")
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;

    @Override
    public int create(String username, String password, String userStatus) {
        return userDao.create(username, password, userStatus);
    }
}
