package cn.canlnac.course.dao;

import cn.canlnac.course.entity.Chat;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
/**
 * Created by can on 2016/10/3.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-context.xml"})
@TransactionConfiguration(transactionManager="transactionManager", defaultRollback=true)
@Transactional
public class ChatDaoTest {

    @Autowired
    private ChatDao chatDao;


    @Test
    public void create(){
        int m = 0;
        for (int i=1;i<=15;i++){
            Chat chat = new Chat();
            chat.setTitle("kjfsd"+i);
            chat.setUserId(1);
            chat.setContent("josjflsdf");
            m += chatDao.create(chat);
        }
        assertEquals(15,m);
    }

    @Test
    public void findByID(){
        create();
        Map<String,Object> map = new HashMap();
        map.put("userId",1);
        int id = chatDao.getList(0,20,null,map).get(0).getId();

        Chat c = chatDao.findByID(id);
        System.out.println(c.toString());
    }

    @Test
    public void count(){
        create();

        Map<String,Object> map = new HashMap();
        map.put("userId",1);
        int count = chatDao.count(map);
        assertEquals(15,count);
    }

    @Test
    public void getList(){
        create();

        Map<String,Object> map = new HashMap();
        map.put("userId",1);
        List<Chat> chats = chatDao.getList(0,20,null,map);
        assertEquals(15,chats.size());
    }

    @Test
    public void delete(){
        create();
        Map<String,Object> map = new HashMap();
        map.put("userId",1);
        int id = chatDao.getList(0,20,null,map).get(0).getId();

        int i = chatDao.delete(id);
        assertEquals(1,i);
    }

    @Test
    public void update(){
        create();

        Map<String,Object> map = new HashMap();
        map.put("userId",1);
        int id = chatDao.getList(0,20,null,map).get(0).getId();
        Chat chat = new Chat();
        chat.setId(id);
        chat.setWatchCount(3);
        int i = chatDao.update(chat);
        assertEquals(1,i);
    }
}
