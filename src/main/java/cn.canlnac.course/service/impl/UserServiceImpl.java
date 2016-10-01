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
 * 登录用户的事务接口实现
 */
@Transactional
@Component(value = "UserService")
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;

    /**
     * 创建用户
     * @param username      用户名
     * @param password      密码
     * @param userStatus    用户类型：老师，学生，管理员
     * @return              用户ID
     */
    @Override
    public int create(String username, String password, String userStatus) {
        return userDao.create(username, password, userStatus);
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
     * @param id        用户ID
     * @param fields    字段和值
     * @return          更新的数目
     */
    @Override
    public int update(int id, Map<String, Object> fields) {
        return userDao.update(id, fields);
    }

    /**
     * 临时封号，永久封号
     * @param id        用户ID
     * @param status    修改的状态，lock，dead
     * @param lockDate  封号开始时间
     * @param endDate   封号结束时间，dead状态，可以省略
     * @return          更新的数目
     */
    @Override
    public int lock(int id, String status, Date lockDate, Date endDate) {
        return userDao.lock(id, status, lockDate, endDate);
    }

    /**
     * 对用户进行解封
     * @param id    用户ID
     * @return      更新的数目
     */
    @Override
    public int unlock(int id) {
        return userDao.unlock(id);
    }
}
