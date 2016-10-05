package cn.canlnac.course.dao;

import cn.canlnac.course.entity.Profile;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 关注记录数据接口
 */
@Component
public interface FollowingDao {
    /**
     * 创建关注记录
     * @param targetId      被关注用户ID
     * @param userId        关注用户ID
     * @return              创建数目
     */
    int create(
            @Param("targetId") int targetId,
            @Param("userId") int userId
    );

    /**
     * 取消关注记录
     * @param targetId      关注用户ID
     * @param userId        用户ID
     * @return              删除记录数
     */
    int delete(
            @Param("targetId") int targetId,
            @Param("userId") int userId
    );

    /**
     * 统计自己的粉丝数
     * @param targetId      用户ID
     * @return              用户粉丝数
     */
    int countFollower(
            @Param("targetId") int targetId
    );

    /**
     * 统计自己的关注数
     * @param userId    用户ID
     * @return          用户关注数
     */
    int countFollowing(
            @Param("userId") int userId
    );

    /**
     * 获取自己的粉丝用户
     * @param start         分页位置开始
     * @param count         分页返回数目
     * @param targetId      用户ID
     * @return              粉丝用户资料
     */
    List<Profile> getFollowerUsers(
            @Param("start") int start,
            @Param("count") int count,
            @Param("targetId") int targetId
    );

    /**
     * 获取自己的关注用户
     * @param start     分页位置开始
     * @param count     分页返回数目
     * @param userId    用户ID
     * @return          关注用户资料
     */
    List<Profile> getFollowingUsers(
            @Param("start") int start,
            @Param("count") int count,
            @Param("userId") int userId
    );
}