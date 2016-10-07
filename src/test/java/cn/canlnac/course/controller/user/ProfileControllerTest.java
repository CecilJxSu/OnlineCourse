package cn.canlnac.course.controller.user;

import cn.canlnac.course.entity.Profile;
import cn.canlnac.course.entity.User;
import cn.canlnac.course.service.FollowingService;
import cn.canlnac.course.service.ProfileService;
import cn.canlnac.course.service.UserService;
import cn.canlnac.course.util.JWT;
import org.apache.commons.collections.map.HashedMap;
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
 * 用户资料测试用例
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-context.xml"})
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
public class ProfileControllerTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    private String url;

    @MockBean
    private ProfileService profileService;
    @MockBean
    private UserService userService;
    @MockBean
    private FollowingService followingService;
    @MockBean
    private JWT jwt;

    /**
     * 获取用户资料，参数错误，应该返回400
     */
    @Test
    public void shouldReturn400WhenGetUserProfile() {
        url = "http://localhost:"+port+"/user/abc/profile"; //用户ID错误

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
     * 获取用户资料，未登录，应该返回401
     */
    @Test
    public void shouldReturn401WhenGetUserProfile() {
        url = "http://localhost:"+port+"/user/10/profile";

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
     * 获取用户资料，未登录，应该返回401
     */
    @Test
    public void shouldReturn401WithDecodeJwtWhenGetUserProfile() {
        url = "http://localhost:"+port+"/user/10/profile";

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
     * 获取用户资料，用户不存在，应该返回404
     */
    @Test
    public void shouldReturn404WhenGetUserProfile() {
        url = "http://localhost:"+port+"/user/10/profile";

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

        //解码返回的用户信息
        Map<String,Object> auth = new HashedMap();
        //Mock jwt，解码json web token
        when(jwt.decode(any())).thenReturn(auth);

        //Mock userService，获取用户信息
        when(userService.findByID(10)).thenReturn(null);

        //发起请求
        ResponseEntity response = this.restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class );

        //返回状态码应该等于404
        assertEquals(404,response.getStatusCode().value());
    }

    /**
     * 获取用户资料，应该返回200和自己的资料
     */
    @Test
    public void shouldReturn200WhenGetUserProfile() {
        url = "http://localhost:"+port+"/user/10/profile";

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

        //解码返回的用户信息
        Map<String,Object> auth = new HashedMap();
        auth.put("id",10);
        //Mock jwt，解码json web token
        when(jwt.decode(any())).thenReturn(auth);

        User user = new User();
        user.setUserStatus("student");
        //Mock userService，获取用户信息
        when(userService.findByID(10)).thenReturn(user);

        when(profileService.findByUserID(10)).thenReturn(null);

        //发起请求
        ResponseEntity response = this.restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class );

        //返回状态码应该等于200
        assertEquals(200,response.getStatusCode().value());
        //返回自己的资料
        assertEquals("{\"userStatus\":\"student\"}",response.getBody());
    }

    /**
     * 获取用户资料，应该返回200和别人的资料
     */
    @Test
    public void shouldReturn200WhenGetOtherUserProfile() {
        url = "http://localhost:"+port+"/user/10/profile";

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

        //解码返回的用户信息
        Map<String,Object> auth = new HashedMap();
        auth.put("id",11);
        //Mock jwt，解码json web token
        when(jwt.decode(any())).thenReturn(auth);

        User user = new User();
        user.setUserStatus("student");
        //Mock userService，获取用户信息
        when(userService.findByID(10)).thenReturn(user);

        //返回用户资料
        Profile profile = new Profile();
        profile.setGender("male");
        profile.setEmail("123@qq.com");
        when(profileService.findByUserID(10)).thenReturn(profile);
        //是否关注用户
        when(followingService.isFollowing(11,10)).thenReturn(1);

        //发起请求
        ResponseEntity response = this.restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class );

        //返回状态码应该等于200
        assertEquals(200,response.getStatusCode().value());
        //返回别人的资料，isFollowing = true
        assertEquals("{\"universityId\":null,\"userStatus\":\"student\"," +
                "\"isFollowing\":true,\"gender\":\"male\"," +
                "\"major\":null,\"phone\":null,\"dormitoryAddress\":null," +
                "\"nickname\":null,\"iconUrl\":null,\"department\":null," +
                "\"email\":\"123@qq.com\",\"realname\":null}",response.getBody());
    }

    /**
     * 更新用户资料，未登录，应该返回401
     */
    @Test
    public void shouldReturn401WhenUpdateProfile() {
        url = "http://localhost:"+port+"/user/profile";

        //创建body
        Map<String,Object> body = new HashMap();

        //创建headers
        MultiValueMap<String,String> requestHeaders = new LinkedMultiValueMap<>();
        //设置headers
        requestHeaders.add("Content-Type","application/json; charset=UTF-8");

        //设置http请求数据
        HttpEntity<?> httpEntity = new HttpEntity<Object>(body, requestHeaders);

        //发起请求
        ResponseEntity response = this.restTemplate.exchange(url, HttpMethod.PUT, httpEntity, String.class );

        //返回状态码应该等于401
        assertEquals(401,response.getStatusCode().value());
    }

    /**
     * 更新用户资料，未登录，应该返回401
     */
    @Test
    public void shouldReturn401WithDecodeJwtWhenUpdateProfile() {
        url = "http://localhost:"+port+"/user/profile";

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
     * 更新个人资料，参数为空，应该返回400
     */
    @Test
    public void shouldReturn400WhenUpdateProfile() {
        url = "http://localhost:"+port+"/user/profile";

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

        //解码返回的用户信息
        Map<String,Object> auth = new HashedMap();
        //Mock jwt，解码json web token
        when(jwt.decode(any())).thenReturn(auth);

        //发起请求
        ResponseEntity response = this.restTemplate.exchange(url, HttpMethod.PUT, httpEntity, String.class );

        //返回状态码应该等于400
        assertEquals(400,response.getStatusCode().value());
    }

    /**
     * 更新个人资料，邮箱格式不正确，应该返回400
     */
    @Test
    public void shouldReturn400WhereEmailFailedWhenUpdateProfile() {
        url = "http://localhost:"+port+"/user/profile";

        //创建body
        Map<String,Object> body = new HashMap();
        body.put("email","123");

        //创建headers
        MultiValueMap<String,String> requestHeaders = new LinkedMultiValueMap<>();
        //设置headers
        requestHeaders.add("Content-Type","application/json; charset=UTF-8");
        //头部登录信息
        requestHeaders.add("Authentication","123asd");

        //设置http请求数据
        HttpEntity<?> httpEntity = new HttpEntity<Object>(body, requestHeaders);

        //解码返回的用户信息
        Map<String,Object> auth = new HashedMap();
        auth.put("id",1);
        //Mock jwt，解码json web token
        when(jwt.decode(any())).thenReturn(auth);

        //获取用户资料
        when(profileService.findByUserID(1)).thenReturn(null);

        //发起请求
        ResponseEntity response = this.restTemplate.exchange(url, HttpMethod.PUT, httpEntity, String.class );

        //返回状态码应该等于400
        assertEquals(400,response.getStatusCode().value());
    }

    /**
     * 更新个人资料，性别不正确，应该返回400
     */
    @Test
    public void shouldReturn400WhereGenderFailedWhenUpdateProfile() {
        url = "http://localhost:"+port+"/user/profile";

        //创建body
        Map<String,Object> body = new HashMap();
        body.put("gender","123");

        //创建headers
        MultiValueMap<String,String> requestHeaders = new LinkedMultiValueMap<>();
        //设置headers
        requestHeaders.add("Content-Type","application/json; charset=UTF-8");
        //头部登录信息
        requestHeaders.add("Authentication","123asd");

        //设置http请求数据
        HttpEntity<?> httpEntity = new HttpEntity<Object>(body, requestHeaders);

        //解码返回的用户信息
        Map<String,Object> auth = new HashedMap();
        auth.put("id",1);
        //Mock jwt，解码json web token
        when(jwt.decode(any())).thenReturn(auth);

        //获取用户资料
        when(profileService.findByUserID(1)).thenReturn(null);

        //发起请求
        ResponseEntity response = this.restTemplate.exchange(url, HttpMethod.PUT, httpEntity, String.class );

        //返回状态码应该等于400
        assertEquals(400,response.getStatusCode().value());
    }

    /**
     * 更新个人资料，更新失败，应该返回500
     */
    @Test
    public void shouldReturn500WhenUpdateProfile() {
        url = "http://localhost:"+port+"/user/profile";

        //创建body
        Map<String,Object> body = new HashMap();
        body.put("universityId","1340706111");
        body.put("nickname","cecil");
        body.put("realname","苏金兴");
        body.put("iconUrl","http://url.com");
        body.put("phone","18819437953");
        body.put("department","计算机系");
        body.put("major","web应用软件开发");
        body.put("dormitoryAddress","M2409");

        body.put("gender","male");
        body.put("email","123@qq.com");

        //创建headers
        MultiValueMap<String,String> requestHeaders = new LinkedMultiValueMap<>();
        //设置headers
        requestHeaders.add("Content-Type","application/json; charset=UTF-8");
        //头部登录信息
        requestHeaders.add("Authentication","123asd");

        //设置http请求数据
        HttpEntity<?> httpEntity = new HttpEntity<Object>(body, requestHeaders);

        //解码返回的用户信息
        Map<String,Object> auth = new HashedMap();
        auth.put("id",1);
        //Mock jwt，解码json web token
        when(jwt.decode(any())).thenReturn(auth);

        //获取用户资料
        when(profileService.findByUserID(1)).thenReturn(null);
        //创建资料
        when(profileService.create(any())).thenReturn(0);   //创建失败

        //发起请求
        ResponseEntity response = this.restTemplate.exchange(url, HttpMethod.PUT, httpEntity, String.class );

        //返回状态码应该等于500
        assertEquals(500,response.getStatusCode().value());
    }

    /**
     * 更新个人资料，更新成功，应该返回200
     */
    @Test
    public void shouldReturn200WhenUpdateProfile() {
        url = "http://localhost:"+port+"/user/profile";

        //创建body
        Map<String,Object> body = new HashMap();
        body.put("universityId","1340706111");
        body.put("nickname","cecil");
        body.put("realname","苏金兴");
        body.put("iconUrl","http://url.com");
        body.put("phone","18819437953");
        body.put("department","计算机系");
        body.put("major","web应用软件开发");
        body.put("dormitoryAddress","M2409");

        body.put("gender","male");
        body.put("email","123@qq.com");

        //创建headers
        MultiValueMap<String,String> requestHeaders = new LinkedMultiValueMap<>();
        //设置headers
        requestHeaders.add("Content-Type","application/json; charset=UTF-8");
        //头部登录信息
        requestHeaders.add("Authentication","123asd");

        //设置http请求数据
        HttpEntity<?> httpEntity = new HttpEntity<Object>(body, requestHeaders);

        //解码返回的用户信息
        Map<String,Object> auth = new HashedMap();
        auth.put("id",1);
        //Mock jwt，解码json web token
        when(jwt.decode(any())).thenReturn(auth);

        //获取用户资料
        when(profileService.findByUserID(1)).thenReturn(null);
        //创建资料
        when(profileService.create(any())).thenReturn(1);   //创建成功

        //发起请求
        ResponseEntity response = this.restTemplate.exchange(url, HttpMethod.PUT, httpEntity, String.class );

        //返回状态码应该等于200
        assertEquals(200,response.getStatusCode().value());
        //返回空对象
        assertEquals("{}",response.getBody().toString());
    }

    /**
     * 更新个人资料，更新成功，应该返回200
     */
    @Test
    public void shouldReturn200WhereProfileExistsWhenUpdateProfile() {
        url = "http://localhost:"+port+"/user/profile";

        //创建body
        Map<String,Object> body = new HashMap();
        body.put("universityId","1340706111");
        body.put("nickname","cecil");
        body.put("realname","苏金兴");
        body.put("iconUrl","http://url.com");
        body.put("phone","18819437953");
        body.put("department","计算机系");
        body.put("major","web应用软件开发");
        body.put("dormitoryAddress","M2409");

        body.put("gender","male");
        body.put("email","123@qq.com");

        //创建headers
        MultiValueMap<String,String> requestHeaders = new LinkedMultiValueMap<>();
        //设置headers
        requestHeaders.add("Content-Type","application/json; charset=UTF-8");
        //头部登录信息
        requestHeaders.add("Authentication","123asd");

        //设置http请求数据
        HttpEntity<?> httpEntity = new HttpEntity<Object>(body, requestHeaders);

        //解码返回的用户信息
        Map<String,Object> auth = new HashedMap();
        auth.put("id",1);
        //Mock jwt，解码json web token
        when(jwt.decode(any())).thenReturn(auth);

        //获取用户资料
        Profile profile = new Profile();
        profile.setId(10);
        profile.setUserId(1);
        when(profileService.findByUserID(1)).thenReturn(profile);
        //创建资料
        when(profileService.update(any())).thenReturn(1);   //更新成功

        //发起请求
        ResponseEntity response = this.restTemplate.exchange(url, HttpMethod.PUT, httpEntity, String.class );

        //返回状态码应该等于200
        assertEquals(200,response.getStatusCode().value());
        //返回空对象
        assertEquals("{}",response.getBody().toString());
    }
}