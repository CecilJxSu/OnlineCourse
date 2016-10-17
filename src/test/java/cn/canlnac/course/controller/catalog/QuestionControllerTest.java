package cn.canlnac.course.controller.catalog;

import cn.canlnac.course.entity.Catalog;
import cn.canlnac.course.entity.Course;
import cn.canlnac.course.entity.Question;
import cn.canlnac.course.service.CatalogService;
import cn.canlnac.course.service.CourseService;
import cn.canlnac.course.service.QuestionService;
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
 * 章节下的小测测试用例
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-context.xml"})
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
public class QuestionControllerTest {
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
    QuestionService questionService;
    @MockBean
    private JWT jwt;

    /**
     * 创建小测，未登录，应该返回401
     */
    @Test
    public void shouldReturn401WhenCreateQuestion() {
        url = "http://localhost:"+port+"/catalog/1/question";

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
     * 创建小测，参数错误，应该返回400
     */
    @Test
    public void shouldReturn400WhenCreateQuestion() {
        url = "http://localhost:"+port+"/catalog/1/question";

        //创建body
        Map<String,Object> body = new HashMap();
        body.put("total",12);

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
     * 创建小测，章节不存在，应该返回404
     */
    @Test
    public void shouldReturn404WhenCreateQuestion() {
        url = "http://localhost:"+port+"/catalog/1/question";

        //创建body
        Map<String,Object> body = new HashMap();
        body.put("total",12);

        List questions = new ArrayList();
        Map question = new HashMap();
        questions.add(question);
        body.put("questions",questions);

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
     * 创建小测，课程不属于自己的，应该返回403
     */
    @Test
    public void shouldReturn403WhenCreateQuestion() {
        url = "http://localhost:"+port+"/catalog/1/question";

        //创建body
        Map<String,Object> body = new HashMap();
        body.put("total",12);

        List questions = new ArrayList();
        Map question = new HashMap();
        questions.add(question);
        body.put("questions",questions);

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
        ResponseEntity response = this.restTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class );

        //返回状态码应该等于403
        assertEquals(403,response.getStatusCode().value());
    }

    /**
     * 创建小测，小测已经存在，应该返回409
     */
    @Test
    public void shouldReturn409WhenCreateQuestion() {
        url = "http://localhost:"+port+"/catalog/1/question";

        //创建body
        Map<String,Object> body = new HashMap();
        body.put("total",12);

        List questions = new ArrayList();
        Map question = new HashMap();
        questions.add(question);
        body.put("questions",questions);

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

        when(questionService.findByCatalogId(1)).thenReturn(new Question());    //获取小测

        //发起请求
        ResponseEntity response = this.restTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class );

        //返回状态码应该等于409
        assertEquals(409,response.getStatusCode().value());
    }

    /**
     * 创建小测，创建成功，应该返回200
     */
    @Test
    public void shouldReturn200WhenCreateQuestion() {
        url = "http://localhost:"+port+"/catalog/1/question";

        //创建body
        Map<String,Object> body = new HashMap();
        body.put("total",12);

        List questions = new ArrayList();
        Map question = new HashMap();
        questions.add(question);
        body.put("questions",questions);

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

        when(questionService.findByCatalogId(1)).thenReturn(null);      //获取小测
        when(questionService.create(any())).thenReturn(1);              //创建小测

        //发起请求
        ResponseEntity response = this.restTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class );

        //返回状态码应该等于200
        assertEquals(200,response.getStatusCode().value());

        //返回小测ID
        assertEquals("{\"questionId\":0}",response.getBody());
    }

    /**
     * 更新小测，未登录，应该返回401
     */
    @Test
    public void shouldReturn401WhenUpdateQuestion() {
        url = "http://localhost:"+port+"/catalog/1/question";

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
     * 更新小测，参数错误，应该返回400
     */
    @Test
    public void shouldReturn400WhenUpdateQuestion() {
        url = "http://localhost:"+port+"/catalog/1/question";

        //创建body
        Map<String,Object> body = new HashMap();

        List questions = new ArrayList();
        Map question = new HashMap();
        questions.add(question);
        body.put("questions",questions);

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
        //Mock jwt，解码json web token
        when(jwt.decode(any())).thenReturn(auth);

        //发起请求
        ResponseEntity response = this.restTemplate.exchange(url, HttpMethod.PUT, httpEntity, String.class );

        //返回状态码应该等于400
        assertEquals(400,response.getStatusCode().value());
    }

    /**
     * 更新小测，小测不存在，应该返回404
     */
    @Test
    public void shouldReturn404WhenUpdateQuestion() {
        url = "http://localhost:"+port+"/catalog/1/question";

        //创建body
        Map<String,Object> body = new HashMap();
        body.put("total",12);

        List questions = new ArrayList();
        Map question = new HashMap();
        questions.add(question);
        body.put("questions",questions);

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
        //Mock jwt，解码json web token
        when(jwt.decode(any())).thenReturn(auth);

        when(questionService.findByCatalogId(1)).thenReturn(null);  //获取小测

        //发起请求
        ResponseEntity response = this.restTemplate.exchange(url, HttpMethod.PUT, httpEntity, String.class );

        //返回状态码应该等于404
        assertEquals(404,response.getStatusCode().value());
    }

    /**
     * 更新小测，课程不属于自己的，应该返回403
     */
    @Test
    public void shouldReturn403WhenUpdateQuestion() {
        url = "http://localhost:"+port+"/catalog/1/question";

        //创建body
        Map<String,Object> body = new HashMap();
        body.put("total",12);

        List questions = new ArrayList();
        Map question = new HashMap();
        questions.add(question);
        body.put("questions",questions);

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
        //Mock jwt，解码json web token
        when(jwt.decode(any())).thenReturn(auth);

        when(questionService.findByCatalogId(1)).thenReturn(new Question());  //获取小测

        Catalog catalog = new Catalog();
        catalog.setCourseId(2);
        when(catalogService.findByID(1)).thenReturn(catalog);   //获取章节

        Course course = new Course();
        course.setUserId(2);
        when(courseService.findByID(2)).thenReturn(course); //获取课程

        //发起请求
        ResponseEntity response = this.restTemplate.exchange(url, HttpMethod.PUT, httpEntity, String.class );

        //返回状态码应该等于403
        assertEquals(403,response.getStatusCode().value());
    }

    /**
     * 更新小测，更新成功，应该返回200
     */
    @Test
    public void shouldReturn200WhenUpdateQuestion() {
        url = "http://localhost:"+port+"/catalog/1/question";

        //创建body
        Map<String,Object> body = new HashMap();
        body.put("total",12);

        List questions = new ArrayList();
        Map question = new HashMap();
        questions.add(question);
        body.put("questions",questions);

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

        when(questionService.findByCatalogId(1)).thenReturn(new Question());  //获取小测

        Catalog catalog = new Catalog();
        catalog.setCourseId(2);
        when(catalogService.findByID(1)).thenReturn(catalog);   //获取章节

        Course course = new Course();
        course.setUserId(1);
        when(courseService.findByID(2)).thenReturn(course); //获取课程

        when(questionService.update(any())).thenReturn(1);  //更新小测

        //发起请求
        ResponseEntity response = this.restTemplate.exchange(url, HttpMethod.PUT, httpEntity, String.class );

        //返回状态码应该等于200
        assertEquals(200,response.getStatusCode().value());
    }

    /**
     * 删除小测，未登录，应该返回401
     */
    @Test
    public void shouldReturn401WhenDeleteQuestion() {
        url = "http://localhost:"+port+"/catalog/1/question";

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
     * 删除小测，小测不存在，应该返回404
     */
    @Test
    public void shouldReturn404WhenDeleteQuestion() {
        url = "http://localhost:"+port+"/catalog/1/question";

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

        when(questionService.findByCatalogId(1)).thenReturn(null);  //获取小测

        //发起请求
        ResponseEntity response = this.restTemplate.exchange(url, HttpMethod.DELETE, httpEntity, String.class );

        //返回状态码应该等于404
        assertEquals(404,response.getStatusCode().value());
    }

    /**
     * 删除小测，课程不属于自己的，应该返回403
     */
    @Test
    public void shouldReturn403WhenDeleteQuestion() {
        url = "http://localhost:"+port+"/catalog/1/question";

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

        when(questionService.findByCatalogId(1)).thenReturn(new Question());  //获取小测

        Catalog catalog = new Catalog();
        catalog.setCourseId(2);
        when(catalogService.findByID(1)).thenReturn(catalog);   //获取章节

        Course course = new Course();
        course.setUserId(2);
        when(courseService.findByID(2)).thenReturn(course); //获取课程

        //发起请求
        ResponseEntity response = this.restTemplate.exchange(url, HttpMethod.DELETE, httpEntity, String.class );

        //返回状态码应该等于403
        assertEquals(403,response.getStatusCode().value());
    }

    /**
     * 删除小测，删除成功，应该返回200
     */
    @Test
    public void shouldReturn200WhenDeleteQuestion() {
        url = "http://localhost:"+port+"/catalog/1/question";

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

        Question question = new Question();
        question.setId(1);
        when(questionService.findByCatalogId(1)).thenReturn(question);  //获取小测

        Catalog catalog = new Catalog();
        catalog.setCourseId(2);
        when(catalogService.findByID(1)).thenReturn(catalog);   //获取章节

        Course course = new Course();
        course.setUserId(1);
        when(courseService.findByID(2)).thenReturn(course); //获取课程

        when(questionService.delete(1)).thenReturn(1);  //删除小测

        //发起请求
        ResponseEntity response = this.restTemplate.exchange(url, HttpMethod.DELETE, httpEntity, String.class );

        //返回状态码应该等于200
        assertEquals(200,response.getStatusCode().value());
    }

    /**
     * 获取小测，未登录，应该返回401
     */
    @Test
    public void shouldReturn401WhenGetQuestion() {
        url = "http://localhost:"+port+"/catalog/1/question";

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
     * 获取小测，小测不存在，应该返回404
     */
    @Test
    public void shouldReturn404WhenGetQuestion() {
        url = "http://localhost:"+port+"/catalog/1/question";

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

        when(questionService.findByCatalogId(1)).thenReturn(null);  //获取小测

        //发起请求
        ResponseEntity response = this.restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class );

        //返回状态码应该等于404
        assertEquals(404,response.getStatusCode().value());
    }

    /**
     * 获取小测，应该返回200
     */
    @Test
    public void shouldReturn200WhenGetQuestion() {
        url = "http://localhost:"+port+"/catalog/1/question";

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

        Question question = new Question();
        question.setQuestions("[{}]");
        when(questionService.findByCatalogId(1)).thenReturn(question);  //获取小测

        //发起请求
        ResponseEntity response = this.restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class );

        //返回状态码应该等于200
        assertEquals(200,response.getStatusCode().value());
        assertEquals("{\"date\":null,\"total\":0.0,\"catalogId\":0,\"questions\":[{}],\"id\":0}",response.getBody());
    }
}