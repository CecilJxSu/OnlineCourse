package cn.canlnac.course.controller.course;

import cn.canlnac.course.entity.Catalog;
import cn.canlnac.course.entity.Course;
import cn.canlnac.course.service.CatalogService;
import cn.canlnac.course.service.CourseService;
import cn.canlnac.course.util.JWT;
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
 * 课程章节测试用例
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-context.xml"})
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
public class CatalogControllerTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    private String url;

    @MockBean
    CourseService courseService;
    @MockBean
    CatalogService catalogService;
    @MockBean
    JWT jwt;

    /**
     * 创建章节，未登录，应该返回401
     */
    @Test
    public void shouldReturn401WhenCreateCatalog() {
        url = "http://localhost:"+port+"/course/1/catalog";

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
     * 创建章节，课程不存在，应该返回404
     */
    @Test
    public void shouldReturn404WhenCreateCatalog() {
        url = "http://localhost:"+port+"/course/1/catalog";

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
        //Mock jwt，解码json web token
        when(jwt.decode(any())).thenReturn(auth);

        when(courseService.findByID(1)).thenReturn(null);   //获取课程

        //发起请求
        ResponseEntity response = this.restTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class );

        //返回状态码应该等于404
        assertEquals(404,response.getStatusCode().value());
    }

    /**
     * 创建章节，课程不属于自己的，应该返回403
     */
    @Test
    public void shouldReturn403WhenCreateCatalog() {
        url = "http://localhost:"+port+"/course/1/catalog";

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
        auth.put("userStatus","teacher");
        //Mock jwt，解码json web token
        when(jwt.decode(any())).thenReturn(auth);

        Course course = new Course();
        course.setUserId(2);
        when(courseService.findByID(1)).thenReturn(course);   //获取课程

        //发起请求
        ResponseEntity response = this.restTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class );

        //返回状态码应该等于403
        assertEquals(403,response.getStatusCode().value());
    }

    /**
     * 创建章节，参数错误，应该返回400
     */
    @Test
    public void shouldReturn400WhenCreateCatalog() {
        url = "http://localhost:"+port+"/course/1/catalog";

        //创建body
        Map<String,Object> body = new HashMap();
        body.put("index",1);
        body.put("name","Java");
        body.put("introduction","Java");
        body.put("url","http://url.com");

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
        auth.put("userStatus","teacher");
        //Mock jwt，解码json web token
        when(jwt.decode(any())).thenReturn(auth);

        Course course = new Course();
        course.setUserId(1);
        when(courseService.findByID(1)).thenReturn(course);   //获取课程

        //发起请求
        ResponseEntity response = this.restTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class );

        //返回状态码应该等于400
        assertEquals(400,response.getStatusCode().value());
    }

    /**
     * 创建章节，index冲突，应该返回409
     */
    @Test
    public void shouldReturn409WhenCreateCatalog() {
        url = "http://localhost:"+port+"/course/1/catalog";

        //创建body
        Map<String,Object> body = new HashMap();
        body.put("index",1);
        body.put("name","Java");
        body.put("introduction","Java");
        body.put("url","http://url.com");
        body.put("duration",123);
        body.put("previewImage","http://url.com");

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
        auth.put("userStatus","teacher");
        //Mock jwt，解码json web token
        when(jwt.decode(any())).thenReturn(auth);

        Course course = new Course();
        course.setUserId(1);
        when(courseService.findByID(1)).thenReturn(course);   //获取课程

        //获取课程的所有章节
        Catalog catalog = new Catalog();
        catalog.setIndex(1);    //index冲突
        List<Catalog> catalogs = new ArrayList();
        catalogs.add(catalog);
        when(catalogService.getList(1)).thenReturn(catalogs);

        //发起请求
        ResponseEntity response = this.restTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class );

        //返回状态码应该等于409
        assertEquals(409,response.getStatusCode().value());
    }

    /**
     * 创建章节，创建成功，应该返回200
     */
    @Test
    public void shouldReturn200WhenCreateCatalog() {
        url = "http://localhost:"+port+"/course/1/catalog";

        //创建body
        Map<String,Object> body = new HashMap();
        body.put("index",1);
        body.put("name","Java");
        body.put("introduction","Java");
        body.put("url","http://url.com");
        body.put("duration",123);
        body.put("previewImage","http://url.com");

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
        auth.put("userStatus","teacher");
        //Mock jwt，解码json web token
        when(jwt.decode(any())).thenReturn(auth);

        Course course = new Course();
        course.setUserId(1);
        when(courseService.findByID(1)).thenReturn(course);   //获取课程

        //获取课程的所有章节
        Catalog catalog = new Catalog();
        catalog.setIndex(2);
        List<Catalog> catalogs = new ArrayList();
        catalogs.add(catalog);
        when(catalogService.getList(1)).thenReturn(catalogs);

        when(catalogService.create(any())).thenReturn(1);   //创建章节

        //发起请求
        ResponseEntity response = this.restTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class );

        //返回状态码应该等于200
        assertEquals(200,response.getStatusCode().value());
        assertEquals("{\"catalogId\":0}",response.getBody());
    }

    /**
     * 获取章节，未登录，应该返回401
     */
    @Test
    public void shouldReturn401WhenGetCatalogs() {
        url = "http://localhost:"+port+"/course/1/catalogs";

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
     * 获取章节，没有章节，应该返回404
     */
    @Test
    public void shouldReturn404WhenGetCatalogs() {
        url = "http://localhost:"+port+"/course/1/catalogs";

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
        //Mock jwt，解码json web token
        when(jwt.decode(any())).thenReturn(auth);

        when(catalogService.getList(1)).thenReturn(new ArrayList());

        //发起请求
        ResponseEntity response = this.restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class );

        //返回状态码应该等于404
        assertEquals(404,response.getStatusCode().value());
    }

    /**
     * 获取章节，应该返回200
     */
    @Test
    public void shouldReturn200WhenGetCatalogs() {
        url = "http://localhost:"+port+"/course/1/catalogs";

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
        //Mock jwt，解码json web token
        when(jwt.decode(any())).thenReturn(auth);

        //获取章节列表
        Catalog catalog = new Catalog();
        List<Catalog> catalogs = new ArrayList();
        catalogs.add(catalog);
        when(catalogService.getList(1)).thenReturn(catalogs);

        //发起请求
        ResponseEntity response = this.restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class );

        //返回状态码应该等于200
        assertEquals(200,response.getStatusCode().value());
        assertEquals("[{\"id\":0,\"date\":null,\"courseId\":0,\"index\":0," +
                "\"name\":null,\"introduction\":null,\"url\":null," +
                "\"duration\":0,\"previewImage\":null}]",response.getBody());
    }
}