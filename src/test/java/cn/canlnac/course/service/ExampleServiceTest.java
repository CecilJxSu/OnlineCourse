package cn.canlnac.course.service;

import cn.canlnac.course.entity.Example;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by can on 2016/9/10.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-context.xml"})
public class ExampleServiceTest {
    @Autowired
    protected ExampleService exampleService;

    @Test
    public void getAll(){
        List<Example> list = exampleService.getAll();
        assertEquals(0,list.size());
        System.out.println(list.toString());
    }

}
