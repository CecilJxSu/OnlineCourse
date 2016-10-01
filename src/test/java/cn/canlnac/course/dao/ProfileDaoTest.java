package cn.canlnac.course.dao;

import cn.canlnac.course.entity.Profile;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by can on 2016/10/1.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-context.xml"})
@TransactionConfiguration(transactionManager="transactionManager", defaultRollback=true)
@Transactional
public class ProfileDaoTest {
    @Autowired
    private ProfileDao profileDao;

    @Test
    public void testCreate(){
        Profile profile = new Profile();
        profile.setUserId(38);
        profile.setUniversityId("1340101010");
        profile.setNickname("looser");
        int i = profileDao.create(profile);
        System.out.println(profile.toString());
    }

    @Test
    public void testUpdate(){
        testCreate();
        Profile profile = new Profile();
        profile.setUserId(38);
        profile.setUniversityId("1349393939");
        profile.setNickname("beautiful");
        int i = profileDao.update(profile);
        System.out.println(profileDao.findByUserID(38).toString());
    }

    @Test
    public void testFindByUserID(){
        testCreate();
        Profile profile = profileDao.findByUserID(38);
        System.out.println(profile.toString());
    }
}
