package cn.canlnac.course.dao;

import cn.canlnac.course.entity.Profile;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 浏览记录数据接口
 */
@Component
public interface WatchDao {
    /**
     * 创建浏览记录
     * @param targetType    浏览类型，课程：course；话题：chat；文档：document
     * @param targetId      浏览ID
     * @param userId        用户ID
     * @return              创建数目
     */
    int create(
            @Param("targetType") String targetType,
            @Param("targetId") int targetId,
            @Param("userId") int userId
    );

    /**
     * 取消浏览记录
     * @param targetType    浏览类型，课程：course；话题：chat；文档：document
     * @param targetId      浏览ID
     * @param userId        用户ID
     * @return              删除记录数
     */
    int delete(
            @Param("targetType") String targetType,
            @Param("targetId") int targetId,
            @Param("userId") int userId
    );

    /**
     * 统计该（课程/话题/文档）下的用户浏览数
     * @param targetType    浏览类型，课程：course；话题：chat；文档：document
     * @param targetId      浏览目标ID
     * @return              用户浏览数
     */
    int count(
            @Param("targetType") String targetType,
            @Param("targetId") int targetId
    );

    /**
     * 获取该（课程/话题/文档）下的浏览用户资料
     * @param start         分页位置开始
     * @param count         分页返回数目
     * @param targetType    浏览类型，课程：course；话题：chat；文档：document
     * @param targetId      浏览目标ID
     * @return              用户资料
     */
    List<Profile> getUsers(
            @Param("start") int start,
            @Param("count") int count,
            @Param("targetType") String targetType,
            @Param("targetId") int targetId
    );
}
