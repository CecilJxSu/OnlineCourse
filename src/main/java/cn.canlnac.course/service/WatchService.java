package cn.canlnac.course.service;

import cn.canlnac.course.entity.Profile;

import java.util.List;

/**
 * 浏览记录数据事务接口
 */
public interface WatchService {
    /**
     * 创建浏览记录
     * @param targetType    浏览类型，课程：course；话题：chat；文档：document
     * @param targetId      浏览ID
     * @param userId        用户ID
     * @return              创建数目
     */
    int create(String targetType, int targetId, int userId);

    /**
     * 取消浏览记录
     * @param targetType    浏览类型，课程：course；话题：chat；文档：document
     * @param targetId      浏览ID
     * @param userId        用户ID
     * @return              删除记录数
     */
    int delete(String targetType, int targetId, int userId);

    /**
     * 统计该（课程/话题/文档）下的用户浏览数
     * @param targetType    浏览类型，课程：course；话题：chat；文档：document
     * @param targetId      浏览目标ID
     * @return              用户浏览数
     */
    int count(String targetType, int targetId);

    /**
     * 获取该（课程/话题/文档）下的浏览用户资料
     * @param start         分页位置开始
     * @param count         分页返回数目
     * @param targetType    浏览类型，课程：course；话题：chat；文档：document
     * @param targetId      浏览目标ID
     * @return              用户资料
     */
    List<Profile> getUsers(int start, int count, String targetType, int targetId);

    /**
     * 用户是否浏览了该对象
     * @param userId        用户ID
     * @param targetType    浏览类型，课程：course；话题：chat；文档：document
     * @param targetId      目标ID
     * @return              0：未关注，1：已关注
     */
    int isWatch(int userId, String targetType, int targetId);
}
