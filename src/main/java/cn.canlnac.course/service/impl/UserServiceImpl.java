package cn.canlnac.course.service.impl;

import cn.canlnac.course.dao.UserDao;
import cn.canlnac.course.entity.User;
import cn.canlnac.course.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

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

    @Override
    public User findByID(int id) {
        return userDao.findByID(id);
    }

    @Override
    public List<User> getList(int start, int count, Map<String, Object> conditions) {
        return userDao.getList(start, count, conditions);
    }

    @Override
    public int count(Map<String, Object> conditions) {
        return userDao.count(conditions);
    }

    @Override
    public int update(int id, Map<String, Object> fields) {
        return userDao.update(id, fields);
    }

    @Override
    public int lock(int id, String status, Date lockDate, Date endDate) {
        return userDao.lock(id, status, lockDate, endDate);
    }

    @Override
    public int unlock(int id) {
        return userDao.unlock(id);
    }
}
