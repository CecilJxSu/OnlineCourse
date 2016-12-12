package cn.canlnac.course.dao;

import cn.canlnac.course.entity.Course;
import org.apache.ibatis.annotations.Param;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
/**
 * Created by can on 2016/10/3.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-context.xml"})
@TransactionConfiguration(transactionManager="transactionManager", defaultRollback=true)
@Transactional
public class CourseDaoTest {
    @Autowired
    private CourseDao courseDao;

    private int pubId;

    @Test
    public void testCreate(){
        int m = 0;
        for (int i = 1;i<=18;i++){
            Course course = new Course();
            course.setUserId(1);
            course.setName("name"+i);
            course.setDepartment(i%2==1?"计算机系":"外语系");
            switch (i%3){
                case 0:
                    course.setStatus("public");
                    break;
                case 1:
                    course.setStatus("draft");
                    break;
                case 2:
                    course.setStatus("delete");
                    break;
            }
            m += courseDao.create(course);
            pubId = course.getId();
        }
        assertEquals(18,m);
    }

    @Test
    public void testUpdate(){
        testCreate();

        Course course = new Course();
        course.setId(pubId);
        course.setStatus("public");
        course.setDepartment("管理系");
        course.setLikeCount(2);
        int m = courseDao.update(course);
        assertEquals(1,m);
    }

    @Test
    public void testFindByID(){
        testCreate();

        Course course = courseDao.findByID(pubId);
        System.out.println(course.toString());
    }

    @Test
    public void testCount(){
        testCreate();

        String[] status = new String[2];
        status[0] = "public";
        status[1] = "draft";

        int userId = 1;

        String[] department = new String[2];
        department[0] = "计算机系";
        //department[1] = "外语系";

        Map<String,Object> map = new HashMap();
        map.put("status",status);
        map.put("userId",userId);
        map.put("department",department);
        int count = courseDao.count(map);
        System.out.println(count);
    }

    @Test
    public void testGetList(){
        testCreate();

        String[] status = new String[2];
        status[0] = "public";
        status[1] = "draft";

        int userId = 1;

        String[] department = new String[2];
        department[0] = "计算机系";
        department[1] = "外语系";

        Map<String,Object> map = new HashMap();
        map.put("status",status);
        map.put("userId",userId);
        map.put("department",department);

        List<Course> courses = courseDao.getList(0,20,"rank",map);
        assertEquals(12,courses.size());
    }

    @Test
    public void testDelete(){
        testCreate();

        int i = courseDao.delete(pubId);
        assertEquals(1,i);
    }

    @Test
    public void findByUserId(){
        testCreate();

        List<Course> list = courseDao.findByUserId(1);
        assertEquals(18,list.size());
    }

    @Test
    public void getNumOfPeople(){
        int count = courseDao.getNumOfPeople(3);
        assertEquals(0,count);
    }
}
