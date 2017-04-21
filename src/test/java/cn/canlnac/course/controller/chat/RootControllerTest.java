package cn.canlnac.course.controller.chat;

import cn.canlnac.course.entity.Chat;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

/**
 * 话题测试用例
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
    ChatService chatService;
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
     * 创建话题，未登录，应该返回401
     */
    @Test
    public void shouldReturn401WhenCreateChat() {
        url = "http://localhost:"+port+"/chat";

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
     * 创建话题，参数错误，应该返回400
     */
    @Test
    public void shouldReturn400WhenCreateChat() {
        url = "http://localhost:"+port+"/chat";

        //创建body
        Map<String,Object> body = new HashMap();
        body.put("title","Java话题");
        body.put("content","Java话题");
        body.put("html","<p>Java话题</p>");
        body.put("pictureUrls","Java话题");   //参数错误

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
     * 创建话题，创建成功，应该返回200和话题ID
     */
    @Test
    public void shouldReturn200WhenCreateChat() {
        url = "http://localhost:"+port+"/chat";

        //创建body
        Map<String,Object> body = new HashMap();
        body.put("title","Java话题");
        body.put("content","Java话题");
        body.put("html","<p>Java话题</p>");

        List<String> pictureUrls = new ArrayList();
        pictureUrls.add("http://url.com");
        body.put("pictureUrls",pictureUrls);

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

        //创建话题
        when(chatService.create(any())).thenReturn(1);

        //发起请求
        ResponseEntity response = this.restTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class );

        //返回状态码应该等于200
        assertEquals(200,response.getStatusCode().value());

        //返回话题ID
        assertEquals("{\"chatId\":0}",response.getBody());
    }

    /**
     * 删除话题，未登录，应该返回401
     */
    @Test
    public void shouldReturn401WhenDeleteChat() {
        url = "http://localhost:"+port+"/chat/1";

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
     * 删除话题，话题不存在，应该返回404
     */
    @Test
    public void shouldReturn404WhenDeleteChat() {
        url = "http://localhost:"+port+"/chat/1";

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

        //查找话题
        when(chatService.findByID(1)).thenReturn(null);

        //发起请求
        ResponseEntity response = this.restTemplate.exchange(url, HttpMethod.DELETE, httpEntity, String.class );

        //返回状态码应该等于404
        assertEquals(404,response.getStatusCode().value());
    }

    /**
     * 删除话题，话题不是自己的，应该返回403
     */
    @Test
    public void shouldReturn403WhenDeleteChat() {
        url = "http://localhost:"+port+"/chat/1";

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

        Chat chat = new Chat();
        chat.setUserId(2);
        //查找话题
        when(chatService.findByID(1)).thenReturn(chat);

        //发起请求
        ResponseEntity response = this.restTemplate.exchange(url, HttpMethod.DELETE, httpEntity, String.class );

        //返回状态码应该等于403
        assertEquals(403,response.getStatusCode().value());
    }

    /**
     * 删除话题，删除成功，应该返回200
     */
    @Test
    public void shouldReturn200WhenDeleteChat() {
        url = "http://localhost:"+port+"/chat/1";

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

        Chat chat = new Chat();
        chat.setUserId(1);
        //查找话题
        when(chatService.findByID(1)).thenReturn(chat);

        //删除话题
        when(chatService.delete(1)).thenReturn(1);

        //发起请求
        ResponseEntity response = this.restTemplate.exchange(url, HttpMethod.DELETE, httpEntity, String.class );

        //返回状态码应该等于200
        assertEquals(200,response.getStatusCode().value());
    }

    /**
     * 获取话题，未登录，应该返回401
     */
    @Test
    public void shouldReturn401WhenGetChat() {
        url = "http://localhost:"+port+"/chat/1";

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
     * 获取话题，话题不存在，应该返回404
     */
    @Test
    public void shouldReturn404WhenGetChat() {
        url = "http://localhost:"+port+"/chat/1";

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

        //查找话题
        when(chatService.findByID(1)).thenReturn(null);

        //发起请求
        ResponseEntity response = this.restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class );

        //返回状态码应该等于404
        assertEquals(404,response.getStatusCode().value());
    }

    /**
     * 获取话题，获取成功，应该返回200
     */
    @Test
    public void shouldReturn200WhenGetChat() {
        url = "http://localhost:"+port+"/chat/1";

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

        Chat chat = new Chat();
        chat.setUserId(1);
        chat.setId(1);
        chat.setWatchCount(1);
        chat.setLikeCount(8);
        chat.setCommentCount(6);
        chat.setFavoriteCount(8);
        chat.setPictureUrls("[\"http://url.com\"]");
        //查找话题
        when(chatService.findByID(1)).thenReturn(chat);

        when(profileService.findByUserID(1)).thenReturn(new Profile()); //返回作者资料

        when(watchService.isWatch(1,"chat",1)).thenReturn(0); //是否浏览
        when(likeService.isLike(1,"chat",1)).thenReturn(1);   //是否点赞
        when(favoriteService.isFavorite(1,"chat",1)).thenReturn(0);   //是否收藏

        when(chatService.update(any())).thenReturn(1);    //更新话题

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

        //话题图片
        assertEquals("[\"http://url.com\"]",responseBody.get("pictureUrls").toString());

        //标记
        assertEquals(false,responseBody.get("isFavorite"));
        assertEquals(true,responseBody.get("isLike"));
    }

    /**
     * 获取话题列表，参数错误，应该返回400
     */
    @Test
    public void shouldReturn400WhenGetChats() {
        url = "http://localhost:"+port+"/chats?start=0&count=10&sort=hot";

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
     * 获取话题列表，没有话题，应该返回404
     */
    @Test
    public void shouldReturn404WhenGetChats() {
        url = "http://localhost:"+port+"/chats?start=0&count=10&sort=rank&departments=计算机系";

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

        //统计话题
        when(chatService.count(any())).thenReturn(0);

        //发起请求
        ResponseEntity response = this.restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class );

        //返回状态码应该等于404
        assertEquals(404,response.getStatusCode().value());
    }

    /**
     * 获取话题列表，应该返回200和一门话题
     */
    @Test
    public void shouldReturn200WhenGetChats() {
        url = "http://localhost:"+port+"/chats?start=0&count=10&sort=rank";

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

        //统计话题
        when(chatService.count(any())).thenReturn(1);

        //获取话题
        Chat chat = new Chat();
        chat.setId(1);
        chat.setUserId(1);
        chat.setPictureUrls("[\"http://url.com\"]");
        List<Chat> chats = new ArrayList();
        chats.add(chat);

        //查询条件
        Map<String, Object> conditions = new HashMap();

        //获取话题
        when(chatService.getList(0,10,"rank",conditions)).thenReturn(chats);

        //获取用户资料
        when(profileService.findByUserID(1)).thenReturn(new Profile());

        when(likeService.isLike(1,"chat",1)).thenReturn(0);           //是否点赞
        when(favoriteService.isFavorite(1,"chat",1)).thenReturn(1);   //是否收藏

        //发起请求
        ResponseEntity response = this.restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class );

        //返回状态码应该等于200
        assertEquals(200,response.getStatusCode().value());

        //响应数据
        JSONObject responseBody = new JSONObject(response.getBody().toString());

        assertEquals(1,responseBody.get("total"));  //总共1门话题

        //话题
        JSONObject chatObj = new JSONObject(((JSONArray)responseBody.get("chats")).get(0).toString());
        //标记
        assertEquals(true,chatObj.get("isFavorite"));
        assertEquals(false,chatObj.get("isLike"));

        //话题图片
        assertEquals("[\"http://url.com\"]",chatObj.get("pictureUrls").toString());
    }
}