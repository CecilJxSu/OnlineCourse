package cn.canlnac.course.dao;

import cn.canlnac.course.entity.Chat;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by can on 2016/9/24.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-context.xml"})
public class ChatDaoTest {

    @Autowired
    private ChatDao chatDao;

    @Test
    public void testGetById(){
        Chat chat = chatDao.getById(1);
        System.out.println("==================================================="+chat.toString());
    }
}
