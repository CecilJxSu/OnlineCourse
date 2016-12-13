package cn.canlnac.course.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * Created by can on 2016/12/13.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-context.xml"})
public class CatalogServiceTest {
    @Autowired
    private CatalogService catalogService;

    @Test
    public void getChapterAndSectionList(){
        List catalogs = catalogService.getChapterAndSectionList(1);
        System.out.println(catalogs.toString());
    }
}
