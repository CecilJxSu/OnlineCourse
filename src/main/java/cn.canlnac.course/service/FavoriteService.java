package cn.canlnac.course.service;

import cn.canlnac.course.entity.Profile;

import java.util.List;

/**
 * 收藏记录数据事务接口
 */
public interface FavoriteService {
    /**
     * 创建收藏记录
     * @param targetType    收藏类型，课程：course；话题：chat
     * @param targetId      收藏ID
     * @param userId        用户ID
     * @return              创建数目
     */
    int create(String targetType, int targetId, int userId);

    /**
     * 取消收藏记录
     * @param targetType    收藏类型，课程：course；话题：chat
     * @param targetId      收藏ID
     * @param userId        用户ID
     * @return              删除记录数
     */
    int delete(String targetType, int targetId, int userId);

    /**
     * 统计该（课程/话题）下的用户收藏数
     * @param targetType    收藏类型，课程：course；话题：chat
     * @param targetId      收藏目标ID
     * @return              用户收藏数
     */
    int count(String targetType, int targetId);

    /**
     * 获取该（课程/话题）下的收藏用户资料
     * @param start         分页位置开始
     * @param count         分页返回数目
     * @param targetType    收藏类型，课程：course；话题：chat
     * @param targetId      收藏目标ID
     * @return              用户资料
     */
    List<Profile> getUsers(int start, int count, String targetType, int targetId);

    /**
     * 用户是否收藏了该对象
     * @param userId        用户ID
     * @param targetType    收藏类型，课程：course；话题：chat
     * @param targetId      目标ID
     * @return              0：未关注，1：已关注
     */
    int isFavorite(int userId, String targetType, int targetId);
}
