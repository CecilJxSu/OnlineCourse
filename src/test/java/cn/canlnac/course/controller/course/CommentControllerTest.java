package cn.canlnac.course.controller.course;

import cn.canlnac.course.entity.*;
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

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

/**
 * 课程评论测试用例
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-context.xml"})
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
public class CommentControllerTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    private String url;

    @MockBean
    CommentService commentService;
    @MockBean
    ReplyService replyService;
    @MockBean
    ProfileService profileService;
    @MockBean
    LikeService likeService;
    @MockBean
    CourseService courseService;
    @MockBean
    UserService userService;
    @MockBean
    JWT jwt;

    /**
     * 获取课程评论，参数错误，应该返回400
     */
    @Test
    public void shouldReturn400WhenGetComments() {
        url = "http://localhost:"+port+"/course/1/comments?sort=hot";

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
     * 获取课程评论，评论为空，应该返回404
     */
    @Test
    public void shouldReturn404WhenGetComments() {
        url = "http://localhost:"+port+"/course/1/comments?sort=date&start=0&count=10";

        //创建body
        Map<String,Object> body = new HashMap();

        //创建headers
        MultiValueMap<String,String> requestHeaders = new LinkedMultiValueMap<>();
        //设置headers
        requestHeaders.add("Content-Type","application/json; charset=UTF-8");

        //设置http请求数据
        HttpEntity<?> httpEntity = new HttpEntity<Object>(body, requestHeaders);

        //返回评论的总数
        when(commentService.count("course",1)).thenReturn(0);

        //发起请求
        ResponseEntity response = this.restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class );

        //返回状态码应该等于404
        assertEquals(404,response.getStatusCode().value());
    }

    /**
     * 获取课程评论，用户未登录，应该返回200和1条评论，评论有1条回复，没有评论图片
     */
    @Test
    public void shouldReturn200WhenGetComments() {
        url = "http://localhost:"+port+"/course/1/comments?sort=date&start=0&count=10";

        //创建body
        Map<String,Object> body = new HashMap();

        //创建headers
        MultiValueMap<String,String> requestHeaders = new LinkedMultiValueMap<>();
        //设置headers
        requestHeaders.add("Content-Type","application/json; charset=UTF-8");

        //设置http请求数据
        HttpEntity<?> httpEntity = new HttpEntity<Object>(body, requestHeaders);

        //Mock jwt，解码json web token
        when(jwt.decode(any())).thenReturn(null);

        //返回评论的总数
        when(commentService.count("course",1)).thenReturn(1);

        //评论
        List<Comment> comments = new ArrayList();

        Comment comment = new Comment();
        comment.setId(1);
        comment.setUserId(1);
        comment.setDate(new Date());
        comment.setContent("Comment");
        comment.setLikeCount(10);
        comment.setReplyCount(2);

        comments.add(comment);
        //返回评论
        when(commentService.getList(0, 10, "date", "course", 1)).thenReturn(comments);

        //返回用户资料
        Profile profile1 = new Profile();
        profile1.setUserId(1);
        profile1.setNickname("cecil.1");
        profile1.setIconUrl("http://url.com");
        when(profileService.findByUserID(1)).thenReturn(profile1);

        List<Reply> replies = new ArrayList();
        Reply reply = new Reply();
        reply.setId(1);
        reply.setUserId(1);
        reply.setDate(new Date());
        reply.setContent("Reply");
        reply.setCommentId(1);
        reply.setToUserId(2);
        replies.add(reply);
        when(replyService.getReplies(1)).thenReturn(replies);

        //返回用户资料
        Profile profile2 = new Profile();
        profile2.setUserId(2);
        profile2.setNickname("cecil.2");
        profile2.setIconUrl("http://url.com");
        when(profileService.findByUserID(2)).thenReturn(profile2);

        //发起请求
        ResponseEntity response = this.restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class );

        //返回状态码应该等于200
        assertEquals(200,response.getStatusCode().value());

        //响应数据
        JSONObject responseBody = new JSONObject(response.getBody().toString());

        //1个评论
        assertEquals(1,responseBody.get("total"));
        assertEquals(1,((JSONArray)responseBody.get("comments")).length());

        //评论图片
        assertEquals("[]",((JSONObject)((JSONArray)responseBody.get("comments")).get(0)).get("pictureUrls").toString());

        //未点赞，用户没登录
        assertEquals(false,((JSONObject)((JSONArray)responseBody.get("comments")).get(0)).get("isLike"));

        //回复
        JSONArray replyArray = new JSONArray(((JSONObject)((JSONArray)responseBody.get("comments")).get(0)).get("replies").toString());

        //1条回复
        assertEquals(1,((JSONObject)((JSONArray)responseBody.get("comments")).get(0)).get("replyCount"));
        assertEquals(1,replyArray.length());
    }

    /**
     * 获取课程评论，用户登录，应该返回200和1条评论，评论有1条回复，包含评论图片
     */
    @Test
    public void shouldReturn200WithJwtWhenGetComments() {
        url = "http://localhost:"+port+"/course/1/comments?sort=date&start=0&count=10";

        //创建body
        Map<String,Object> body = new HashMap();

        //创建headers
        MultiValueMap<String,String> requestHeaders = new LinkedMultiValueMap<>();
        //设置headers
        requestHeaders.add("Content-Type","application/json; charset=UTF-8");

        //设置http请求数据
        HttpEntity<?> httpEntity = new HttpEntity<Object>(body, requestHeaders);

        //Mock jwt，解码json web token
        Map<String,Object> auth = new HashMap();
        auth.put("id",1);
        when(jwt.decode(any())).thenReturn(auth);

        //返回评论的总数
        when(commentService.count("course",1)).thenReturn(1);

        //评论
        List<Comment> comments = new ArrayList();

        Comment comment = new Comment();
        comment.setId(1);
        comment.setUserId(1);
        comment.setDate(new Date());
        comment.setContent("Comment");
        comment.setPictureUrls("[\"http://url.com/img\"]");
        comment.setLikeCount(10);
        comment.setReplyCount(2);

        comments.add(comment);
        //返回评论
        when(commentService.getList(0, 10, "date", "course", 1)).thenReturn(comments);

        //返回用户资料
        Profile profile1 = new Profile();
        profile1.setUserId(1);
        profile1.setNickname("cecil.1");
        profile1.setIconUrl("http://url.com");
        when(profileService.findByUserID(1)).thenReturn(profile1);

        List<Reply> replies = new ArrayList();
        Reply reply = new Reply();
        reply.setId(1);
        reply.setUserId(1);
        reply.setDate(new Date());
        reply.setContent("Reply");
        reply.setCommentId(1);
        reply.setToUserId(2);
        replies.add(reply);
        when(replyService.getReplies(1)).thenReturn(replies);

        //返回用户资料
        Profile profile2 = new Profile();
        profile2.setUserId(2);
        profile2.setNickname("cecil.2");
        profile2.setIconUrl("http://url.com");
        when(profileService.findByUserID(2)).thenReturn(profile2);

        //点赞
        when(likeService.isLike(1,"comment",1)).thenReturn(1);

        //发起请求
        ResponseEntity response = this.restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class );

        //返回状态码应该等于200
        assertEquals(200,response.getStatusCode().value());

        //响应数据
        JSONObject responseBody = new JSONObject(response.getBody().toString());

        //1个评论
        assertEquals(1,responseBody.get("total"));
        assertEquals(1,((JSONArray)responseBody.get("comments")).length());

        //评论图片
        assertEquals("[\"http://url.com/img\"]",((JSONObject)((JSONArray)responseBody.get("comments")).get(0)).get("pictureUrls").toString());
        //点赞，用户登录
        assertEquals(true,((JSONObject)((JSONArray)responseBody.get("comments")).get(0)).get("isLike"));

        //回复
        JSONArray replyArray = new JSONArray(((JSONObject)((JSONArray)responseBody.get("comments")).get(0)).get("replies").toString());

        //1条回复
        assertEquals(1,((JSONObject)((JSONArray)responseBody.get("comments")).get(0)).get("replyCount"));
        assertEquals(1,replyArray.length());
    }

    /**
     * 创建课程评论，未登录，应该返回401
     */
    @Test
    public void shouldReturn401WhenCreateComment() {
        url = "http://localhost:"+port+"/course/1/comment";

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
     * 创建课程评论，未登录，应该返回401
     */
    @Test
    public void shouldReturn401WithDecodeJwtWhenCreateComment() {
        url = "http://localhost:"+port+"/course/1/comment";

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
     * 创建课程评论，参数错误，应该返回400
     */
    @Test
    public void shouldReturn400WhenCreateComment() {
        url = "http://localhost:"+port+"/course/1/comment";

        //创建body
        Map<String,Object> body = new HashMap();
        body.put("content","Comment");
        body.put("pictureUrls","123");  //不是数组

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
        ResponseEntity response = this.restTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class );

        //返回状态码应该等于400
        assertEquals(400,response.getStatusCode().value());
    }

    /**
     * 创建课程评论，课程不存在，应该返回404
     */
    @Test
    public void shouldReturn404WhenCreateComment() {
        url = "http://localhost:"+port+"/course/1/comment";

        //创建body
        Map<String,Object> body = new HashMap();
        body.put("content","Comment");
        body.put("pictureUrls",new ArrayList());

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

        //返回课程
        when(courseService.findByID(1)).thenReturn(null);

        //发起请求
        ResponseEntity response = this.restTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class );

        //返回状态码应该等于404
        assertEquals(404,response.getStatusCode().value());
    }

    /**
     * 创建课程评论，创建失败，应该返回500
     */
    @Test
    public void shouldReturn500WhenCreateComment() {
        url = "http://localhost:"+port+"/course/1/comment";

        //创建body
        Map<String,Object> body = new HashMap();
        body.put("content","Comment");
        String[] pictureUrls = {"http://url.com/image"};
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

        //返回课程
        when(courseService.findByID(1)).thenReturn(new Course());

        //创建评论
        when(commentService.create(any())).thenReturn(0);

        //发起请求
        ResponseEntity response = this.restTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class );

        //返回状态码应该等于500
        assertEquals(500,response.getStatusCode().value());
    }

    /**
     * 创建课程评论，创建成功，应该返回200和评论ID
     */
    @Test
    public void shouldReturn200WhenCreateComment() {
        url = "http://localhost:"+port+"/course/1/comment";

        //创建body
        Map<String,Object> body = new HashMap();
        body.put("content","Comment");
        String[] pictureUrls = {"http://url.com/image"};
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

        //返回课程
        when(courseService.findByID(1)).thenReturn(new Course());

        //创建评论
        when(commentService.create(any())).thenReturn(1);

        when(courseService.update(any())).thenReturn(1);    //更新课程

        //发起请求
        ResponseEntity response = this.restTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class );

        //返回状态码应该等于200
        assertEquals(200,response.getStatusCode().value());

        //返回评论ID
        assertEquals("{\"commentId\":0}",response.getBody());
    }

    /**
     * 回复课程评论，未登录，应该返回401
     */
    @Test
    public void shouldReturn401WhenCreateReply() {
        url = "http://localhost:"+port+"/course/1/comment/1";

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
     * 回复课程评论，未登录，应该返回401
     */
    @Test
    public void shouldReturn401WithDecodeJwtWhenCreateReply() {
        url = "http://localhost:"+port+"/course/1/comment/1";

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
     * 回复课程评论，参数错误，应该返回400
     */
    @Test
    public void shouldReturn400WhenCreateReply() {
        url = "http://localhost:"+port+"/course/1/comment/1";

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
        ResponseEntity response = this.restTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class );

        //返回状态码应该等于400
        assertEquals(400,response.getStatusCode().value());
    }

    /**
     * 回复课程评论，课程不存在，应该返回404
     */
    @Test
    public void shouldReturn404WhenCreateReply() {
        url = "http://localhost:"+port+"/course/1/comment/1";

        //创建body
        Map<String,Object> body = new HashMap();
        body.put("content","Comment");

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

        //返回课程
        when(courseService.findByID(1)).thenReturn(null);

        //发起请求
        ResponseEntity response = this.restTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class );

        //返回状态码应该等于404
        assertEquals(404,response.getStatusCode().value());
    }

    /**
     * 回复课程评论，创建回复成功，应该返回200
     */
    @Test
    public void shouldReturn200WhenCreateReply() {
        url = "http://localhost:"+port+"/course/1/comment/1";

        //创建body
        Map<String,Object> body = new HashMap();
        body.put("content","Comment");
        body.put("toUserId",2);

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

        //返回课程
        when(courseService.findByID(1)).thenReturn(new Course());

        //返回评论
        when(commentService.findByID(1)).thenReturn(new Comment());

        //返回toUser
        when(userService.findByID(2)).thenReturn(new User());

        //创建评论
        when(replyService.create(any())).thenReturn(1);

        when(commentService.update(any())).thenReturn(1);

        //发起请求
        ResponseEntity response = this.restTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class );

        //返回状态码应该等于200
        assertEquals(200,response.getStatusCode().value());
        //返回回复ID
        assertEquals("{\"replyId\":0}",response.getBody());
    }
}