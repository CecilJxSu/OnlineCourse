package cn.canlnac.course.controller.user;

import cn.canlnac.course.entity.*;
import cn.canlnac.course.service.*;
import cn.canlnac.course.util.JWT;
import org.apache.commons.collections.map.HashedMap;
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
 * 用户消息测试用例
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-context.xml"})
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
public class MessageControllerTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    private String url;

    @MockBean
    MessageService messageService;
    @MockBean
    ProfileService profileService;
    @MockBean
    CourseService courseService;
    @MockBean
    ChatService chatService;
    @MockBean
    CommentService commentService;
    @MockBean
    JWT jwt;

    /**
     * 获取用户消息，未登录，应该返回401
     */
    @Test
    public void shouldReturn401WhenGetMessages() {
        url = "http://localhost:"+port+"/user/messages";

        //创建body
        Map<String,Object> body = new HashMap();

        //创建headers
        MultiValueMap<String,String> requestHeaders = new LinkedMultiValueMap<>();
        //设置headers
        requestHeaders.add("Content-Type","application/json; charset=UTF-8");

        //设置http请求数据
        HttpEntity<?> httpEntity = new HttpEntity<Object>(body, requestHeaders);

        //发起请求
        ResponseEntity response = this.restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class );

        //返回状态码应该等于401
        assertEquals(401,response.getStatusCode().value());
    }

    /**
     * 获取用户消息，未登录，应该返回401
     */
    @Test
    public void shouldReturn401WithDecodeJwtWhenGetMessages() {
        url = "http://localhost:"+port+"/user/messages";

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
     * 获取用户消息，参数错误，应该返回400
     */
    @Test
    public void shouldReturn400WhenGetMessages() {
        url = "http://localhost:"+port+"/user/messages?isRead=No";

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

        Map<String,Object> auth = new HashedMap();
        //Mock jwt，解码json web token
        when(jwt.decode(any())).thenReturn(auth);

        //发起请求
        ResponseEntity response = this.restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class );

        //返回状态码应该等于400
        assertEquals(400,response.getStatusCode().value());
    }

    /**
     * 获取用户消息，消息为空，应该返回404
     */
    @Test
    public void shouldReturn404WhenGetMessages() {
        url = "http://localhost:"+port+"/user/messages?start=1&count=10&isRead=N";

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

        Map<String,Object> auth = new HashedMap();
        auth.put("id",1);
        //Mock jwt，解码json web token
        when(jwt.decode(any())).thenReturn(auth);

        //插桩，返回0条消息数目
        when(messageService.count(1,"N")).thenReturn(0);

        //发起请求
        ResponseEntity response = this.restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class );

        //返回状态码应该等于404
        assertEquals(404,response.getStatusCode().value());
    }

    /**
     * 获取用户消息，应该返回200和4条消息
     */
    @Test
    public void shouldReturn200WhenGetMessages() {
        url = "http://localhost:"+port+"/user/messages?start=1&count=10&isRead=N";

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

        Map<String,Object> auth = new HashedMap();
        auth.put("id",1);
        auth.put("nickname","cecil");
        auth.put("iconUrl","http://url.com");
        //Mock jwt，解码json web token
        when(jwt.decode(any())).thenReturn(auth);

        //插桩，返回4条消息数目
        when(messageService.count(1,"N")).thenReturn(4);

        //返回4条消息
        List<Message> messages = new ArrayList();
        Message message1 = new Message();
        Message message2 = new Message();
        Message message3 = new Message();
        Message message4 = new Message();

        message1.setId(1);
        message1.setDate(new Date());
        message1.setType("course");
        message1.setFromUserId(2);
        message1.setActionType("like");
        message1.setPositionId(3);
        message1.setContent("点赞测试");

        message2.setId(2);
        message2.setDate(new Date());
        message2.setType("chat");
        message2.setActionType("like");
        message2.setPositionId(3);
        message2.setContent("点赞测试");

        message3.setId(3);
        message3.setDate(new Date());
        message3.setType("comment");
        message3.setFromUserId(2);
        message3.setActionType("like");
        message3.setPositionId(3);

        message4.setId(4);
        message4.setDate(new Date());
        message4.setType("system");

        messages.add(message1);
        messages.add(message2);
        messages.add(message3);
        messages.add(message4);
        when(messageService.getMessages(1,10,1,"N")).thenReturn(messages);

        Profile profile = new Profile();
        profile.setId(1);
        profile.setNickname("cecil");
        profile.setIconUrl("http://url.com/icon");
        when(profileService.findByUserID(2)).thenReturn(profile);

        Course course = new Course();
        course.setId(3);
        course.setName("Java");
        when(courseService.findByID(3)).thenReturn(course);

        Chat chat = new Chat();
        chat.setId(3);
        chat.setTitle("Test");
        when(chatService.findByID(3)).thenReturn(chat);

        Comment comment = new Comment();
        comment.setId(3);
        comment.setContent("Test");
        when(commentService.findByID(3)).thenReturn(comment);

        //发起请求
        ResponseEntity response = this.restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class );

        //返回状态码应该等于200
        assertEquals(200,response.getStatusCode().value());

        //响应数据
        JSONObject responseBody = new JSONObject(response.getBody().toString());

        assertEquals(4,responseBody.get("total"));  //返回4条消息
    }

    /**
     * 获取用户消息，参数错误，应该返回400
     */
    @Test
    public void shouldReturn400WhenGetMessage() {
        url = "http://localhost:"+port+"/user/message/abc"; //id错误

        //创建body
        Map<String,Object> body = new HashMap();

        //创建headers
        MultiValueMap<String,String> requestHeaders = new LinkedMultiValueMap<>();
        //设置headers
        requestHeaders.add("Content-Type","application/json; charset=UTF-8");

        //设置http请求数据
        HttpEntity<?> httpEntity = new HttpEntity<Object>(body, requestHeaders);

        //发起请求
        ResponseEntity response = this.restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class );

        //返回状态码应该等于400
        assertEquals(400,response.getStatusCode().value());
    }

    /**
     * 获取用户消息，未登录，应该返回401
     */
    @Test
    public void shouldReturn401WhenGetMessage() {
        url = "http://localhost:"+port+"/user/message/1";

        //创建body
        Map<String,Object> body = new HashMap();

        //创建headers
        MultiValueMap<String,String> requestHeaders = new LinkedMultiValueMap<>();
        //设置headers
        requestHeaders.add("Content-Type","application/json; charset=UTF-8");

        //设置http请求数据
        HttpEntity<?> httpEntity = new HttpEntity<Object>(body, requestHeaders);

        //发起请求
        ResponseEntity response = this.restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class );

        //返回状态码应该等于401
        assertEquals(401,response.getStatusCode().value());
    }

    /**
     * 获取用户消息，未登录，应该返回401
     */
    @Test
    public void shouldReturn401WithDecodeJwtWhenGetMessage() {
        url = "http://localhost:"+port+"/user/message/1";

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
     * 获取用户消息，消息不存在，应该返回404
     */
    @Test
    public void shouldReturn404WhenGetMessage() {
        url = "http://localhost:"+port+"/user/message/1";

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
        Map<String,Object> auth = new HashedMap();
        auth.put("id",2);
        when(jwt.decode(any())).thenReturn(auth);

        Message message = new Message();
        message.setToUserId(3); //用户ID不相等
        when(messageService.findByID(1)).thenReturn(message);

        //发起请求
        ResponseEntity response = this.restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class );

        //返回状态码应该等于404
        assertEquals(404,response.getStatusCode().value());
    }

    /**
     * 获取用户消息，应该返回200和指定的消息
     */
    @Test
    public void shouldReturn200WhenGetMessage() {
        url = "http://localhost:"+port+"/user/message/1";

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
        Map<String,Object> auth = new HashedMap();
        auth.put("id",2);
        auth.put("nickname","cecil");
        auth.put("iconUrl","http://url.com");
        when(jwt.decode(any())).thenReturn(auth);

        Message message = new Message();
        message.setId(1);
        message.setToUserId(2);
        message.setDate(new Date());
        message.setType("course");
        message.setFromUserId(2);
        message.setActionType("like");
        message.setPositionId(3);
        message.setContent("点赞测试");
        when(messageService.findByID(1)).thenReturn(message);

        Profile profile = new Profile();
        profile.setId(1);
        profile.setNickname("cecil");
        profile.setIconUrl("http://url.com/icon");
        when(profileService.findByUserID(2)).thenReturn(profile);

        Course course = new Course();
        course.setId(3);
        course.setName("Java");
        when(courseService.findByID(3)).thenReturn(course);

        //发起请求
        ResponseEntity response = this.restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class );

        //返回状态码应该等于200
        assertEquals(200,response.getStatusCode().value());

        //响应数据
        JSONObject responseBody = new JSONObject(response.getBody().toString());

        JSONObject fromUser = new JSONObject(responseBody.get("fromUser").toString());

        JSONObject position = new JSONObject((responseBody.get("position").toString()));
        assertEquals(2,fromUser.get("id"));
        assertEquals("Java",position.get("name"));
    }

    /**
     * 删除用户消息，参数错误，应该返回400
     */
    @Test
    public void shouldReturn400WhenDeleteMessage() {
        url = "http://localhost:"+port+"/user/message/abc"; //id错误

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

        //返回状态码应该等于400
        assertEquals(400,response.getStatusCode().value());
    }

    /**
     * 删除用户消息，未登录，应该返回401
     */
    @Test
    public void shouldReturn401WhenDeleteMessage() {
        url = "http://localhost:"+port+"/user/message/1";

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
     * 删除用户消息，未登录，应该返回401
     */
    @Test
    public void shouldReturn401WithDecodeJwtWhenDeleteMessage() {
        url = "http://localhost:"+port+"/user/message/1";

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
     * 删除用户消息，消息不存在，应该返回404
     */
    @Test
    public void shouldReturn404WhenDeleteMessage() {
        url = "http://localhost:"+port+"/user/message/1";

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
        Map<String,Object> auth = new HashedMap();
        auth.put("id",2);
        when(jwt.decode(any())).thenReturn(auth);

        Message message = new Message();
        message.setToUserId(3); //用户ID不相等
        when(messageService.findByID(1)).thenReturn(message);

        //发起请求
        ResponseEntity response = this.restTemplate.exchange(url, HttpMethod.DELETE, httpEntity, String.class );

        //返回状态码应该等于404
        assertEquals(404,response.getStatusCode().value());
    }

    /**
     * 删除用户消息，删除失败，应该返回500
     */
    @Test
    public void shouldReturn500WhenDeleteMessage() {
        url = "http://localhost:"+port+"/user/message/1";

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
        Map<String,Object> auth = new HashedMap();
        auth.put("id",2);
        when(jwt.decode(any())).thenReturn(auth);

        Message message = new Message();
        message.setToUserId(2);
        when(messageService.findByID(1)).thenReturn(message);

        //删除消息失败
        when(messageService.delete(1)).thenReturn(0);

        //发起请求
        ResponseEntity response = this.restTemplate.exchange(url, HttpMethod.DELETE, httpEntity, String.class );

        //返回状态码应该等于500
        assertEquals(500,response.getStatusCode().value());
    }

    /**
     * 删除用户消息，删除成功，应该返回200
     */
    @Test
    public void shouldReturn200WhenDeleteMessage() {
        url = "http://localhost:"+port+"/user/message/1";

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
        Map<String,Object> auth = new HashedMap();
        auth.put("id",2);
        when(jwt.decode(any())).thenReturn(auth);

        Message message = new Message();
        message.setToUserId(2);
        when(messageService.findByID(1)).thenReturn(message);

        //删除消息成功
        when(messageService.delete(1)).thenReturn(1);

        //发起请求
        ResponseEntity response = this.restTemplate.exchange(url, HttpMethod.DELETE, httpEntity, String.class );

        //返回状态码应该等于200
        assertEquals(200,response.getStatusCode().value());
    }
}