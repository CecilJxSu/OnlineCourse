package cn.canlnac.course.service;

import cn.canlnac.course.entity.User;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 登录用户的事务接口
 */
public interface UserService {
    /**
     * 创建用户
     * @param user          用户对象，必须的字段username,password,userStatus
     * @return              插入数目
     */
    int create(User user);

    /**
     * 根据用户ID，获取该用户登录信息
     * @param id    用户ID
     * @return      登录用户数据
     */
    User findByID(int id);

    /**
     * 根据用户名查找登录用户
     * @param username  用户名
     * @return          登录用户信息
     */
    User findByUsername(String username);

    /**
     * 根据条件获取登录用户列表
     * @param start         分页开始位置
     * @param count         分页返回数目
     * @param conditions    过滤条件，status数组，userStatus数组，username数组
     *                       status: active | lock | dead
     *                       userStatus: teacher | student | admin
     * @return              登录用户数据列表
     */
    List<User> getList(int start, int count, Map<String, Object> conditions);

    /**
     * 根据条件，统计登录用户数目
     * @param conditions    过滤条件，status数组，userStatus数组，username数组
     *                       status: active | lock | dead
     *                       userStatus: teacher | student | admin
     * @return              登录用户列表的数目
     */
    int count(Map<String, Object> conditions);

    /**
     * 更新登录用户的字段
     * @param id        用户ID
     * @param fields    字段和值
     * @return          更新的数目
     */
    int update(int id, Map<String, Object> fields);

    /**
     * 临时封号，永久封号
     * @param id        用户ID
     * @param status    修改的状态，lock，dead
     * @param lockDate  封号开始时间
     * @param endDate   封号结束时间，dead状态，可以省略
     * @return          更新的数目
     */
    int lock(int id, String status, Date lockDate, Date endDate);

    /**
     * 对用户进行解封
     * @param id    用户ID
     * @return      更新的数目
     */
    int unlock(int id);
}