package cn.canlnac.course.dao;

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
 * Created by can on 2016/10/1.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-context.xml"})
@TransactionConfiguration(transactionManager="transactionManager", defaultRollback=true)
@Transactional
public class WatchDaoTest {
    @Autowired
    private WatchDao watchDao;

    @Test
    public void testCreate(){
        for (int i=1;i<=15;i++){
            switch (i%3){
                case 0:
                    watchDao.create("course",i,39);
                    break;
                case 1:
                    watchDao.create("chat",i,39);
                    break;
                case 2:
                    watchDao.create("document",i,39);
                    break;
            }
        }
    }

    @Test
    public void testDelete(){
        testCreate();

        int i = watchDao.delete("chat",1,39);
        assertEquals(1,i);
    }

    @Test
    public void testCount(){
        testCreate();

        int i = watchDao.count("chat",1);
        assertEquals(1,i);
    }

    @Test
    public void testGetUsers(){
        List<Profile> list = watchDao.getUsers(0,20,"chat",7);
        System.out.println(list.toString());
    }

    @Test
    public void isWatch(){
        testCreate();

        int i = watchDao.isWatch(39,"chat",1);
        assertEquals(1,i);
    }
}
