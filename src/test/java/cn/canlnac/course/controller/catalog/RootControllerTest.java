package cn.canlnac.course.controller.catalog;

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
 * 章节测试用例
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
    private CatalogService catalogService;
    @MockBean
    private CourseService courseService;
    @MockBean
    private JWT jwt;

    /**
     * 获取章节，未登录，应该返回401
     */
    @Test
    public void shouldReturn401WhenGetCatalog() {
        url = "http://localhost:"+port+"/catalog/1";

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
     * 获取章节，不存在，应该返回404
     */
    @Test
    public void shouldReturn404WhenGetCatalog() {
        url = "http://localhost:"+port+"/catalog/1";

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

        when(catalogService.findByID(1)).thenReturn(null);  //获取章节

        //发起请求
        ResponseEntity response = this.restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class );

        //返回状态码应该等于404
        assertEquals(404,response.getStatusCode().value());
    }

    /**
     * 获取章节，应该返回200
     */
    @Test
    public void shouldReturn200WhenGetCatalog() {
        url = "http://localhost:"+port+"/catalog/1";

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

        when(catalogService.findByID(1)).thenReturn(new Catalog());  //获取章节

        //发起请求
        ResponseEntity response = this.restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class );

        //返回状态码应该等于200
        assertEquals(200,response.getStatusCode().value());
    }

    /**
     * 更新章节，未登录，应该返回401
     */
    @Test
    public void shouldReturn401WhenUpdateCatalog() {
        url = "http://localhost:"+port+"/catalog/1";

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
        ResponseEntity response = this.restTemplate.exchange(url, HttpMethod.PUT, httpEntity, String.class );

        //返回状态码应该等于401
        assertEquals(401,response.getStatusCode().value());
    }

    /**
     * 更新章节，章节不存在，应该返回404
     */
    @Test
    public void shouldReturn404WhenUpdateCatalog() {
        url = "http://localhost:"+port+"/catalog/1";

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

        when(catalogService.findByID(1)).thenReturn(null);  //获取章节

        //发起请求
        ResponseEntity response = this.restTemplate.exchange(url, HttpMethod.PUT, httpEntity, String.class );

        //返回状态码应该等于404
        assertEquals(404,response.getStatusCode().value());
    }

    /**
     * 更新章节，不属于自己的章节，应该返回403
     */
    @Test
    public void shouldReturn403WhenUpdateCatalog() {
        url = "http://localhost:"+port+"/catalog/1";

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
        auth.put("userStatus","teacher");
        //Mock jwt，解码json web token
        when(jwt.decode(any())).thenReturn(auth);

        Catalog catalog = new Catalog();
        catalog.setCourseId(2);
        when(catalogService.findByID(1)).thenReturn(catalog);  //获取章节

        Course course = new Course();
        course.setUserId(2);
        when(courseService.findByID(2)).thenReturn(course); //获取课程

        //发起请求
        ResponseEntity response = this.restTemplate.exchange(url, HttpMethod.PUT, httpEntity, String.class );

        //返回状态码应该等于403
        assertEquals(403,response.getStatusCode().value());
    }

    /**
     * 更新章节，参数错误，应该返回400
     */
    @Test
    public void shouldReturn400WhenUpdateCatalog() {
        url = "http://localhost:"+port+"/catalog/1";

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
        auth.put("userStatus","teacher");
        //Mock jwt，解码json web token
        when(jwt.decode(any())).thenReturn(auth);

        Catalog catalog = new Catalog();
        catalog.setCourseId(2);
        when(catalogService.findByID(1)).thenReturn(catalog);  //获取章节

        Course course = new Course();
        course.setUserId(1);
        when(courseService.findByID(2)).thenReturn(course); //获取课程

        //发起请求
        ResponseEntity response = this.restTemplate.exchange(url, HttpMethod.PUT, httpEntity, String.class );

        //返回状态码应该等于400
        assertEquals(400,response.getStatusCode().value());
    }

    /**
     * 更新章节，index冲突，应该返回409
     */
    @Test
    public void shouldReturn409WhenUpdateCatalog() {
        url = "http://localhost:"+port+"/catalog/1";

        //创建body
        Map<String,Object> body = new HashMap();
        body.put("index",1);

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
        auth.put("userStatus","teacher");
        //Mock jwt，解码json web token
        when(jwt.decode(any())).thenReturn(auth);

        Catalog catalog = new Catalog();
        catalog.setIndex(2);
        catalog.setCourseId(2);
        when(catalogService.findByID(1)).thenReturn(catalog);  //获取章节

        Course course = new Course();
        course.setUserId(1);
        when(courseService.findByID(2)).thenReturn(course); //获取课程

        List catalogs = new ArrayList();
        Catalog catalog1 = new Catalog();
        catalog1.setIndex(1);
        catalogs.add(catalog);
        catalogs.add(catalog1);
        when(catalogService.getList(2)).thenReturn(catalogs);   //获取所有章节

        //发起请求
        ResponseEntity response = this.restTemplate.exchange(url, HttpMethod.PUT, httpEntity, String.class );

        //返回状态码应该等于409
        assertEquals(409,response.getStatusCode().value());
    }

    /**
     * 更新章节，更新成功，应该返回200
     */
    @Test
    public void shouldReturn200WhenUpdateCatalog() {
        url = "http://localhost:"+port+"/catalog/1";

        //创建body
        Map<String,Object> body = new HashMap();
        body.put("index",3);
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

        Map auth = new HashMap();
        auth.put("id",1);
        auth.put("userStatus","teacher");
        //Mock jwt，解码json web token
        when(jwt.decode(any())).thenReturn(auth);

        Catalog catalog = new Catalog();
        catalog.setIndex(2);
        catalog.setCourseId(2);
        when(catalogService.findByID(1)).thenReturn(catalog);  //获取章节

        Course course = new Course();
        course.setUserId(1);
        when(courseService.findByID(2)).thenReturn(course); //获取课程

        List catalogs = new ArrayList();
        Catalog catalog1 = new Catalog();
        catalog1.setIndex(1);
        catalogs.add(catalog);
        catalogs.add(catalog1);
        when(catalogService.getList(2)).thenReturn(catalogs);   //获取所有章节

        when(catalogService.update(any())).thenReturn(1);   //更新章节

        //发起请求
        ResponseEntity response = this.restTemplate.exchange(url, HttpMethod.PUT, httpEntity, String.class );

        //返回状态码应该等于200
        assertEquals(200,response.getStatusCode().value());
    }

    /**
     * 删除章节，未登录，应该返回401
     */
    @Test
    public void shouldReturn401WhenDeleteCatalog() {
        url = "http://localhost:"+port+"/catalog/1";

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
     * 删除章节，章节不存在，应该返回404
     */
    @Test
    public void shouldReturn404WhenDeleteCatalog() {
        url = "http://localhost:"+port+"/catalog/1";

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

        when(catalogService.findByID(1)).thenReturn(null);  //获取章节

        //发起请求
        ResponseEntity response = this.restTemplate.exchange(url, HttpMethod.DELETE, httpEntity, String.class );

        //返回状态码应该等于404
        assertEquals(404,response.getStatusCode().value());
    }

    /**
     * 删除章节，章节不属于自己，应该返回403
     */
    @Test
    public void shouldReturn403WhenDeleteCatalog() {
        url = "http://localhost:"+port+"/catalog/1";

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
        auth.put("userStatus","teacher");
        //Mock jwt，解码json web token
        when(jwt.decode(any())).thenReturn(auth);

        Catalog catalog = new Catalog();
        catalog.setCourseId(2);
        when(catalogService.findByID(1)).thenReturn(catalog);  //获取章节

        Course course = new Course();
        course.setUserId(2);
        when(courseService.findByID(2)).thenReturn(course); //获取课程

        //发起请求
        ResponseEntity response = this.restTemplate.exchange(url, HttpMethod.DELETE, httpEntity, String.class );

        //返回状态码应该等于403
        assertEquals(403,response.getStatusCode().value());
    }

    /**
     * 删除章节，删除成功，应该返回200
     */
    @Test
    public void shouldReturn200WhenDeleteCatalog() {
        url = "http://localhost:"+port+"/catalog/1";

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
        auth.put("userStatus","teacher");
        //Mock jwt，解码json web token
        when(jwt.decode(any())).thenReturn(auth);

        Catalog catalog = new Catalog();
        catalog.setCourseId(2);
        when(catalogService.findByID(1)).thenReturn(catalog);  //获取章节

        Course course = new Course();
        course.setUserId(1);
        when(courseService.findByID(2)).thenReturn(course); //获取课程

        when(catalogService.delete(1)).thenReturn(1);   //删除章节

        //发起请求
        ResponseEntity response = this.restTemplate.exchange(url, HttpMethod.DELETE, httpEntity, String.class );

        //返回状态码应该等于200
        assertEquals(200,response.getStatusCode().value());
    }
}