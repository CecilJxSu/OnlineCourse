package cn.canlnac.course.controller.catalog;

import cn.canlnac.course.entity.Catalog;
import cn.canlnac.course.entity.LearnRecord;
import cn.canlnac.course.service.CatalogService;
import cn.canlnac.course.service.LearnRecordService;
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
 * 章节下的学习记录测试用例
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
    CatalogService catalogService;
    @MockBean
    LearnRecordService learnRecordService;
    @MockBean
    private JWT jwt;

    /**
     * 创建学习记录，未登录，应该返回401
     */
    @Test
    public void shouldReturn401WhenCreateLearnRecord() {
        url = "http://localhost:"+port+"/catalog/1/learnRecord";

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
     * 创建学习记录，参数错误，应该返回400
     */
    @Test
    public void shouldReturn400WhenCreateLearnRecord() {
        url = "http://localhost:"+port+"/catalog/1/learnRecord";

        //创建body
        Map<String,Object> body = new HashMap();
        body.put("progress",12);

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

        //发起请求
        ResponseEntity response = this.restTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class );

        //返回状态码应该等于400
        assertEquals(400,response.getStatusCode().value());
    }

    /**
     * 创建学习记录，章节不存在，应该返回404
     */
    @Test
    public void shouldReturn404WhenCreateLearnRecord() {
        url = "http://localhost:"+port+"/catalog/1/learnRecord";

        //创建body
        Map<String,Object> body = new HashMap();
        body.put("progress",12.6);
        body.put("lastPosition",12);

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
        ResponseEntity response = this.restTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class );

        //返回状态码应该等于404
        assertEquals(404,response.getStatusCode().value());
    }

    /**
     * 创建学习记录，已经存在学习记录，应该返回409
     */
    @Test
    public void shouldReturn409WhenCreateLearnRecord() {
        url = "http://localhost:"+port+"/catalog/1/learnRecord";

        //创建body
        Map<String,Object> body = new HashMap();
        body.put("progress",12.6);
        body.put("lastPosition",12);

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

        when(learnRecordService.getLearnRecord(1,1)).thenReturn(new LearnRecord()); //获取学习记录

        //发起请求
        ResponseEntity response = this.restTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class );

        //返回状态码应该等于409
        assertEquals(409,response.getStatusCode().value());
    }

    /**
     * 创建学习记录，创建成功，应该返回200
     */
    @Test
    public void shouldReturn200WhenCreateLearnRecord() {
        url = "http://localhost:"+port+"/catalog/1/learnRecord";

        //创建body
        Map<String,Object> body = new HashMap();
        body.put("progress",12.6);
        body.put("lastPosition",12);

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

        when(learnRecordService.getLearnRecord(1,1)).thenReturn(null); //获取学习记录

        when(learnRecordService.create(any())).thenReturn(1);   //创建学习记录

        //发起请求
        ResponseEntity response = this.restTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class );

        //返回状态码应该等于200
        assertEquals(200,response.getStatusCode().value());
    }

    /**
     * 更新学习记录，未登录，应该返回401
     */
    @Test
    public void shouldReturn401WhenUpdateLearnRecord() {
        url = "http://localhost:"+port+"/catalog/1/learnRecord";

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
     * 更新学习记录，参数错误，应该返回400
     */
    @Test
    public void shouldReturn400WhenUpdateLearnRecord() {
        url = "http://localhost:"+port+"/catalog/1/learnRecord";

        //创建body
        Map<String,Object> body = new HashMap();
        body.put("progress",12.6);

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

        //发起请求
        ResponseEntity response = this.restTemplate.exchange(url, HttpMethod.PUT, httpEntity, String.class );

        //返回状态码应该等于400
        assertEquals(400,response.getStatusCode().value());
    }

    /**
     * 更新学习记录，学习记录不存在，应该返回404
     */
    @Test
    public void shouldReturn404WhenUpdateLearnRecord() {
        url = "http://localhost:"+port+"/catalog/1/learnRecord";

        //创建body
        Map<String,Object> body = new HashMap();
        body.put("progress",12.6);
        body.put("lastPosition",12);

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

        when(learnRecordService.getLearnRecord(1,1)).thenReturn(null);  //获取学习记录

        //发起请求
        ResponseEntity response = this.restTemplate.exchange(url, HttpMethod.PUT, httpEntity, String.class );

        //返回状态码应该等于404
        assertEquals(404,response.getStatusCode().value());
    }

    /**
     * 更新学习记录，更新成功，应该返回200
     */
    @Test
    public void shouldReturn200WhenUpdateLearnRecord() {
        url = "http://localhost:"+port+"/catalog/1/learnRecord";

        //创建body
        Map<String,Object> body = new HashMap();
        body.put("progress",12.6);
        body.put("lastPosition",12);

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

        when(learnRecordService.getLearnRecord(1,1)).thenReturn(new LearnRecord());  //获取学习记录
        when(learnRecordService.update(any())).thenReturn(1);   //更新学习记录

        //发起请求
        ResponseEntity response = this.restTemplate.exchange(url, HttpMethod.PUT, httpEntity, String.class );

        //返回状态码应该等于200
        assertEquals(200,response.getStatusCode().value());
    }

    /**
     * 获取习记录，未登录，应该返回401
     */
    @Test
    public void shouldReturn401WhenGetLearnRecord() {
        url = "http://localhost:"+port+"/catalog/1/learnRecord";

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
     * 获取习记录，没有，应该返回404
     */
    @Test
    public void shouldReturn404WhenGetLearnRecord() {
        url = "http://localhost:"+port+"/catalog/1/learnRecord";

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

        when(learnRecordService.getLearnRecord(1,1)).thenReturn(null);  //获取学习记录

        //发起请求
        ResponseEntity response = this.restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class );

        //返回状态码应该等于404
        assertEquals(404,response.getStatusCode().value());
    }

    /**
     * 获取习记录，应该返回200
     */
    @Test
    public void shouldReturn200WhenGetLearnRecord() {
        url = "http://localhost:"+port+"/catalog/1/learnRecord";

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

        when(learnRecordService.getLearnRecord(1,1)).thenReturn(new LearnRecord());  //获取学习记录

        //发起请求
        ResponseEntity response = this.restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class );

        //返回状态码应该等于200
        assertEquals(200,response.getStatusCode().value());
    }
}