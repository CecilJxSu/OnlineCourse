package cn.canlnac.course.dao;

import cn.canlnac.course.entity.Profile;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 收藏记录数据接口
 */
@Component
public interface FavoriteDao {
    /**
     * 创建收藏记录
     * @param targetType    收藏类型，课程：course；话题：chat
     * @param targetId      收藏ID
     * @param userId        用户ID
     * @return              创建数目
     */
    int create(
            @Param("targetType") String targetType,
            @Param("targetId") int targetId,
            @Param("userId") int userId
    );

    /**
     * 取消收藏记录
     * @param targetType    收藏类型，课程：course；话题：chat
     * @param targetId      收藏ID
     * @param userId        用户ID
     * @return              删除记录数
     */
    int delete(
            @Param("targetType") String targetType,
            @Param("targetId") int targetId,
            @Param("userId") int userId
    );

    /**
     * 统计该（课程/话题）下的用户收藏数
     * @param targetType    收藏类型，课程：course；话题：chat
     * @param targetId      收藏目标ID
     * @return              用户收藏数
     */
    int count(
            @Param("targetType") String targetType,
            @Param("targetId") int targetId
    );

    /**
     * 获取该（课程/话题）下的收藏用户资料
     * @param start         分页位置开始
     * @param count         分页返回数目
     * @param targetType    收藏类型，课程：course；话题：chat
     * @param targetId      收藏目标ID
     * @return              用户资料
     */
    List<Profile> getUsers(
            @Param("start") int start,
            @Param("count") int count,
            @Param("targetType") String targetType,
            @Param("targetId") int targetId
    );

    /**
     * 用户是否收藏了该对象
     * @param userId        用户ID
     * @param targetType    收藏类型，课程：course；话题：chat
     * @param targetId      目标ID
     * @return              0：未关注，1：已关注
     */
    int isFavorite(
            @Param("userId") int userId,
            @Param("targetType") String targetType,
            @Param("targetId") int targetId
    );
}
