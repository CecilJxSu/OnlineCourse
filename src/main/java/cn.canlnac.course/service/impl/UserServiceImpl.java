package cn.canlnac.course.service.impl;

import cn.canlnac.course.dao.UserDao;
import cn.canlnac.course.entity.User;
import cn.canlnac.course.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 登录用户的事务接口实现
 */
@Transactional
@Component(value = "UserService")
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;

    /**
     * 创建用户
     * @param user          用户对象，必须的字段username,password,userStatus
     * @return              插入数目
     */
    @Override
    public int create(User user) {
        return userDao.create(user);
    }

    /**
     * 根据用户ID，获取用户数据
     * @param id    用户ID
     * @return      登录用户数据
     */
    @Override
    public User findByID(int id) {
        return userDao.findByID(id);
    }

    /**
     * 根据用户名查找用户信息
     * @param username  用户名
     * @return          登录用户信息
     */
    @Override
    public User findByUsername(String username) {
        return userDao.findByUsername(username);
    }

    /**
     * 根据条件获取登录用户列表
     * @param start         分页开始位置
     * @param count         分页返回数目
     * @param conditions    过滤条件，status数组，userStatus数组，username数组
     *                       status: active | lock | dead
     *                       userStatus: teacher | student | admin
     * @return              登录用户列表
     */
    @Override
    public List<User> getList(int start, int count, Map<String, Object> conditions) {
        return userDao.getList(start, count, conditions);
    }

    /**
     * 根据条件，统计登录用户数目
     * @param conditions    过滤条件，status数组，userStatus数组，username数组
     *                       status: active | lock | dead
     *                       userStatus: teacher | student | admin
     * @return              登录用户列表的数目
     */
    @Override
    public int count(Map<String, Object> conditions) {
        return userDao.count(conditions);
    }

    /**
     * 更新登录用户的字段
     * @param user      用户数据
     * @return          更新的数目
     */
    @Override
    public int update(User user) {
        return userDao.update(user);
    }
}
