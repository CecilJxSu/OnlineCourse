package cn.canlnac.course.dao;

import cn.canlnac.course.entity.Message;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * Created by can on 2016/10/2.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-context.xml"})
@TransactionConfiguration(transactionManager="transactionManager", defaultRollback=true)
@Transactional
public class MessageDaoTest {
    @Autowired
    private MessageDao messageDao;

    @Test
    public void testCreate(){
        for (int i=1;i<=15;i++){
            Message message = new Message();
            message.setIsRead('N');
            message.setToUserId(1);
            message.setContent("fjosfoj467896sdtfhgudf");
            switch (i%5){
                case 0:
                    message.setType("course");
                    message.setFromUserId(i%5+1);
                    break;
                case 1:
                    message.setType("chat");
                    message.setFromUserId(i%5+1);
                    break;
                case 2:
                    message.setType("comment");
                    message.setFromUserId(i%5+1);
                    break;
                case 3:
                    message.setType("system");
                    break;
                case 4:
                    message.setType("user");
                    message.setFromUserId(i%5+1);
                    break;
            }
            messageDao.create(message);
//            System.out.println(message.getId());
        }
    }

    @Test
    public void testSetRead(){
        testCreate();

        List<Integer> ids = new ArrayList<>();
        for (Message m:messageDao.getMessages(0,20,1,"chat")) {
            ids.add(m.getId());
        }
        int m = messageDao.setRead(ids);
        assertEquals(3,m);
    }

    @Test
    public void testCountUnread(){
        testCreate();

        List<Map<String, Integer>> map = messageDao.countUnread(1);
        System.out.println(map.toString());
    }

    @Test
    public void testCount(){
        testCreate();

        int m = messageDao.count(1,"chat");
        assertEquals(3,m);
    }

    @Test
    public void tsetGetMessages(){
        testCreate();

        List<Message> messages = messageDao.getMessages(0,20,1,"chat");
        assertEquals(3,messages.size());
    }

    @Test
    public void testDelete(){
        testCreate();

        int i = messageDao.delete(messageDao.getMessages(0,20,1,"chat").get(1).getId());
        assertEquals(1,i);
    }
}
