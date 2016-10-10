package cn.canlnac.course.dao;

import cn.canlnac.course.entity.Catalog;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 课程章节接口
 */
@Component
public interface CatalogDao {
    /**
     * 创建章节
     * @param catalog   章节
     * @return          章节ID
     */
    int create(@Param("catalog") Catalog catalog);

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