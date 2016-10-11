package cn.canlnac.course.controller.document;

import cn.canlnac.course.entity.Catalog;
import cn.canlnac.course.entity.Course;
import cn.canlnac.course.entity.Document;
import cn.canlnac.course.service.CatalogService;
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

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

/**
 * 文档测试用例
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
    CatalogService catalogService;
    @MockBean
    CourseService courseService;
    @MockBean
    DocumentService documentService;
    @MockBean
    JWT jwt;

    /**
     * 获取文档，未登录，应该返回401
     */
    @Test
    public void shouldReturn401WhenGetDocument() {
        url = "http://localhost:"+port+"/document/1";

        //删除body
        Map<String,Object> body = new HashMap();

        //删除headers
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
    public void shouldReturn400WhenGetDocument() {
        url = "http://localhost:"+port+"/document/abc"; //参数错误

        //删除body
        Map<String,Object> body = new HashMap();

        //删除headers
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
    public void shouldReturn404WhenGetDocument() {
        url = "http://localhost:"+port+"/document/1";

        //删除body
        Map<String,Object> body = new HashMap();

        //删除headers
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

        when(documentService.findByID(1)).thenReturn(null);  //获取文档

        //发起请求
        ResponseEntity response = this.restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class );

        //返回状态码应该等于404
        assertEquals(404,response.getStatusCode().value());
    }

    /**
     * 获取文档，应该返回200
     */
    @Test
    public void shouldReturn200WhenGetDocument() {
        url = "http://localhost:"+port+"/document/1";

        //删除body
        Map<String,Object> body = new HashMap();

        //删除headers
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

        //获取文档
        Document document = new Document();
        document.setTargetType("course");
        document.setTargetId(1);
        document.setUrl("http://url.com");
        when(documentService.findByID(1)).thenReturn(document);

        //发起请求
        ResponseEntity response = this.restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class );

        //返回状态码应该等于200
        assertEquals(200,response.getStatusCode().value());
        //返回body
        assertEquals("{\"id\":0," +
                "\"date\":null,\"targetType\":\"course\"," +
                "\"targetId\":1,\"url\":\"http://url.com\"," +
                "\"type\":null,\"size\":0,\"name\":null}",response.getBody());
    }

    /**
     * 删除文档，未登录，应该返回401
     */
    @Test
    public void shouldReturn401WhenDeleteDocument() {
        url = "http://localhost:"+port+"/document/1";

        //删除body
        Map<String,Object> body = new HashMap();

        //删除headers
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
     * 删除文档，文档不存在，应该返回404
     */
    @Test
    public void shouldReturn404WhenDeleteDocument() {
        url = "http://localhost:"+port+"/document/1";

        //删除body
        Map<String,Object> body = new HashMap();

        //删除headers
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

        when(documentService.findByID(1)).thenReturn(null);   //获取文档

        //发起请求
        ResponseEntity response = this.restTemplate.exchange(url, HttpMethod.DELETE, httpEntity, String.class );

        //返回状态码应该等于404
        assertEquals(404,response.getStatusCode().value());
    }

    /**
     * 删除文档，文档不属于自己的，应该返回403
     */
    @Test
    public void shouldReturn403WhenDeleteDocument() {
        url = "http://localhost:"+port+"/document/1";

        //删除body
        Map<String,Object> body = new HashMap();

        //删除headers
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

        Document document = new Document();
        document.setTargetType("catalog");
        document.setTargetId(2);
        when(documentService.findByID(1)).thenReturn(document);     //获取文档

        Catalog catalog = new Catalog();
        catalog.setCourseId(2);
        when(catalogService.findByID(2)).thenReturn(catalog);       //获取章节

        Course course = new Course();
        course.setUserId(2);
        when(courseService.findByID(2)).thenReturn(course);         //获取课程

        //发起请求
        ResponseEntity response = this.restTemplate.exchange(url, HttpMethod.DELETE, httpEntity, String.class );

        //返回状态码应该等于403
        assertEquals(403,response.getStatusCode().value());
    }

    /**
     * 删除文档，删除成功，应该返回200
     */
    @Test
    public void shouldReturn200WhenDeleteDocument() {
        url = "http://localhost:"+port+"/document/1";

        //删除body
        Map<String,Object> body = new HashMap();

        //删除headers
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

        Document document = new Document();
        document.setTargetType("catalog");
        document.setTargetId(2);
        when(documentService.findByID(1)).thenReturn(document);     //获取文档

        Catalog catalog = new Catalog();
        catalog.setCourseId(2);
        when(catalogService.findByID(2)).thenReturn(catalog);       //获取章节

        Course course = new Course();
        course.setUserId(1);
        when(courseService.findByID(2)).thenReturn(course);         //获取课程

        when(documentService.delete(1)).thenReturn(1);              //删除文档

        //发起请求
        ResponseEntity response = this.restTemplate.exchange(url, HttpMethod.DELETE, httpEntity, String.class );

        //返回状态码应该等于200
        assertEquals(200,response.getStatusCode().value());
        assertEquals("{}",response.getBody());  //返回空对象
    }
}