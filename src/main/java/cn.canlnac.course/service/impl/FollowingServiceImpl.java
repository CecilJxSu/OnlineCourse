package cn.canlnac.course.service.impl;

import cn.canlnac.course.dao.FollowingDao;
import cn.canlnac.course.entity.Profile;
import cn.canlnac.course.service.FollowingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 关注记录数据事务接口实现
 */
@Transactional
@Component(value = "FollowingService")
public class FollowingServiceImpl implements FollowingService {
    @Autowired
    private FollowingDao followingDao;

    /**
     * 创建关注记录
     * @param targetId      关注用户ID
     * @param userId        用户ID
     * @return              创建ID
     */
    @Override
    public int create(int targetId, int userId) {
        return followingDao.create(targetId, userId);
    }

    /**
     * 取消关注记录
     * @param targetId      关注用户ID
     * @param userId        用户ID
     * @return              删除记录数
     */
    @Override
    public int delete(int targetId, int userId) {
        return followingDao.delete(targetId, userId);
    }

    /**
     * 统计自己的粉丝数
     * @param targetId      用户ID
     * @return              用户粉丝数
     */
    @Override
    public int countFollower(int targetId) {
        return followingDao.countFollower(targetId);
    }

    /**
     * 统计自己的关注数
     * @param userId    用户ID
     * @return          用户关注数
     */
    @Override
    public int countFollowing(int userId) {
        return followingDao.countFollowing(userId);
    }

    /**
     * 获取自己的粉丝用户
     * @param start         分页位置开始
     * @param count         分页返回数目
     * @param targetId      用户ID
     * @return              粉丝用户资料
     */
    @Override
    public List<Profile> getFollowerUsers(int start, int count, int targetId){
        return followingDao.getFollowerUsers(start, count, targetId);
    }

    /**
     * 获取自己的关注用户
     * @param start     分页位置开始
     * @param count     分页返回数目
     * @param userId    用户ID
     * @return          关注用户资料
     */
    @Override
    public List<Profile> getFollowingUsers(int start, int count, int userId) {
        return followingDao.getFollowingUsers(start, count, userId);
    }

    /**
     * 用户是否关注了目标用户
     * @param userId    用户ID
     * @param targetId  目标用户ID
     * @return          0：未关注，1：已关注
     */
    @Override
    public int isFollowing(int userId, int targetId) {
        return followingDao.isFollowing(userId,targetId);
    }
}
