package cn.canlnac.course.controller;

import cn.canlnac.course.entity.Profile;
import cn.canlnac.course.entity.User;
import cn.canlnac.course.service.ProfileService;
import cn.canlnac.course.service.UserService;
import cn.canlnac.course.util.JWT;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
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
 * 登录测试用例
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-context.xml"})
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
public class LoginControllerTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    private String url;

    @MockBean
    private UserService userService;
    @MockBean
    private ProfileService profileService;
    @MockBean
    private JWT jwt;

    @Before
    public void before(){
        url = "http://localhost:"+port+"/login";
    }

    /**
     * 参数错误，应该返回400状态码
     */
    @Test
    public void shouldReturn400() {
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

        //返回状态码应该等于400
        assertEquals(400,response.getStatusCode().value());
    }

    /**
     * 用户密码不正确，应该返回403状态码
     */
    @Test
    public void shouldReturn403(){
        //创建body
        Map<String,Object> body = new HashMap();
        //设置body参数
        body.put("username","123");
        body.put("password","123");

        //创建headers
        MultiValueMap<String,String> requestHeaders = new LinkedMultiValueMap<>();
        //设置headers
        requestHeaders.add("Content-Type","application/json; charset=UTF-8");

        //设置http请求数据
        HttpEntity<?> httpEntity = new HttpEntity<Object>(body, requestHeaders);

        //Mock userService，根据用户名获取用户信息
        User user = new User();     //创建用户
        user.setPassword("124");    //设置密码
        when(this.userService.findByUsername("123")).thenReturn(user);  //返回该用户信息

        //发起请求
        ResponseEntity response = this.restTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class );

        //返回状态码应该等于403
        assertEquals(403,response.getStatusCode().value());
    }

    /**
     * 用户找不到，应该返回404状态码
     */
    @Test
    public void shouldReturn404(){
        //创建body
        Map<String,Object> body = new HashMap();
        //设置body参数
        body.put("username","123");
        body.put("password","123");

        //创建headers
        MultiValueMap<String,String> requestHeaders = new LinkedMultiValueMap<>();

        //设置headers
        requestHeaders.add("Content-Type","application/json; charset=UTF-8");

        //设置http请求数据
        HttpEntity<?> httpEntity = new HttpEntity<Object>(body, requestHeaders);

        //Mock userService，根据用户名获取用户信息
        when(this.userService.findByUsername("123")).thenReturn(null);

        //发起请求
        ResponseEntity response = this.restTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class );

        //返回状态码应该等于404
        assertEquals(404,response.getStatusCode().value());
    }

    /**
     * 用户验证成功，应该返回200状态码并设置header的Authentication
     */
    @Test
    public void shouldReturn200AndSetJWT(){
        //创建body
        Map<String,Object> body = new HashMap();
        //设置body参数
        body.put("username","123");
        body.put("password","123");

        //创建headers
        MultiValueMap<String,String> requestHeaders = new LinkedMultiValueMap<>();

        //设置headers
        requestHeaders.add("Content-Type","application/json; charset=UTF-8");

        //设置http请求数据
        HttpEntity<?> httpEntity = new HttpEntity<Object>(body, requestHeaders);

        //Mock userService，根据用户名获取用户信息
        User user = new User();     //创建用户
        user.setId(1);
        user.setUserStatus("student");
        user.setPassword("123");    //设置密码
        when(this.userService.findByUsername("123")).thenReturn(user);  //返回该用户信息

        //Mock，profileService，获取用户资料
        Profile profile = new Profile();
        profile.setNickname("cecil");
        profile.setGender("male");
        profile.setIconUrl("http://url.com");
        when(profileService.findByUserID(1)).thenReturn(profile);   //返回该用户资料

        //Mock，JWT，json web token
        when(jwt.sign(any())).thenReturn("asd123");

        //发起请求
        ResponseEntity response = this.restTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class );

        //返回状态码应该等于200
        assertEquals(200,response.getStatusCode().value());

        //获取响应头
         HttpHeaders responseHeaders = response.getHeaders();
        //应该设置头部的Authentication的JWT值
        assertEquals("asd123", responseHeaders.get("Authentication").get(0));
    }
}