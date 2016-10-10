package cn.canlnac.course.service.impl;

import cn.canlnac.course.dao.CatalogDao;
import cn.canlnac.course.entity.Catalog;
import cn.canlnac.course.service.CatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 课程章节事务接口实现
 */
@Transactional
@Component(value = "CatalogService")
public class CatalogServiceImpl implements CatalogService {
    @Autowired
    private CatalogDao catalogDao;

    /**
     * 创建章节
     * @param catalog   章节
     * @return          创建成功数目
     */
    @Override
    public int create(Catalog catalog) {
        return catalogDao.create(catalog);
    }

    /**
     * 更新章节
     * @param catalog   章节
     * @return          成功更新数目
     */
    @Override
    public int update(Catalog catalog) {
        return catalogDao.update(catalog);
    }

    /**
     * 获取课程下的所有章节
     * @param courseId  课程ID
     * @return          章节列表
     */
    @Override
    public List<Catalog> getList(int courseId) {
        return catalogDao.getList(courseId);
    }

    /**
     * 删除章节
     * @param id    章节ID
     * @return      成功删除数目
     */
    @Override
    public int delete(int id) {
        return catalogDao.delete(id);
    }
}