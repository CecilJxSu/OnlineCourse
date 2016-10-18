package cn.canlnac.course.dao;

import cn.canlnac.course.entity.Catalog;
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
public class CatalogDaoTest {
    @Autowired
    private CatalogDao catalogDao;

    @Test
    public void create(){
        int m = 0;
        for (int i=0;i<15;i++){
            Catalog catalog = new Catalog();
            catalog.setCourseId(1);
            catalog.setIndex(i+1);
            m += catalogDao.create(catalog);
        }
        assertEquals(15,m);
    }

    @Test
    public void update(){
        create();

        int id = catalogDao.getList(1).get(0).getId();
        Catalog catalog = new Catalog();
        catalog.setId(id);
        catalog.setCourseId(2);
        int m = catalogDao.update(catalog);
        assertEquals(1,m);
    }

    @Test
    public void getList(){
        create();

        List<Catalog> catalogs = catalogDao.getList(1);
        assertEquals(15,catalogs.size());
    }

    @Test
    public void delete(){
        create();

        int id = catalogDao.getList(1).get(0).getId();
        int m = catalogDao.delete(id);
        assertEquals(1,m);
    }
}
