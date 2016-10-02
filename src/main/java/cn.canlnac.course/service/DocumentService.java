package cn.canlnac.course.service;

import cn.canlnac.course.entity.Document;

import java.util.List;

/**
 * 文档事务接口
 */
public interface DocumentService {
    /**
     * 创建文档
     * @param document  文档
     * @return          创建成功数目
     */
    int create(Document document);

    /**
     * 根据ID获取文档
     * @param id    文档ID
     * @return      文档
     */
    Document findByID(int id);

    /**
     * 统计文档数目
     * @param targetType    目标类型，课程：course；章节：catalog
     * @param targetId      目标ID
     * @return              文档数目
     */
    int count(String targetType, int targetId);

    /**
     * 获取指定类型和目标下的文档
     * @param start         分页开始位置
     * @param count         分页返回数目
     * @param targetType    目标类型，课程：course；章节：catalog
     * @param targetId      目标ID
     * @return              文档列表
     */
    List<Document> getDocuments(int start, int count, String targetType, int targetId);

    /**
     * 删除文档
     * @param id    文档ID
     * @return      删除成功数目
     */
    int delete(int id);
}