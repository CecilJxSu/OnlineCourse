package cn.canlnac.course.service;

import cn.canlnac.course.entity.Profile;

import java.util.List;

/**
 * 关注记录数据事务接口
 */
public interface FollowingService {
    /**
     * 创建关注记录
     * @param targetId      关注用户ID
     * @param userId        用户ID
     * @return              创建数目
     */
    int create(int targetId, int userId);

    /**
     * 取消关注记录
     * @param targetId      关注用户ID
     * @param userId        用户ID
     * @return              删除记录数
     */
    int delete(int targetId, int userId);

    /**
     * 统计自己的粉丝数
     * @param targetId      用户ID
     * @return              用户粉丝数
     */
    int countFollower(int targetId);

    /**
     * 统计自己的关注数
     * @param userId    用户ID
     * @return          用户关注数
     */
    int countFollowing(int userId);

    /**
     * 获取自己的粉丝用户
     * @param start         分页位置开始
     * @param count         分页返回数目
     * @param targetId      用户ID
     * @return              粉丝用户资料
     */
    List<Profile> getFollowerUsers(int start, int count, int targetId);

    /**
     * 获取自己的关注用户
     * @param start     分页位置开始
     * @param count     分页返回数目
     * @param userId    用户ID
     * @return          关注用户资料
     */
    List<Profile> getFollowingUsers(int start, int count, int userId);

    /**
     * 用户是否关注了目标用户
     * @param userId    用户ID
     * @param targetId  目标用户ID
     * @return          0：未关注，1：已关注
     */
    int isFollowing(int userId, int targetId);
}
