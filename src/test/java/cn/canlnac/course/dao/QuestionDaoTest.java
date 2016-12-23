package cn.canlnac.course.dao;

import cn.canlnac.course.entity.Question;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by can on 2016/10/2.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-context.xml"})
@TransactionConfiguration(transactionManager="transactionManager", defaultRollback=true)
@Transactional
public class QuestionDaoTest {
    @Autowired
    private QuestionDao questionDao;

    @Test
    public void testCreate(){
        int n = 0;
        for (int i=1;i<=15;i++){
            Question question = new Question();
            question.setCatalogId(i);
            question.setQuestions("fsfjojojo");
            n += questionDao.create(question);
        }
        assertEquals(15, n);
    }

    @Test
    public void testUpdate(){
        testCreate();
        int id = questionDao.findByCatalogId(1).getId();

        Question question = new Question();
        question.setId(id);
        question.setQuestions("54fsdf");
        int i = questionDao.update(question);
        assertEquals(1,i);
    }


    @Test
    public void testDelete(){
        testCreate();
        int id = questionDao.findByCatalogId(1).getId();

        int i = questionDao.delete(id);
        assertEquals(1,i);
    }

    @Test
    public void testFindById(){
        testCreate();

        Question question1 = questionDao.findByCatalogId(1);
        Question question2 = questionDao.findById(question1.getId());
        System.out.println(question1.toString());
        System.out.println(question2.toString());
        //assertEquals(true,question2==question1);
    }

    @Test
    public void testFindByCatalogId(){
        testCreate();

        Question question = questionDao.findByCatalogId(1);
        System.out.println(question.toString());
    }
}


