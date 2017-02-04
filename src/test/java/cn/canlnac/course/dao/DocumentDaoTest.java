package cn.canlnac.course.dao;

import cn.canlnac.course.entity.Document;
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
public class DocumentDaoTest {
    @Autowired
    private DocumentDao documentDao;

    @Test
    public void testCreate(){
        int m = 0;
        for (int i=1;i<=15;i++){
            Document document = new Document();
            document.setTargetType("course");
            document.setTargetId(i);
            document.setUrl("jdfisjfos");
            document.setType("sdlfs");
            document.setSize(155+i);
            document.setName("name"+i);
            m += documentDao.create(document);
        }
        assertEquals(15,m);
    }

    @Test
    public void testFinById(){
        testCreate();

        int id = documentDao.getDocuments(0,20,"date","course",1,null).get(0).getId();
        Document document = documentDao.findByID(id);
        System.out.println(document.toString());
    }

    @Test
    public void testCount(){
        testCreate();

        int count = documentDao.count("course",1,null);
        assertEquals(1,count);
    }

    @Test
    public void testGetDocuments(){
        testCreate();

        List<Document> list = documentDao.getDocuments(0,20,"size","course",1,null);
        assertEquals(1,list.size());
        System.out.println(list.toString());
    }

    @Test
    public void testDelete(){
        testCreate();

        int id = documentDao.getDocuments(0,20,"date","course",1,null).get(0).getId();
        int i = documentDao.delete(id);
        assertEquals(1,i);
    }
}
