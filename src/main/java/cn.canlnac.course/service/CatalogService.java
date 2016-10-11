package cn.canlnac.course.service;

import cn.canlnac.course.entity.Catalog;

import java.util.List;

/**
 * 课程章节事务接口
 */
public interface CatalogService {
    /**
     * 创建章节
     * @param catalog   章节
     * @return          创建成功数目
     */
    int create(Catalog catalog);

    /**
     * 获取指定的章节
     * @param id    章节ID
     * @return
     */
    Catalog findByID(int id);

    /**
     * 更新章节
     * @param catalog   章节
     * @return          成功更新数目
     */
    int update(Catalog catalog);

    /**
     * 获取课程下的所有章节
     * @param courseId  课程ID
     * @return          章节列表
     */
    List<Catalog> getList(int courseId);

    /**
     * 删除章节
     * @param id    章节ID
     * @return      成功删除数目
     */
    int delete(int id);
}