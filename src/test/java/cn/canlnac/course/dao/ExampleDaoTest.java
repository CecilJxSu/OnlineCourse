package cn.canlnac.course.dao;

import cn.canlnac.course.entity.Example;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by can on 2016/9/10.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-context.xml"})
public class ExampleDaoTest {
    @Autowired
    private ExampleDao exampleDao;


    @Test
    public void testSelectAll() throws Exception {
        System.out.println(exampleDao);
        List<Example> example = exampleDao.selectAll();
        assertEquals(0,example.size());
    }
}
