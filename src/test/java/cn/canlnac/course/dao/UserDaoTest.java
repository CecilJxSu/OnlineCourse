package cn.canlnac.course.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;

/**
 * Created by cecil on 2016/9/21.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-context.xml"})
public class UserDaoTest {
    @Autowired
    private UserDao userDao;

    @Test
    public void testCreate() throws Exception {
        int count = userDao.create("123","123","student");
        assertEquals(1, count);
    }
}
