package cn.canlnac.course.service.impl;

import cn.canlnac.course.dao.WatchDao;
import cn.canlnac.course.service.WatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by cecil on 2016/9/20.
 */
@Transactional
@Component(value = "WatchService")
public class WatchServiceImpl implements WatchService {
    @Autowired
    private WatchDao watchDao;
}
