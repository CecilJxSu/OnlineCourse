package cn.canlnac.course.dao;

import cn.canlnac.course.entity.Favorite;
import cn.canlnac.course.entity.Profile;
import org.junit.Before;
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
 * Created by can on 2016/10/2.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-context.xml"})
@TransactionConfiguration(transactionManager="transactionManager", defaultRollback=true)
@Transactional
public class FavoriteDaoTest {
    @Autowired
    private FavoriteDao favoriteDao;
    @Autowired
    private ProfileDao profileDao;

    @Test
    public void testCreate(){
        for (int i=1;i<=5;i++){
            int n = favoriteDao.create("course",1,i);
            int m = favoriteDao.create("chat",1,i);
            System.out.println(n+":"+m);
        }
    }

    @Test
    public void testDelete(){
        for (int i=1;i<=5;i++){
            Profile profile = new Profile();
            profile.setUserId(i);
            profile.setUniversityId("134070610"+i);
            profile.setNickname("134070610"+i);
            profileDao.create(profile);
            favoriteDao.create("course",1,i);
            favoriteDao.create("chat",1,i);
        }

        int count1 = favoriteDao.count("chat",1);
        favoriteDao.delete("chat",1,1);
        int count2 = favoriteDao.count("chat",1);
        assertEquals(count1,count2+1);
    }

    @Test
    public void testCount(){
        for (int i=1;i<=5;i++){
            int n = favoriteDao.create("course",1,i);
            int m = favoriteDao.create("chat",1,i);
            System.out.println(n+":"+m);
        }

        int count = favoriteDao.count("chat",1);
        assertEquals(5,count);
    }

    @Test
    public void testGetUsers(){
        for (int i=1;i<=5;i++){
            Profile profile = new Profile();
            profile.setUserId(i);
            profile.setUniversityId("134070610"+i);
            profile.setNickname("134070610"+i);
            profileDao.create(profile);
            int n = favoriteDao.create("course",1,i);
            int m = favoriteDao.create("chat",1,i);
        }

        List<Profile> list = favoriteDao.getUsers(0,20,"chat",1);
        System.out.println(list.toString());
        assertEquals(5,list.size());
    }

    @Test
    public void isFavorite(){
        testCreate();

        int i = favoriteDao.isFavorite(2,"chat",1);
        assertEquals(1,i);
    }
}
