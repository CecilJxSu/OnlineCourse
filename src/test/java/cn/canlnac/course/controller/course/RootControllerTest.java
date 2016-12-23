package cn.canlnac.course.controller.course;

import cn.canlnac.course.entity.Course;
import cn.canlnac.course.entity.Profile;
import cn.canlnac.course.service.*;
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

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

/**
 * 课程测试用例
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-context.xml"})
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
public class RootControllerTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    private String url;

    @MockBean
    CourseService courseService;
    @MockBean
    LikeService likeService;
    @MockBean
    FavoriteService favoriteService;
    @MockBean
    WatchService watchService;
    @MockBean
    ProfileService profileService;
    @MockBean
    JWT jwt;

    /**
     * 创建课程，未登录，应该返回401
     */
    @Test
    public void shouldReturn401WhenCreateCourse() {
        url = "http://localhost:"+port+"/course";

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
        ResponseEntity response = this.restTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class );

        //返回状态码应该等于401
        assertEquals(401,response.getStatusCode().value());
    }

    /**
     * 创建课程，不是老师，应该返回403
     */
    @Test
    public void shouldReturn403WhenCreateCourse() {
        url = "http://localhost:"+port+"/course";

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

        Map<String,Object> auth = new HashMap();
        auth.put("userStatus","student");
        //Mock jwt，解码json web token
        when(jwt.decode(any())).thenReturn(auth);

        //发起请求
        ResponseEntity response = this.restTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class );

        //返回状态码应该等于403
        assertEquals(403,response.getStatusCode().value());
    }

    /**
     * 创建课程，参数错误，应该返回400
     */
    @Test
    public void shouldReturn400WhenCreateCourse() {
        url = "http://localhost:"+port+"/course";

        //创建body
        Map<String,Object> body = new HashMap();
        body.put("name","Java");
        body.put("introduction","Java");

        //创建headers
        MultiValueMap<String,String> requestHeaders = new LinkedMultiValueMap<>();
        //设置headers
        requestHeaders.add("Content-Type","application/json; charset=UTF-8");
        //头部登录信息
        requestHeaders.add("Authentication","123asd");

        //设置http请求数据
        HttpEntity<?> httpEntity = new HttpEntity<Object>(body, requestHeaders);

        Map<String,Object> auth = new HashMap();
        auth.put("userStatus","teacher");
        //Mock jwt，解码json web token
        when(jwt.decode(any())).thenReturn(auth);

        //发起请求
        ResponseEntity response = this.restTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class );

        //返回状态码应该等于400
        assertEquals(400,response.getStatusCode().value());
    }

    /**
     * 创建课程，创建成功，应该返回200和课程ID
     */
    @Test
    public void shouldReturn200WhenCreateCourse() {
        url = "http://localhost:"+port+"/course";

        //创建body
        Map<String,Object> body = new HashMap();
        body.put("name","Java");
        body.put("introduction","Java");
        body.put("department","Java");

        //创建headers
        MultiValueMap<String,String> requestHeaders = new LinkedMultiValueMap<>();
        //设置headers
        requestHeaders.add("Content-Type","application/json; charset=UTF-8");
        //头部登录信息
        requestHeaders.add("Authentication","123asd");

        //设置http请求数据
        HttpEntity<?> httpEntity = new HttpEntity<Object>(body, requestHeaders);

        Map<String,Object> auth = new HashMap();
        auth.put("userStatus","teacher");
        auth.put("id",1);
        //Mock jwt，解码json web token
        when(jwt.decode(any())).thenReturn(auth);

        //创建课程
        when(courseService.create(any())).thenReturn(1);

        //发起请求
        ResponseEntity response = this.restTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class );

        //返回状态码应该等于200
        assertEquals(200,response.getStatusCode().value());

        //返回课程ID
        assertEquals("{\"courseId\":0}",response.getBody());
    }

    /**
     * 更新课程，未登录，应该返回401
     */
    @Test
    public void shouldReturn401WhenUpdateCourse() {
        url = "http://localhost:"+port+"/course/1";

        //创建body
        Map<String,Object> body = new HashMap();
        body.put("name","Java");
        body.put("introduction","Java");

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
        ResponseEntity response = this.restTemplate.exchange(url, HttpMethod.PUT, httpEntity, String.class );

        //返回状态码应该等于401
        assertEquals(401,response.getStatusCode().value());
    }

    /**
     * 更新课程，课程不存在，应该返回404
     */
    @Test
    public void shouldReturn404WhenUpdateCourse() {
        url = "http://localhost:"+port+"/course/1";

        //创建body
        Map<String,Object> body = new HashMap();
        body.put("name","Java");
        body.put("introduction","Java");
        body.put("department","Java");

        //创建headers
        MultiValueMap<String,String> requestHeaders = new LinkedMultiValueMap<>();
        //设置headers
        requestHeaders.add("Content-Type","application/json; charset=UTF-8");
        //头部登录信息
        requestHeaders.add("Authentication","123asd");

        //设置http请求数据
        HttpEntity<?> httpEntity = new HttpEntity<Object>(body, requestHeaders);

        Map<String,Object> auth = new HashMap();
        auth.put("userStatus","teacher");
        auth.put("id",1);
        //Mock jwt，解码json web token
        when(jwt.decode(any())).thenReturn(auth);

        //查找课程
        when(courseService.findByID(1)).thenReturn(null);

        //发起请求
        ResponseEntity response = this.restTemplate.exchange(url, HttpMethod.PUT, httpEntity, String.class );

        //返回状态码应该等于404
        assertEquals(404,response.getStatusCode().value());
    }

    /**
     * 更新课程，课程不属于自己的，应该返回403
     */
    @Test
    public void shouldReturn403WhenUpdateCourse() {
        url = "http://localhost:"+port+"/course/1";

        //创建body
        Map<String,Object> body = new HashMap();
        body.put("name","Java");
        body.put("introduction","Java");
        body.put("department","Java");

        //创建headers
        MultiValueMap<String,String> requestHeaders = new LinkedMultiValueMap<>();
        //设置headers
        requestHeaders.add("Content-Type","application/json; charset=UTF-8");
        //头部登录信息
        requestHeaders.add("Authentication","123asd");

        //设置http请求数据
        HttpEntity<?> httpEntity = new HttpEntity<Object>(body, requestHeaders);

        Map<String,Object> auth = new HashMap();
        auth.put("userStatus","teacher");
        auth.put("id",1);
        //Mock jwt，解码json web token
        when(jwt.decode(any())).thenReturn(auth);

        Course course = new Course();
        course.setUserId(5);
        //查找课程
        when(courseService.findByID(1)).thenReturn(course);

        //发起请求
        ResponseEntity response = this.restTemplate.exchange(url, HttpMethod.PUT, httpEntity, String.class );

        //返回状态码应该等于403
        assertEquals(403,response.getStatusCode().value());
    }

    /**
     * 更新课程，参数不对，应该返回400
     */
    @Test
    public void shouldReturn400WhenUpdateCourse() {
        url = "http://localhost:"+port+"/course/1";

        //创建body
        Map<String,Object> body = new HashMap();
        body.put("status","Java");
        body.put("name","Java");
        body.put("introduction","Java");
        body.put("department","Java");
        body.put("other","Java");

        //创建headers
        MultiValueMap<String,String> requestHeaders = new LinkedMultiValueMap<>();
        //设置headers
        requestHeaders.add("Content-Type","application/json; charset=UTF-8");
        //头部登录信息
        requestHeaders.add("Authentication","123asd");

        //设置http请求数据
        HttpEntity<?> httpEntity = new HttpEntity<Object>(body, requestHeaders);

        Map<String,Object> auth = new HashMap();
        auth.put("userStatus","teacher");
        auth.put("id",1);
        //Mock jwt，解码json web token
        when(jwt.decode(any())).thenReturn(auth);

        Course course = new Course();
        course.setUserId(1);
        //查找课程
        when(courseService.findByID(1)).thenReturn(course);

        //发起请求
        ResponseEntity response = this.restTemplate.exchange(url, HttpMethod.PUT, httpEntity, String.class );

        //返回状态码应该等于400
        assertEquals(400,response.getStatusCode().value());
    }

    /**
     * 更新课程，更新成功，应该返回200
     */
    @Test
    public void shouldReturn200WhenUpdateCourse() {
        url = "http://localhost:"+port+"/course/1";

        //创建body
        Map<String,Object> body = new HashMap();
        body.put("status","public");
        body.put("name","Java");
        body.put("introduction","Java");
        body.put("department","Java");
        body.put("other","Java");

        //创建headers
        MultiValueMap<String,String> requestHeaders = new LinkedMultiValueMap<>();
        //设置headers
        requestHeaders.add("Content-Type","application/json; charset=UTF-8");
        //头部登录信息
        requestHeaders.add("Authentication","123asd");

        //设置http请求数据
        HttpEntity<?> httpEntity = new HttpEntity<Object>(body, requestHeaders);

        Map<String,Object> auth = new HashMap();
        auth.put("userStatus","teacher");
        auth.put("id",1);
        //Mock jwt，解码json web token
        when(jwt.decode(any())).thenReturn(auth);

        Course course = new Course();
        course.setUserId(1);
        //查找课程
        when(courseService.findByID(1)).thenReturn(course);

        //更新课程
        when(courseService.update(any())).thenReturn(1);

        //发起请求
        ResponseEntity response = this.restTemplate.exchange(url, HttpMethod.PUT, httpEntity, String.class );

        //返回状态码应该等于200
        assertEquals(200,response.getStatusCode().value());
    }

    /**
     * 删除课程，未登录，应该返回401
     */
    @Test
    public void shouldReturn401WhenDeleteCourse() {
        url = "http://localhost:"+port+"/course/1";

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
        ResponseEntity response = this.restTemplate.exchange(url, HttpMethod.DELETE, httpEntity, String.class );

        //返回状态码应该等于401
        assertEquals(401,response.getStatusCode().value());
    }

    /**
     * 删除课程，课程不存在，应该返回404
     */
    @Test
    public void shouldReturn404WhenDeleteCourse() {
        url = "http://localhost:"+port+"/course/1";

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

        Map<String,Object> auth = new HashMap();
        auth.put("userStatus","teacher");
        auth.put("id",1);
        //Mock jwt，解码json web token
        when(jwt.decode(any())).thenReturn(auth);

        //查找课程
        when(courseService.findByID(1)).thenReturn(null);

        //发起请求
        ResponseEntity response = this.restTemplate.exchange(url, HttpMethod.DELETE, httpEntity, String.class );

        //返回状态码应该等于404
        assertEquals(404,response.getStatusCode().value());
    }

    /**
     * 删除课程，课程不是自己的，应该返回403
     */
    @Test
    public void shouldReturn403WhenDeleteCourse() {
        url = "http://localhost:"+port+"/course/1";

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

        Map<String,Object> auth = new HashMap();
        auth.put("userStatus","teacher");
        auth.put("id",1);
        //Mock jwt，解码json web token
        when(jwt.decode(any())).thenReturn(auth);

        Course course = new Course();
        course.setUserId(2);
        //查找课程
        when(courseService.findByID(1)).thenReturn(course);

        //发起请求
        ResponseEntity response = this.restTemplate.exchange(url, HttpMethod.DELETE, httpEntity, String.class );

        //返回状态码应该等于403
        assertEquals(403,response.getStatusCode().value());
    }

    /**
     * 删除课程，删除成功，应该返回200
     */
    @Test
    public void shouldReturn200WhenDeleteCourse() {
        url = "http://localhost:"+port+"/course/1";

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

        Map<String,Object> auth = new HashMap();
        auth.put("userStatus","teacher");
        auth.put("id",1);
        //Mock jwt，解码json web token
        when(jwt.decode(any())).thenReturn(auth);

        Course course = new Course();
        course.setUserId(1);
        //查找课程
        when(courseService.findByID(1)).thenReturn(course);

        //删除课程
        when(courseService.delete(1)).thenReturn(1);

        //发起请求
        ResponseEntity response = this.restTemplate.exchange(url, HttpMethod.DELETE, httpEntity, String.class );

        //返回状态码应该等于200
        assertEquals(200,response.getStatusCode().value());
    }

    /**
     * 获取课程，未登录，应该返回401
     */
    @Test
    public void shouldReturn401WhenGetCourse() {
        url = "http://localhost:"+port+"/course/1";

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
     * 获取课程，课程不存在，应该返回404
     */
    @Test
    public void shouldReturn404WhenGetCourse() {
        url = "http://localhost:"+port+"/course/1";

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

        Map<String,Object> auth = new HashMap();
        auth.put("id",1);
        //Mock jwt，解码json web token
        when(jwt.decode(any())).thenReturn(auth);

        //查找课程
        when(courseService.findByID(1)).thenReturn(null);

        //发起请求
        ResponseEntity response = this.restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class );

        //返回状态码应该等于404
        assertEquals(404,response.getStatusCode().value());
    }

    /**
     * 获取课程，获取成功，应该返回200
     */
    @Test
    public void shouldReturn200WhenGetCourse() {
        url = "http://localhost:"+port+"/course/1";

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

        Map<String,Object> auth = new HashMap();
        auth.put("id",1);
        //Mock jwt，解码json web token
        when(jwt.decode(any())).thenReturn(auth);

        Course course = new Course();
        course.setUserId(1);
        course.setId(1);
        course.setWatchCount(1);
        course.setLikeCount(8);
        course.setCommentCount(6);
        course.setFavoriteCount(8);
        //查找课程
        when(courseService.findByID(1)).thenReturn(course);

        when(profileService.findByUserID(1)).thenReturn(new Profile()); //返回作者资料

        when(watchService.isWatch(1,"course",1)).thenReturn(0); //是否浏览
        when(likeService.isLike(1,"course",1)).thenReturn(1);   //是否点赞
        when(favoriteService.isFavorite(1,"course",1)).thenReturn(0);   //是否收藏

        when(courseService.update(any())).thenReturn(1);    //更新课程

        //发起请求
        ResponseEntity response = this.restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class );

        //返回状态码应该等于200
        assertEquals(200,response.getStatusCode().value());

        //响应数据
        JSONObject responseBody = new JSONObject(response.getBody().toString());

        //社交，统计的值
        assertEquals(2,responseBody.get("watchCount"));
        assertEquals(4,responseBody.get("likeCount"));
        assertEquals(2,responseBody.get("commentCount"));
        assertEquals(2,responseBody.get("favoriteCount"));

        //标记
        assertEquals(false,responseBody.get("isFavorite"));
        assertEquals(true,responseBody.get("isLike"));
    }

    /**
     * 获取课程列表，参数错误，应该返回400
     */
    @Test
    public void shouldReturn400WhenGetCourses() {
        url = "http://localhost:"+port+"/courses?start=0&count=10&sort=hot";

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

        Map<String,Object> auth = new HashMap();
        auth.put("id",1);
        //Mock jwt，解码json web token
        when(jwt.decode(any())).thenReturn(auth);

        //发起请求
        ResponseEntity response = this.restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class );

        //返回状态码应该等于400
        assertEquals(400,response.getStatusCode().value());
    }

    /**
     * 获取课程列表，没有课程，应该返回404
     */
    @Test
    public void shouldReturn404WhenGetCourses() {
        url = "http://localhost:"+port+"/courses?start=0&count=10&sort=rank&departments=计算机系";

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

        Map<String,Object> auth = new HashMap();
        auth.put("id",1);
        //Mock jwt，解码json web token
        when(jwt.decode(any())).thenReturn(auth);

        //统计课程
        when(courseService.count(any())).thenReturn(0);

        //发起请求
        ResponseEntity response = this.restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class );

        //返回状态码应该等于404
        assertEquals(404,response.getStatusCode().value());
    }

    /**
     * 获取课程列表，应该返回200和一门课程
     */
    @Test
    public void shouldReturn200WhenGetCourses() {
        url = "http://localhost:"+port+"/courses?start=0&count=10&sort=rank&departments=计算机系";

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

        Map<String,Object> auth = new HashMap();
        auth.put("id",1);
        //Mock jwt，解码json web token
        when(jwt.decode(any())).thenReturn(auth);

        //统计课程
        when(courseService.count(any())).thenReturn(1);

        //获取课程
        Course course = new Course();
        course.setId(1);
        course.setUserId(1);
        List<Course> courses = new ArrayList();
        courses.add(course);

        //查询条件
        Map<String, Object> conditions = new HashMap();
        List<String> departments = new ArrayList();
        departments.add("计算机系");
        conditions.put("status", Arrays.asList("public"));
        conditions.put("department", departments);

        //获取课程
        when(courseService.getList(0,10,"rank",conditions)).thenReturn(courses);

        //获取用户资料
        when(profileService.findByUserID(1)).thenReturn(new Profile());

        when(likeService.isLike(1,"course",1)).thenReturn(0);           //是否点赞
        when(favoriteService.isFavorite(1,"course",1)).thenReturn(1);   //是否收藏

        //发起请求
        ResponseEntity response = this.restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class );

        //返回状态码应该等于200
        assertEquals(200,response.getStatusCode().value());

        //响应数据
        JSONObject responseBody = new JSONObject(response.getBody().toString());

        assertEquals(1,responseBody.get("total"));  //总共1门课程

        //课程
        JSONObject courseObj = new JSONObject(((JSONArray)responseBody.get("courses")).get(0).toString());
        //标记
        assertEquals(true,courseObj.get("isFavorite"));
        assertEquals(false,courseObj.get("isLike"));
    }
}