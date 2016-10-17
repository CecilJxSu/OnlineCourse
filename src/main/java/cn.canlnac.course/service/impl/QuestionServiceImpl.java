package cn.canlnac.course.service.impl;

import cn.canlnac.course.dao.QuestionDao;
import cn.canlnac.course.entity.Question;
import cn.canlnac.course.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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
     * @param question  问题
     * @return          创建成功数目
     */
    @Override
    public int create(Question question) {
        return questionDao.create(question);
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
     * 根据章节ID获取问题
     * @param catalogId 章节ID
     * @return          问题
     */
    @Override
    public Question findByCatalogId(int catalogId) {
        return questionDao.findByCatalogId(catalogId);
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
     * 删除小测
     * @param id    问题ID
     * @return      删除成功数目
     */
    @Override
    public int delete(int id) {
        return questionDao.delete(id);
    }
}