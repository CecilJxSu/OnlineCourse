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
//    @Autowired
//    private QuestionDao questionDao;
//
//    @Test
//    public void testCreate(){
//        List<Question> questions = new ArrayList<>();
//        for (int i=1;i<=15;i++){
//            Question question = new Question();
//            question.setCatalogId(1);
//            question.setIndex(i);
//            int type = (i%3)+1;
//            question.setType(type);
//            question.setQuestion("jjsdfjlsjfjlfjsdf");
//            question.setAnswer("jojfosf,mjfsdjfmm,fsfjl");
//            questions.add(question);
//        }
//        int n = questionDao.create(questions);
//        assertEquals(15, n);
//    }
//
//    @Test
//    public void testUpdate(){
//        testCreate();
//        int id = questionDao.getQuestions(1).get(1).getId();
//
//        Question question = new Question();
//        question.setId(id);
//        question.setType(10);
//        int i = questionDao.update(question);
//        assertEquals(1,i);
//    }
//
//    @Test
//    public void testGetQuestions(){
//        testCreate();
//
//        List<Question> questions = questionDao.getQuestions(1);
//        assertEquals(15,questions.size());
//    }
//
//    @Test
//    public void testDelete(){
//        testCreate();
//        int id = questionDao.getQuestions(1).get(1).getId();
//
//        int i = questionDao.delete(id);
//        assertEquals(1,i);
//    }
}


