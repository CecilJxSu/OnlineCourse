package cn.canlnac.course.dao;

import cn.canlnac.course.entity.Reply;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;

/**
 * Created by can on 2016/10/2.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-context.xml"})
@TransactionConfiguration(transactionManager="transactionManager", defaultRollback=true)
@Transactional
public class ReplyDaoTest {
    @Autowired
    private ReplyDao replyDao;

    @Test
    public void testCreate(){
        for (int i=1;i<=15;i++){
            Reply reply = new Reply();
            Random rd = new Random();
            reply.setCommentId(rd.nextInt(2)+1);//1、2之间的随机数
            reply.setContent("fsfojjfsjdlfj");
            if (i%2!=0){
                reply.setUserId(1);
                reply.setToUserId(2);
            } else {
                reply.setUserId(2);
                reply.setToUserId(1);
            }
            replyDao.create(reply);
            /*System.out.println(reply.getId());*/
        }
    }

    @Test
    public void testGetReplies(){
        testCreate();

        List<Reply> list1 = replyDao.getReplies(1);
        List<Reply> list2 = replyDao.getReplies(2);
        assertEquals(15,list1.size()+list2.size());
        /*for (Reply reply:list1) {
            System.out.println(reply.toString());
        }
        for (Reply reply:list2) {
            System.out.println(reply.toString());
        }*/
    }

    @Test
    public void testGetOwnReplies(){
        testCreate();

        List<Reply> list1 = replyDao.getOwnReplies(1);
        List<Reply> list2 = replyDao.getOwnReplies(2);
        assertEquals(15,list1.size()+list2.size());
        /*for (Reply reply:list1) {
            System.out.println(reply.toString());
        }
        for (Reply reply:list2) {
            System.out.println(reply.toString());
        }*/
    }

    @Test
    public void testDelete(){
        testCreate();
        int id = replyDao.getReplies(1).get(1).getId();

        int i = replyDao.delete(id);
        assertEquals(1,i);
    }
}
