package cn.canlnac.course.dao;

import cn.canlnac.course.entity.Favorite;
import cn.canlnac.course.entity.Profile;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by can on 2016/10/3.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-context.xml"})
@TransactionConfiguration(transactionManager="transactionManager", defaultRollback=true)
@Transactional
public class FollowingDaoTest {
    @Autowired
    private FollowingDao followingDao;
    @Autowired
    private ProfileDao profileDao;

    @Test
    public void testCreate(){
        for (int i=1;i<=30;i++){
            Profile profile = new Profile();
            profile.setUserId(i);
            profile.setUniversityId("134010101"+i);
            profile.setNickname("looser"+i);
            profileDao.create(profile);
        }

        int n=0,m=0;
        for (int i = 16;i<=30;i++){
            n += followingDao.create(1,i);
            m += followingDao.create(i,2);
        }
        assertEquals(n,m);
    }

    @Test
    public void testDelete(){
        testCreate();

        int i = followingDao.delete(1,16);
        assertEquals(1,i);
    }

    @Test
    public void testCountFollower(){
        testCreate();

        int count = followingDao.countFollower(1);
        assertEquals(15,count);
    }

    @Test
    public void testGetFollowerUsers(){
        testCreate();

        List<Profile> list = followingDao.getFollowerUsers(0,20,1);
        assertEquals(15,list.size());
    }

    @Test
    public void testCountFollowing(){
        testCreate();

        int count = followingDao.countFollowing(2);
        assertEquals(15,count);
    }

    @Test
    public void testGetFollowingUsers(){
        testCreate();

        List<Profile> list = followingDao.getFollowingUsers(0,20,2);
        assertEquals(15,list.size());
    }

    @Test
    public  void testIsFollowing(){
        testCreate();

        int re = followingDao.isFollowing(16,1);
        assertEquals(1,re);
    }
}
