package cn.canlnac.course.controller.course;

import cn.canlnac.course.entity.Course;
import cn.canlnac.course.entity.Document;
import cn.canlnac.course.service.CourseService;
import cn.canlnac.course.service.DocumentService;
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
 * 课程文档测试用例
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-context.xml"})
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
public class DocumentControllerTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    private String url;

    @MockBean
    CourseService courseService;
    @MockBean
    DocumentService documentService;
    @MockBean
    JWT jwt;

    /**
     * 创建文档，未登录，应该返回401
     */
    @Test
    public void shouldReturn401WhenCreateDocument() {
        url = "http://localhost:"+port+"/course/1/document";

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
     * 创建文档，课程不存在，应该返回404
     */
    @Test
    public void shouldReturn404WhenCreateDocument() {
        url = "http://localhost:"+port+"/course/1/document";

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
     * 创建文档，课程不属于自己的，应该返回403
     */
    @Test
    public void shouldReturn403WhenCreateDocument() {
        url = "http://localhost:"+port+"/course/1/document";

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
     * 创建文档，参数错误，应该返回400
     */
    @Test
    public void shouldReturn400WhenCreateDocument() {
        url = "http://localhost:"+port+"/course/1/document";

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
        course.setUserId(1);
        when(courseService.findByID(1)).thenReturn(course);   //获取课程

        //发起请求
        ResponseEntity response = this.restTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class );

        //返回状态码应该等于400
        assertEquals(400,response.getStatusCode().value());
    }

    /**
     * 创建文档，创建成功，应该返回200
     */
    @Test
    public void shouldReturn200WhenCreateDocument() {
        url = "http://localhost:"+port+"/course/1/document";

        //创建body
        Map<String,Object> body = new HashMap();
        body.put("url","http://url.com");
        body.put("type","pdf");
        body.put("size",1212);
        body.put("name","java");

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

        when(documentService.create(any())).thenReturn(1);  //创建文档

        //发起请求
        ResponseEntity response = this.restTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class );

        //返回状态码应该等于200
        assertEquals(200,response.getStatusCode().value());
        assertEquals("{\"documentId\":0}",response.getBody());  //返回文档ID
    }

    /**
     * 获取文档，未登录，应该返回401
     */
    @Test
    public void shouldReturn401WhenGetDocuments() {
        url = "http://localhost:"+port+"/course/1/documents";

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
     * 获取文档，参数错误，应该返回400
     */
    @Test
    public void shouldReturn400WhenGetDocuments() {
        url = "http://localhost:"+port+"/course/1/documents?start=0&count=10&sort=sizes";

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

        //发起请求
        ResponseEntity response = this.restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class );

        //返回状态码应该等于400
        assertEquals(400,response.getStatusCode().value());
    }

    /**
     * 获取文档，文档为空，应该返回404
     */
    @Test
    public void shouldReturn404WhenGetDocuments() {
        url = "http://localhost:"+port+"/course/1/documents?start=0&count=10&sort=size";

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

        when(documentService.count("course",1)).thenReturn(0);  //统计文档数目

        //发起请求
        ResponseEntity response = this.restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class );

        //返回状态码应该等于404
        assertEquals(404,response.getStatusCode().value());
    }

    /**
     * 获取文档，应该返回200
     */
    @Test
    public void shouldReturn200WhenGetDocuments() {
        url = "http://localhost:"+port+"/course/1/documents?start=0&count=10&sort=size";

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

        when(documentService.count("course",1)).thenReturn(1);  //统计文档数目
        //获取文档
        Document document = new Document();
        List<Document> documents = new ArrayList();
        documents.add(document);
        when(documentService.getDocuments(0,10,"size","course",1)).thenReturn(documents);

        //发起请求
        ResponseEntity response = this.restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class );

        //返回状态码应该等于200
        assertEquals(200,response.getStatusCode().value());
        //返回body
        assertEquals("{\"total\":1," +
                "\"documents\":[{\"id\":0,\"date\":null,\"targetType\":null," +
                "\"targetId\":0,\"url\":null,\"type\":null," +
                "\"size\":0,\"name\":null}]}",response.getBody());
    }
}