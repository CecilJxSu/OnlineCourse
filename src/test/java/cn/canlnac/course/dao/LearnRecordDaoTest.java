package cn.canlnac.course.dao;

import cn.canlnac.course.entity.LearnRecord;
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
public class LearnRecordDaoTest {
    @Autowired
    private LearnRecordDao learnRecordDao;

    @Test
    public void testCreate(){
        int m = 0;
        for (int i=1;i<=15;i++){
            LearnRecord learnRecord = new LearnRecord();
            learnRecord.setCatalogId(i);
            learnRecord.setUserId(1);
            learnRecord.setProgress(0.25);
            learnRecord.setLastPosition(50);
            m += learnRecordDao.create(learnRecord);
        }
        assertEquals(15,m);
    }

    @Test
    public void testUpdate(){
        testCreate();

        int id = learnRecordDao.getLearnRecord(1,1).getId();
        LearnRecord learnRecord = new LearnRecord();
        learnRecord.setId(id);
        int i = learnRecordDao.update(learnRecord);
        assertEquals(1,i);
    }

    @Test
    public void testGetLearnRecord(){
        testCreate();

        LearnRecord learnRecord = learnRecordDao.getLearnRecord(1,1);
        System.out.println(learnRecord.toString());
    }

    @Test
    public void testCount(){
        testCreate();

        int count = learnRecordDao.count(1);
        assertEquals(15,count);
    }

    @Test
    public void testGetLearnRecords(){
        testCreate();

        List<LearnRecord> learnRecords = learnRecordDao.getLearnRecords(0,20,1);
        assertEquals(15,learnRecords.size());
    }

}
