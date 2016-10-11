package cn.canlnac.course.service.impl;

import cn.canlnac.course.dao.QuestionDao;
import cn.canlnac.course.entity.Question;
import cn.canlnac.course.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 章节的小测事务接口实现
 */
@Transactional
@Component(value = "QuestionService")
public class QuestionServiceImpl implements QuestionService {
    @Autowired
    private QuestionDao questionDao;

    /**
     * 创建问题
     * @param questions 问题列表
     * @return          创建成功数目
     */
    @Override
    public int create(List<Question> questions) {
        return questionDao.create(questions);
    }

    /**
     * 根据ID获取问题
     * @param id    问题ID
     * @return      问题
     */
    @Override
    public Question findById(int id) {
        return questionDao.findById(id);
    }

    /**
     * 更新问题
     * @param question  问题
     * @return          更新成功数目
     */
    @Override
    public int update(Question question) {
        return questionDao.update(question);
    }

    /**
     * 获取某个章节下的所有小测
     * @param catalogId 章节ID
     * @return          问题列表
     */
    @Override
    public List<Question> getQuestions(int catalogId) {
        return questionDao.getQuestions(catalogId);
    }

    /**
     * 删除小测
     * @param id    问题ID
     * @return      删除成功数目
     */
    @Override
    public int delete(int id) {
        return questionDao.delete(id);
    }
}