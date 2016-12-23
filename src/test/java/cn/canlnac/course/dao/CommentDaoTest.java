package cn.canlnac.course.dao;

import cn.canlnac.course.entity.Comment;
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
 * Created by can on 2016/10/4.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-context.xml"})
@TransactionConfiguration(transactionManager="transactionManager", defaultRollback=true)
@Transactional
public class CommentDaoTest {
    @Autowired
    private CommentDao commentDao;

    @Test
    public void create(){
        int m = 0;
        for (int i=1;i<=16;i++){
            Comment comment = new Comment();
            comment.setTargetType(i%2==1?"course":"comment");
            comment.setTargetId(i);
            comment.setUserId(1);
            comment.setContent("joifo55fsjhfgsd");
            m += commentDao.create(comment);
        }
        assertEquals(16,m);
    }


    @Test
    public void findByID(){
        create();

        int id = commentDao.getList(0,20,"rank","comment",2).get(0).getId();
        Comment comment = commentDao.findByID(id);
        System.out.println(comment.toString());
    }

    @Test
    public void count(){
        create();

        int count = commentDao.count("comment",2);
        assertEquals(1,count);
    }

    @Test
    public void getList(){
        create();

        List<Comment> comments = commentDao.getList(0,20,"rank","comment",2);
        assertEquals(1,comments.size());
    }

    @Test
    public void delete(){
        create();

        int id = commentDao.getList(0,20,"rank","comment",2).get(0).getId();
        int m = commentDao.delete(id);
        assertEquals(1,m);
    }

    @Test
    public void update(){
        create();

        int id = commentDao.getList(0,20,"rank","comment",2).get(0).getId();
        Comment comment1 = new Comment();
        comment1.setId(id);
        comment1.setReplyCount(1000);
        int i = commentDao.update(comment1);
        assertEquals(1,i);
    }
}
