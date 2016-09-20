package cn.canlnac.course.service.impl;

import cn.canlnac.course.dao.CatalogDao;
import cn.canlnac.course.service.CatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by cecil on 2016/9/20.
 */
@Transactional
@Component(value = "CatalogService")
public class CatalogServiceImpl implements CatalogService {
    @Autowired
    private CatalogDao catalogDao;
}
