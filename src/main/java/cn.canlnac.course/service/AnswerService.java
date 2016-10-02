package cn.canlnac.course.service;

import cn.canlnac.course.entity.Answer;

import java.util.List;

/**
 * 回答事务接口
 */
public interface AnswerService {
    /**
     * 创建回答
     * @param answer    回答
     * @return          成功创建数目
     */
    int create(Answer answer);

    /**
     * 更新回答
     * @param answer    回答
     * @return          成功更新数目
     */
    int update(Answer answer);

    /**
     * 获取回答
     * @param catalogId 章节ID
     * @param userId    用户ID
     * @return          回答
     */
    Answer getAnswer(
            int catalogId,
            int userId
    );

    /**
     * 统计用户的回答数
     * @param userId    用户ID
     * @return          回答数
     */
    int count(int userId);

    /**
     * 获取用户回答
     * @param start     分页开始位置
     * @param count     分页返回数目
     * @param userId    用户ID
     * @return
     */
    List<Answer> getAnswers(
            int start,
            int count,
            int userId
    );
}
