package cn.canlnac.course.service.impl;

import cn.canlnac.course.dao.CatalogDao;
import cn.canlnac.course.entity.Catalog;
import cn.canlnac.course.service.CatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
     * 获取指定的章节
     * @param id    章节ID
     * @return
     */
    @Override
    public Catalog findByID(int id) {
        return catalogDao.findByID(id);
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
     * 获取课程下的所有章·节(二级树结构)
     * @param courseId  课程ID
     * @return          章节列表[{"chapter":chapter,"sections":{...},...]
     */
    public List getChapterAndSectionList(int courseId) {
        List returnList = new ArrayList();
        //获取章
        List<Catalog> chapters = catalogDao.getChapterList(courseId);
        for (Catalog chapter:chapters) {
            //获取节
            List<Catalog> sections = catalogDao.getSectionList(chapter.getId());
            //将章和其下的节放进Map单元
            Map unit = new HashMap();
            unit.put("chapter",chapter);
            unit.put("sections",sections);
            returnList.add(unit);
        }
        return returnList;
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