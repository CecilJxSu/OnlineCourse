package cn.canlnac.course.service.impl;

import cn.canlnac.course.dao.DocumentDao;
import cn.canlnac.course.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by cecil on 2016/9/20.
 */
@Transactional
@Component(value = "DocumentService")
public class DocumentServiceImpl implements DocumentService {
    @Autowired
    private DocumentDao documentDao;
}
