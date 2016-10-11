package cn.canlnac.course.controller.catalog;

import cn.canlnac.course.entity.Answer;
import cn.canlnac.course.entity.Question;
import cn.canlnac.course.service.AnswerService;
import cn.canlnac.course.service.CatalogService;
import cn.canlnac.course.service.QuestionService;
import cn.canlnac.course.util.JWT;
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
 * 回答测试用例
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-context.xml"})
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
public class AnswerControllerTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    private String url;

    @MockBean
    CatalogService catalogService;
    @MockBean
    AnswerService answerService;
    @MockBean
    QuestionService questionService;
    @MockBean
    private JWT jwt;

    /**
     * 获取回答，未登录，应该返回401
     */
    @Test
    public void shouldReturn401WhenGetAnswer() {
        url = "http://localhost:"+port+"/catalog/1/answer";

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
     * 获取回答，没有回答，应该返回404
     */
    @Test
    public void shouldReturn404WhenGetAnswer() {
        url = "http://localhost:"+port+"/catalog/1/answer";

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

        when(answerService.getAnswer(1,1)).thenReturn(null);    //获取回答

        //发起请求
        ResponseEntity response = this.restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class );

        //返回状态码应该等于404
        assertEquals(404,response.getStatusCode().value());
    }

    /**
     * 获取回答，应该返回200
     */
    @Test
    public void shouldReturn200WhenGetAnswer() {
        url = "http://localhost:"+port+"/catalog/1/answer";

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

        Answer answer = new Answer();
        answer.setAnswer("{\"1\":[\"A\"]}");
        when(answerService.getAnswer(1,1)).thenReturn(answer);    //获取回答

        //发起请求
        ResponseEntity response = this.restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class );

        //返回状态码应该等于200
        assertEquals(200,response.getStatusCode().value());

        //响应数据
        JSONObject responseBody = new JSONObject(response.getBody().toString());

        //答案
        assertEquals("{\"1\":[\"A\"]}",responseBody.get("answer").toString());
    }

    /**
     * 创建回答，未登录，应该返回401
     */
    @Test
    public void shouldReturn401WhenCreateAnswer() {
        url = "http://localhost:"+port+"/catalog/1/answer";

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
     * 创建回答，已经存在了，应该返回409
     */
    @Test
    public void shouldReturn409WhenCreateAnswer() {
        url = "http://localhost:"+port+"/catalog/1/answer";

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

        when(answerService.getAnswer(1,1)).thenReturn(new Answer());    //获取回答

        //发起请求
        ResponseEntity response = this.restTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class );

        //返回状态码应该等于409
        assertEquals(409,response.getStatusCode().value());
    }

    /**
     * 创建回答，参数错误，应该返回400
     */
    @Test
    public void shouldReturn400WhenCreateAnswer() {
        url = "http://localhost:"+port+"/catalog/1/answer";

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

        when(answerService.getAnswer(1,1)).thenReturn(null);    //获取回答

        //发起请求
        ResponseEntity response = this.restTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class );

        //返回状态码应该等于400
        assertEquals(400,response.getStatusCode().value());
    }

    /**
     * 创建回答，章节不等于问题的章节ID，应该返回404
     */
    @Test
    public void shouldReturn404WhenCreateAnswer() {
        url = "http://localhost:"+port+"/catalog/1/answer";

        //创建body
        Map body = new HashMap();
        body.put("questionId",1);

        Map answer = new HashMap();
        List answerSheet = new ArrayList();
        answerSheet.add("A");

        answer.put("1",answerSheet);
        body.put("answer",answer);
        body.put("total",98.5);

        //创建headers
        MultiValueMap requestHeaders = new LinkedMultiValueMap();
        //设置headers
        requestHeaders.add("Content-Type","application/json; charset=UTF-8");
        //头部登录信息
        requestHeaders.add("Authentication","123asd");

        //设置http请求数据
        HttpEntity httpEntity = new HttpEntity<Object>(body, requestHeaders);

        Map auth = new HashMap();
        auth.put("id",1);
        //Mock jwt，解码json web token
        when(jwt.decode(any())).thenReturn(auth);

        when(answerService.getAnswer(1,1)).thenReturn(null);    //获取回答

        //获取小测
        Question question = new Question();
        question.setCatalogId(2);
        when(questionService.findById(1)).thenReturn(question);

        //发起请求
        ResponseEntity response = this.restTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class );

        //返回状态码应该等于404
        assertEquals(404,response.getStatusCode().value());
    }

    /**
     * 创建回答，应该返回200
     */
    @Test
    public void shouldReturn200WhenCreateAnswer() {
        url = "http://localhost:"+port+"/catalog/1/answer";

        //创建body
        Map body = new HashMap();
        body.put("questionId",1);

        Map answer = new HashMap();
        List answerSheet = new ArrayList();
        answerSheet.add("A");

        answer.put("1",answerSheet);
        body.put("answer",answer);
        body.put("total",98.5);

        //创建headers
        MultiValueMap requestHeaders = new LinkedMultiValueMap();
        //设置headers
        requestHeaders.add("Content-Type","application/json; charset=UTF-8");
        //头部登录信息
        requestHeaders.add("Authentication","123asd");

        //设置http请求数据
        HttpEntity httpEntity = new HttpEntity<Object>(body, requestHeaders);

        Map auth = new HashMap();
        auth.put("id",1);
        //Mock jwt，解码json web token
        when(jwt.decode(any())).thenReturn(auth);

        when(answerService.getAnswer(1,1)).thenReturn(null);    //获取回答

        //获取小测
        Question question = new Question();
        question.setCatalogId(1);
        when(questionService.findById(1)).thenReturn(question);

        when(answerService.create(any())).thenReturn(1);    //创建回答

        //发起请求
        ResponseEntity response = this.restTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class );

        //返回状态码应该等于200
        assertEquals(200,response.getStatusCode().value());

        //回答ID
        assertEquals("{\"answerId\":0}",response.getBody());
    }
}