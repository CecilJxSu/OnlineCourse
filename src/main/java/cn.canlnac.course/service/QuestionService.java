package cn.canlnac.course.service;

import cn.canlnac.course.entity.Question;

import java.util.List;

/**
 * 章节的小测事务接口
 */
public interface QuestionService {
    /**
     * 创建问题
     * @param questions 问题列表
     * @return          创建成功数目
     */
    int create(List<Question> questions);

    /**
     * 更新问题
     * @param question  问题
     * @return          更新成功数目
     */
    int update(Question question);

    /**
     * 获取某个章节下的所有小测
     * @param catalogId 章节ID
     * @return          问题列表
     */
    List<Question> getQuestions(int catalogId);

    /**
     * 删除小测
     * @param id    问题ID
     * @return      删除成功数目
     */
    int delete(int id);
}