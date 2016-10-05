package cn.canlnac.course.dao;

import cn.canlnac.course.entity.Answer;
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
 * Created by can on 2016/10/4.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-context.xml"})
@TransactionConfiguration(transactionManager="transactionManager", defaultRollback=true)
@Transactional
public class AnswerDaoTest {
    @Autowired
    private AnswerDao answerDao;
    @Autowired
    private QuestionDao questionDao;

    private int questionId = 1;
    @Test
    public void create(){
        int m = 0;
        for (int i=1;i<=15;i++){
            Answer answer = new Answer();
            answer.setQuestionId(questionId);
            answer.setUserId(i);
            answer.setAnswer("{ index: 1, answer: A,B }");
            m += answerDao.create(answer);
        }
        assertEquals(15,m);
    }


    @Test
    public void update(){
        create();

        int id = answerDao.getAnswers(0,20,15).get(0).getId();
        Answer answer = new Answer();
        answer.setId(id);
        answer.setTotal(50);
        int m = answerDao.update(answer);
        assertEquals(1,m);
    }


    @Test
    public void getAnswer(){
        List<Question> questions = new ArrayList<>();
        Question question = new Question();
        question.setCatalogId(1);
        question.setIndex(1);
        question.setType(1);
        question.setQuestion("jjsdfjlsjfjlfjsdf");
        question.setAnswer("jojfosf,mjfsdjfmm,fsfjl");
        questions.add(question);
        questionDao.create(questions);
        questionId = questionDao.getQuestions(1).get(0).getId();
        create();

        Answer answer = answerDao.getAnswer(1,1);
        System.out.println(answer.toString());
    }


    @Test
    public void count(){
        create();

        int count = answerDao.count(1);
        assertEquals(1,count);
    }


    @Test
    public void getAnswers(){
        create();

        List<Answer> answers = answerDao.getAnswers(0,20,1);
        assertEquals(1,answers.size());
    }
}
