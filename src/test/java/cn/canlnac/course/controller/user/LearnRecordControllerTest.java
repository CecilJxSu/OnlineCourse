package cn.canlnac.course.controller.user;

import cn.canlnac.course.entity.Catalog;
import cn.canlnac.course.entity.Course;
import cn.canlnac.course.entity.LearnRecord;
import cn.canlnac.course.entity.Profile;
import cn.canlnac.course.service.CatalogService;
import cn.canlnac.course.service.CourseService;
import cn.canlnac.course.service.LearnRecordService;
import cn.canlnac.course.service.ProfileService;
import cn.canlnac.course.util.JWT;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

/**
 * 用户学习记录测试用例
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-context.xml"})
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
public class LearnRecordControllerTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    private String url;

    @MockBean
    private LearnRecordService learnRecordService;
    @MockBean
    private CourseService courseService;
    @MockBean
    private CatalogService catalogService;
    @MockBean
    private ProfileService profileService;
    @MockBean
    JWT jwt;

    /**
     * 获取用户学习记录，未登录，应该返回401
     */
    @Test
    public void shouldReturn401WhenGetLearnRecord() {
        url = "http://localhost:"+port+"/user/1/learnRecord";

        //创建body
        Map<String,Object> body = new HashMap();

        //创建headers
        MultiValueMap<String,String> requestHeaders = new LinkedMultiValueMap<>();
        //设置headers
        requestHeaders.add("Content-Type","application/json; charset=UTF-8");
        //头部登录信息
        requestHeaders.add("Authentication","123asd");

        //设置http请求数据
        HttpEntity<?> httpEntity = new HttpEntity<Object>(body, requestHeaders);

        //Mock jwt，解码json web token
        when(jwt.decode(any())).thenReturn(null);

        //发起请求
        ResponseEntity response = this.restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class );

        //返回状态码应该等于401
        assertEquals(401,response.getStatusCode().value());
    }

    /**
     * 获取用户学习记录，没有学习记录，应该返回404
     */
    @Test
    public void shouldReturn404WhenGetLearnRecord() {
        url = "http://localhost:"+port+"/user/1/learnRecord";

        //创建body
        Map<String,Object> body = new HashMap();

        //创建headers
        MultiValueMap<String,String> requestHeaders = new LinkedMultiValueMap<>();
        //设置headers
        requestHeaders.add("Content-Type","application/json; charset=UTF-8");
        //头部登录信息
        requestHeaders.add("Authentication","123asd");

        //设置http请求数据
        HttpEntity<?> httpEntity = new HttpEntity<Object>(body, requestHeaders);

        Map auth = new HashMap();
        //Mock jwt，解码json web token
        when(jwt.decode(any())).thenReturn(auth);

        when(learnRecordService.count(1)).thenReturn(0);    //统计学习记录数目

        //发起请求
        ResponseEntity response = this.restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class );

        //返回状态码应该等于404
        assertEquals(404,response.getStatusCode().value());
    }

    /**
     * 获取用户学习记录，获取自己的记录，应该返回200
     */
    @Test
    public void shouldReturn200WhenGetLearnRecord() {
        url = "http://localhost:"+port+"/user/1/learnRecord?start=0&count=10";

        //创建body
        Map<String,Object> body = new HashMap();

        //创建headers
        MultiValueMap<String,String> requestHeaders = new LinkedMultiValueMap<>();
        //设置headers
        requestHeaders.add("Content-Type","application/json; charset=UTF-8");
        //头部登录信息
        requestHeaders.add("Authentication","123asd");

        //设置http请求数据
        HttpEntity<?> httpEntity = new HttpEntity<Object>(body, requestHeaders);

        Map auth = new HashMap();
        auth.put("id",1);
        //Mock jwt，解码json web token
        when(jwt.decode(any())).thenReturn(auth);

        when(learnRecordService.count(1)).thenReturn(1);    //统计学习记录数目

        //学习记录
        LearnRecord learnRecord = new LearnRecord();
        learnRecord.setCatalogId(2);
        learnRecord.setUserId(1);
        List learnRecords = new ArrayList();
        learnRecords.add(learnRecord);
        when(learnRecordService.getLearnRecords(0,10,1)).thenReturn(learnRecords);

        when(profileService.findByUserID(1)).thenReturn(new Profile()); //返回用户资料
        when(catalogService.findByID(2)).thenReturn(new Catalog());     //返回章节

        //发起请求
        ResponseEntity response = this.restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class );

        //返回状态码应该等于200
        assertEquals(200,response.getStatusCode().value());

        //响应数据
        JSONObject responseBody = new JSONObject(response.getBody().toString());

        assertEquals(1,responseBody.get("total"));  //1条记录
    }

    /**
     * 获取别人的记录，不是老师，应该返回403
     */
    @Test
    public void shouldReturn403WhenGetOtherUserLearnRecord() {
        url = "http://localhost:"+port+"/user/2/learnRecord?start=0&count=10";

        //创建body
        Map<String,Object> body = new HashMap();

        //创建headers
        MultiValueMap<String,String> requestHeaders = new LinkedMultiValueMap<>();
        //设置headers
        requestHeaders.add("Content-Type","application/json; charset=UTF-8");
        //头部登录信息
        requestHeaders.add("Authentication","123asd");

        //设置http请求数据
        HttpEntity<?> httpEntity = new HttpEntity<Object>(body, requestHeaders);

        Map auth = new HashMap();
        auth.put("userStatus","student");
        auth.put("id",1);
        //Mock jwt，解码json web token
        when(jwt.decode(any())).thenReturn(auth);

        when(learnRecordService.count(2)).thenReturn(1);    //统计学习记录数目

        //发起请求
        ResponseEntity response = this.restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class );

        //返回状态码应该等于403
        assertEquals(403,response.getStatusCode().value());
    }

    /**
     * 获取别人的记录，没有与之对应的学习记录，应该返回404
     */
    @Test
    public void shouldReturn404WhenGetOtherUserLearnRecord() {
        url = "http://localhost:"+port+"/user/2/learnRecord?start=2&count=10";

        //创建body
        Map<String,Object> body = new HashMap();

        //创建headers
        MultiValueMap<String,String> requestHeaders = new LinkedMultiValueMap<>();
        //设置headers
        requestHeaders.add("Content-Type","application/json; charset=UTF-8");
        //头部登录信息
        requestHeaders.add("Authentication","123asd");

        //设置http请求数据
        HttpEntity<?> httpEntity = new HttpEntity<Object>(body, requestHeaders);

        Map auth = new HashMap();
        auth.put("userStatus","teacher");
        auth.put("id",1);
        //Mock jwt，解码json web token
        when(jwt.decode(any())).thenReturn(auth);

        when(learnRecordService.count(2)).thenReturn(2);    //统计学习记录数目

        //获取登录用户的课程
        Course course = new Course();
        course.setId(3);
        List courses = new ArrayList();
        courses.add(course);
        when(courseService.findByUserId(1)).thenReturn(courses);

        //获取所有章节
        Catalog catalog = new Catalog();
        catalog.setId(4);
        List catalogs = new ArrayList();
        catalogs.add(catalog);
        when(catalogService.getList(3)).thenReturn(catalogs);

        //获取用户的所有学习记录
        LearnRecord learnRecord1 = new LearnRecord();
        learnRecord1.setId(4);
        learnRecord1.setCatalogId(4);

        LearnRecord learnRecord2 = new LearnRecord();
        learnRecord2.setId(5);
        learnRecord2.setCatalogId(5);

        List learnRecords = new ArrayList();
        learnRecords.add(learnRecord1);
        learnRecords.add(learnRecord2);
        when(learnRecordService.getLearnRecords(0,2,2)).thenReturn(learnRecords);

        //发起请求
        ResponseEntity response = this.restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class );

        //返回状态码应该等于404
        assertEquals(404,response.getStatusCode().value());
    }

    /**
     * 获取别人的记录，应该返回200
     */
    @Test
    public void shouldReturn200WhenGetOtherUserLearnRecord() {
        url = "http://localhost:"+port+"/user/2/learnRecord?start=0&count=10";

        //创建body
        Map<String,Object> body = new HashMap();

        //创建headers
        MultiValueMap<String,String> requestHeaders = new LinkedMultiValueMap<>();
        //设置headers
        requestHeaders.add("Content-Type","application/json; charset=UTF-8");
        //头部登录信息
        requestHeaders.add("Authentication","123asd");

        //设置http请求数据
        HttpEntity<?> httpEntity = new HttpEntity<Object>(body, requestHeaders);

        Map auth = new HashMap();
        auth.put("userStatus","teacher");
        auth.put("id",1);
        //Mock jwt，解码json web token
        when(jwt.decode(any())).thenReturn(auth);

        when(learnRecordService.count(2)).thenReturn(2);    //统计学习记录数目

        //获取登录用户的课程
        Course course = new Course();
        course.setId(3);
        List courses = new ArrayList();
        courses.add(course);
        when(courseService.findByUserId(1)).thenReturn(courses);

        //获取所有章节
        Catalog catalog = new Catalog();
        catalog.setId(4);
        List catalogs = new ArrayList();
        catalogs.add(catalog);
        when(catalogService.getList(3)).thenReturn(catalogs);

        //获取用户的所有学习记录
        LearnRecord learnRecord1 = new LearnRecord();
        learnRecord1.setId(4);
        learnRecord1.setCatalogId(4);
        learnRecord1.setUserId(2);

        LearnRecord learnRecord2 = new LearnRecord();
        learnRecord2.setId(5);
        learnRecord2.setCatalogId(5);

        List learnRecords = new ArrayList();
        learnRecords.add(learnRecord1);
        learnRecords.add(learnRecord2);
        when(learnRecordService.getLearnRecords(0,2,2)).thenReturn(learnRecords);

        when(profileService.findByUserID(2)).thenReturn(new Profile()); //返回用户资料
        when(catalogService.findByID(4)).thenReturn(new Catalog());     //返回章节

        //发起请求
        ResponseEntity response = this.restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class );

        //返回状态码应该等于200
        assertEquals(200,response.getStatusCode().value());

        //响应数据
        JSONObject responseBody = new JSONObject(response.getBody().toString());

        assertEquals(1,responseBody.get("total"));  //1条记录

        //学习记录
        JSONObject learnRecord = new JSONObject(((JSONArray)responseBody.get("learnRecords")).get(0).toString());

        assertEquals(4,learnRecord.get("id"));  //学习记录ID
    }
}