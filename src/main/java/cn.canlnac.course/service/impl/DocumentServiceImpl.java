package cn.canlnac.course.service.impl;

import cn.canlnac.course.dao.DocumentDao;
import cn.canlnac.course.entity.Document;
import cn.canlnac.course.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 文档事务接口实现
 */
@Transactional
@Component(value = "DocumentService")
public class DocumentServiceImpl implements DocumentService {
    @Autowired
    private DocumentDao documentDao;

    /**
     * 创建文档
     * @param document  文档
     * @return          创建成功数目
     */
    @Override
    public int create(Document document) {
        return documentDao.create(document);
    }

    /**
     * 根据ID获取文档
     * @param id    文档ID
     * @return      文档
     */
    @Override
    public Document findByID(int id) {
        return documentDao.findByID(id);
    }

    /**
     * 统计文档数目
     * @param targetType    目标类型，课程：course；章节：catalog
     * @param targetId      目标ID
     * @return              文档数目
     */
    @Override
    public int count(String targetType, int targetId) {
        return documentDao.count(targetType, targetId);
    }

    /**
     * 获取指定类型和目标下的文档
     * @param start         分页开始位置
     * @param count         分页返回数目
     * @param targetType    目标类型，课程：course；章节：catalog
     * @param targetId      目标ID
     * @return              文档列表
     */
    @Override
    public List<Document> getDocuments(
            int start,
            int count,
            String targetType,
            int targetId
    ) {
        return documentDao.getDocuments(start, count, targetType, targetId);
    }

    /**
     * 删除文档
     * @param id    文档ID
     * @return      删除成功数目
     */
    @Override
    public int delete(int id) {
        return documentDao.delete(id);
    }
}