package cn.canlnac.course.service.impl;

import cn.canlnac.course.dao.LearnRecordDao;
import cn.canlnac.course.entity.LearnRecord;
import cn.canlnac.course.service.LearnRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 用户学习记录事务接口实现
 */
@Transactional
@Component(value = "LearnRecordService")
public class LearnRecordServiceImpl implements LearnRecordService{
    @Autowired
    private LearnRecordDao learnRecordDao;

    /**
     * 创建学习记录
     * @param learnRecord   学习记录
     * @return              创建成功数目
     */
    @Override
    public int create(LearnRecord learnRecord) {
        return learnRecordDao.create(learnRecord);
    }

    /**
     * 更新学习记录
     * @param learnRecord   学习记录
     * @return              更新成功数目
     */
    @Override
    public int update(LearnRecord learnRecord) {
        return learnRecordDao.update(learnRecord);
    }

    /**
     * 根据章节获取学习记录
     * @param catalogId 章节ID
     * @param userId    用户ID
     * @return          学习记录列表
     */
    @Override
    public List<LearnRecord> getLearnRecord(int catalogId, int userId) {
        return learnRecordDao.getLearnRecord(catalogId, userId);
    }

    /**
     * 统计自己的学习记录数目
     * @param userId    用户ID
     * @return          学习记录数目
     */
    @Override
    public int count(int userId) {
        return learnRecordDao.count(userId);
    }

    /**
     * 获取自己的学习记录
     * @param start     分页开始位置
     * @param count     分页返回数目
     * @param userId    用户ID
     * @return          学习记录列表
     */
    @Override
    public List<LearnRecord> getLearnRecords(int start, int count, int userId) {
        return learnRecordDao.getLearnRecords(start, count, userId);
    }
}