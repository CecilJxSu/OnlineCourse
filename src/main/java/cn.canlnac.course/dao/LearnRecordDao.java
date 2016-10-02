package cn.canlnac.course.dao;

import cn.canlnac.course.entity.LearnRecord;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 用户学习记录接口
 */
@Component
public interface LearnRecordDao {
    /**
     * 创建学习记录
     * @param learnRecord   学习记录
     * @return              创建成功数目
     */
    int create(LearnRecord learnRecord);

    /**
     * 更新学习记录
     * @param learnRecord   学习记录
     * @return              更新成功数目
     */
    int update(LearnRecord learnRecord);

    /**
     * 根据章节获取学习记录
     * @param catalogId 章节ID
     * @param userId    用户ID
     * @return          学习记录列表
     */
    List<LearnRecord> getLearnRecord(
            @Param("catalogId") int catalogId,
            @Param("userId") int userId
    );

    /**
     * 统计自己的学习记录数目
     * @param userId    用户ID
     * @return          学习记录数目
     */
    int count(int userId);

    /**
     * 获取自己的学习记录
     * @param start     分页开始位置
     * @param count     分页返回数目
     * @param userId    用户ID
     * @return          学习记录列表
     */
    List<LearnRecord> getLearnRecords(
            @Param("start") int start,
            @Param("count") int count,
            @Param("userId") int userId
    );
}