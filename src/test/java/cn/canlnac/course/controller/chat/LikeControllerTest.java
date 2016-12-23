package cn.canlnac.course.controller.chat;

import cn.canlnac.course.entity.Chat;
import cn.canlnac.course.service.ChatService;
import cn.canlnac.course.service.LikeService;
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
 * 话题点赞测试用例
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-context.xml"})
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
public class LikeControllerTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    private String url;

    @MockBean
    LikeService likeService;
    @MockBean
    ChatService chatService;
    @MockBean
    JWT jwt;

    /**
     * 点赞话题，参数错误，应该返回400
     */
    @Test
    public void shouldReturn400WhenLike() {
        url = "http://localhost:"+port+"/chat/abc/like";   //话题ID错误

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
        ResponseEntity response = this.restTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class );

        //返回状态码应该等于400
        assertEquals(400,response.getStatusCode().value());
    }

    /**
     * 点赞话题，未登录，应该返回401
     */
    @Test
    public void shouldReturn401WhenLike() {
        url = "http://localhost:"+port+"/chat/1/like";

        //创建body
        Map<String,Object> body = new HashMap();

        //创建headers
        MultiValueMap<String,String> requestHeaders = new LinkedMultiValueMap<>();
        //设置headers
        requestHeaders.add("Content-Type","application/json; charset=UTF-8");

        //设置http请求数据
        HttpEntity<?> httpEntity = new HttpEntity<Object>(body, requestHeaders);

        //发起请求
        ResponseEntity response = this.restTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class );

        //返回状态码应该等于401
        assertEquals(401,response.getStatusCode().value());
    }

    /**
     * 点赞话题，未登录，应该返回401
     */
    @Test
    public void shouldReturn401WithDecodeJwtWhenLike() {
        url = "http://localhost:"+port+"/chat/1/like";

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
     * 点赞话题，话题不存在，应该返回404
     */
    @Test
    public void shouldReturn404WhenLike() {
        url = "http://localhost:"+port+"/chat/2/like";

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

        //返回话题消息
        when(chatService.findByID(2)).thenReturn(null);

        //发起请求
        ResponseEntity response = this.restTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class );

        //返回状态码应该等于404
        assertEquals(404,response.getStatusCode().value());
    }

    /**
     * 点赞话题，已经点赞了，应该返回304
     */
    @Test
    public void shouldReturn304WhenLike() {
        url = "http://localhost:"+port+"/chat/2/like";

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

        //返回话题消息
        when(chatService.findByID(2)).thenReturn(new Chat());

        //返回是否点赞
        when(likeService.isLike(1,"chat",2)).thenReturn(1);  //已经点赞

        //发起请求
        ResponseEntity response = this.restTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class );

        //返回状态码应该等于304
        assertEquals(304,response.getStatusCode().value());
    }

    /**
     * 点赞话题，点赞失败，应该返回500
     */
    @Test
    public void shouldReturn500WhenLike() {
        url = "http://localhost:"+port+"/chat/2/like";

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

        //返回话题消息
        when(chatService.findByID(2)).thenReturn(new Chat());

        //返回是否点赞
        when(likeService.isLike(1,"chat",2)).thenReturn(0);  //未点赞

        //点赞话题
        when(likeService.create("chat",2,1)).thenReturn(0);   //点赞失败

        //发起请求
        ResponseEntity response = this.restTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class );

        //返回状态码应该等于500
        assertEquals(500,response.getStatusCode().value());
    }

    /**
     * 点赞话题，点赞成功，应该返回200
     */
    @Test
    public void shouldReturn200WhenLike() {
        url = "http://localhost:"+port+"/chat/2/like";

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

        //返回话题消息
        when(chatService.findByID(2)).thenReturn(new Chat());

        //返回是否点赞
        when(likeService.isLike(1,"chat",2)).thenReturn(0);  //未点赞

        //点赞话题
        when(likeService.create("chat",2,1)).thenReturn(1);   //点赞成功

        when(chatService.update(any())).thenReturn(1);    //更新话题

        //发起请求
        ResponseEntity response = this.restTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class );

        //返回状态码应该等于200
        assertEquals(200,response.getStatusCode().value());
    }

    /**
     * 取消点赞话题，参数错误，应该返回400
     */
    @Test
    public void shouldReturn400WhenUnLike() {
        url = "http://localhost:"+port+"/chat/abc/like";   //话题ID错误

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
        ResponseEntity response = this.restTemplate.exchange(url, HttpMethod.DELETE, httpEntity, String.class );

        //返回状态码应该等于400
        assertEquals(400,response.getStatusCode().value());
    }

    /**
     * 取消点赞话题，未登录，应该返回401
     */
    @Test
    public void shouldReturn401WhenUnLike() {
        url = "http://localhost:"+port+"/chat/1/like";

        //创建body
        Map<String,Object> body = new HashMap();

        //创建headers
        MultiValueMap<String,String> requestHeaders = new LinkedMultiValueMap<>();
        //设置headers
        requestHeaders.add("Content-Type","application/json; charset=UTF-8");

        //设置http请求数据
        HttpEntity<?> httpEntity = new HttpEntity<Object>(body, requestHeaders);

        //发起请求
        ResponseEntity response = this.restTemplate.exchange(url, HttpMethod.DELETE, httpEntity, String.class );

        //返回状态码应该等于401
        assertEquals(401,response.getStatusCode().value());
    }

    /**
     * 取消点赞话题，未登录，应该返回401
     */
    @Test
    public void shouldReturn401WithDecodeJwtWhenUnLike() {
        url = "http://localhost:"+port+"/chat/1/like";

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
     * 取消点赞话题，未点赞，应该返回404
     */
    @Test
    public void shouldReturn404WhenUnLike() {
        url = "http://localhost:"+port+"/chat/2/like";

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

        //是否点赞
        when(likeService.isLike(1,"chat",2)).thenReturn(0);  //未点赞

        //发起请求
        ResponseEntity response = this.restTemplate.exchange(url, HttpMethod.DELETE, httpEntity, String.class );

        //返回状态码应该等于404
        assertEquals(404,response.getStatusCode().value());
    }

    /**
     * 取消点赞话题，取消失败，应该返回500
     */
    @Test
    public void shouldReturn500WhenUnLike() {
        url = "http://localhost:"+port+"/chat/2/like";

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

        //是否点赞
        when(likeService.isLike(1,"chat",2)).thenReturn(1);  //已点赞

        //取消点赞
        when(likeService.delete("chat",2,1)).thenReturn(0);   //取消失败

        //发起请求
        ResponseEntity response = this.restTemplate.exchange(url, HttpMethod.DELETE, httpEntity, String.class );

        //返回状态码应该等于500
        assertEquals(500,response.getStatusCode().value());
    }

    /**
     * 取消点赞话题，取消成功，应该返回200
     */
    @Test
    public void shouldReturn200WhenUnLike() {
        url = "http://localhost:"+port+"/chat/2/like";

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

        //是否点赞
        when(likeService.isLike(1,"chat",2)).thenReturn(1);  //已点赞

        //取消点赞
        when(likeService.delete("chat",2,1)).thenReturn(1);   //取消成功

        when(chatService.findByID(2)).thenReturn(new Chat());   //返回话题
        when(chatService.update(any())).thenReturn(1);    //更新话题

        //发起请求
        ResponseEntity response = this.restTemplate.exchange(url, HttpMethod.DELETE, httpEntity, String.class );

        //返回状态码应该等于200
        assertEquals(200,response.getStatusCode().value());
    }
}