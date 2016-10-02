package cn.canlnac.course.service.impl;

import cn.canlnac.course.dao.AnswerDao;
import cn.canlnac.course.entity.Answer;
import cn.canlnac.course.service.AnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 回答事务接口实现
 */
@Transactional
@Component(value = "AnswerService")
public class AnswerServiceImpl implements AnswerService {
    @Autowired
    private AnswerDao answerDao;

    /**
     * 创建回答
     * @param answer    回答
     * @return          成功创建数目
     */
    @Override
    public int create(Answer answer) {
        return answerDao.create(answer);
    }

    /**
     * 更新回答
     * @param answer    回答
     * @return          成功更新数目
     */
    @Override
    public int update(Answer answer) {
        return answerDao.update(answer);
    }

    /**
     * 获取回答
     * @param catalogId 章节ID
     * @param userId    用户ID
     * @return          回答
     */
    @Override
    public Answer getAnswer(
            int catalogId,
            int userId
    ) {
        return answerDao.getAnswer(catalogId, userId);
    }

    /**
     * 统计用户的回答数
     * @param userId    用户ID
     * @return          回答数
     */
    @Override
    public int count(int userId) {
        return answerDao.count(userId);
    }

    /**
     * 获取用户回答
     * @param start     分页开始位置
     * @param count     分页返回数目
     * @param userId    用户ID
     * @return
     */
    @Override
    public List<Answer> getAnswers(
            int start,
            int count,
            int userId
    ) {
        return answerDao.getAnswers(start, count, userId);
    }
}
