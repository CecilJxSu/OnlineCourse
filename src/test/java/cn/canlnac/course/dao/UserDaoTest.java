package cn.canlnac.course.dao;

import cn.canlnac.course.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * Created by cecil on 2016/9/21.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-context.xml"})
@TransactionConfiguration(transactionManager="transactionManager", defaultRollback=true)
@Transactional
public class UserDaoTest {
    @Autowired
    private UserDao userDao;

    List<Integer> ids = new ArrayList<>();

    @Test
    public void testCreate() throws Exception {
        for (int i=1;i<=15;i++){
            User user = new User();
            switch (i%3){
                case 0:
                    user.setUserStatus("admin");
                    user.setUsername("zhangsans"+i);
                    break;
                case 1:
                    user.setUserStatus("student");
                    user.setUsername("lisi"+i);
                    break;
                case 2:
                    user.setUserStatus("teacher");
                    user.setUsername("wangwu"+i);
                    break;
            }
            user.setPassword("123456");
            int count = userDao.create(user);
            assertEquals(1, count);
            ids.add(user.getId());
        }
        System.out.println(ids.toString());
    }

    @Test
    public void testFindByID() throws Exception {
        testCreate();
        List<User> users = new ArrayList<>();
        for (int id:ids) {
            users.add(userDao.findByID(id));
        }
        System.out.println(users.toString());
    }

    @Test
    public void testFindByUsername() throws Exception {
        testCreate();

        User user = userDao.findByUsername("zhangsans3");
        assertEquals("zhangsans3",user.getUsername());
    }

    @Test
    public void testGetList() throws Exception {
        testCreate();

        Map<String,Object> map = new HashMap<>();
        //status数组，userStatus数组，username数组
        List<String> status = new ArrayList<>();//正常：active；封号：lock；永久封号：dead
        status.add("active");

        List<String> userStatus = new ArrayList<>();//学生：student；老师：teacher；管理员：admin
        userStatus.add("student");
        userStatus.add("teacher");
//        userStatus.add("admin");

        List<String> username = new ArrayList<>();//登录用户名zhangsan

        map.put("status",status);
        map.put("userStatus",userStatus);
//        map.put("username",username);

        List<User> users = userDao.getList(0,15,map);
        System.out.println(users.toString());
        assertEquals(10,users.size());
    }

    @Test
    public void testCount() throws Exception {
        testCreate();

        Map<String,Object> map = new HashMap<>();
        //status数组，userStatus数组，username数组
        List<String> status = new ArrayList<>();//正常：active；封号：lock；永久封号：dead
        status.add("active");

        List<String> userStatus = new ArrayList<>();//学生：student；老师：teacher；管理员：admin
        userStatus.add("student");
        userStatus.add("teacher");
//        userStatus.add("admin");

        List<String> username = new ArrayList<>();//登录用户名zhangsan

        map.put("status",status);
        map.put("userStatus",userStatus);
//        map.put("username",username);

        int count = userDao.count(map);
        assertEquals(10,count);
    }

    @Test
    public void testUpdate() throws Exception {
        testCreate();

        User user = userDao.findByUsername("lisi1");
        User userUP = new User();
        userUP.setId(user.getId());
        userUP.setPassword("654321");
        int i = userDao.update(userUP);
        assertEquals(1,i);
    }
}
