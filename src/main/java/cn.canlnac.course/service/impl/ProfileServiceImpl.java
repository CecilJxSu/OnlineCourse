package cn.canlnac.course.service.impl;

import cn.canlnac.course.dao.ProfileDao;
import cn.canlnac.course.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by cecil on 2016/9/20.
 */
@Transactional
@Component(value = "ProfileService")
public class ProfileServiceImpl implements ProfileService {
    @Autowired
    private ProfileDao profileDao;
}