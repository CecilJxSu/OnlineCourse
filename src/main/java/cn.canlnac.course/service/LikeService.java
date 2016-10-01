package cn.canlnac.course.service;

import cn.canlnac.course.entity.Profile;

import java.util.List;

/**
 * 点赞记录数据事务接口
 */
public interface LikeService {
    /**
     * 创建点赞记录
     * @param targetType    点赞类型，课程：course；话题：chat；评论：comment
     * @param targetId      点赞ID
     * @param userId        用户ID
     * @return              创建ID
     */
    int create(String targetType, int targetId, int userId);

    /**
     * 取消点赞记录
     * @param targetType    点赞类型，课程：course；话题：chat；评论：comment
     * @param targetId      点赞ID
     * @param userId        用户ID
     * @return              删除记录数
     */
    int delete(String targetType, int targetId, int userId);

    /**
     * 统计该（课程/话题/评论）下的用户点赞数
     * @param targetType    点赞类型，课程：course；话题：chat；评论：comment
     * @param targetId      点赞目标ID
     * @return              用户点赞数
     */
    int count(String targetType, int targetId);

    /**
     * 获取该（课程/话题/评论）下的点赞用户资料
     * @param start         分页位置开始
     * @param count         分页返回数目
     * @param targetType    点赞类型，课程：course；话题：chat；评论：comment
     * @param targetId      点赞目标ID
     * @return              用户资料
     */
    List<Profile> getUsers(int start, int count, String targetType, int targetId);
}
