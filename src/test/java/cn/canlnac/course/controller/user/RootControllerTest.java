package cn.canlnac.course.controller.user;

import cn.canlnac.course.entity.User;
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

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

/**
 * 用户测试用例
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
    private UserService userService;
    @MockBean
    private JWT jwt;

    /**
     * 注册用户，邮箱格式错误，应该返回400
     */
    @Test
    public void shouldReturn400WhenRegister() {
        url = "http://localhost:"+port+"/user";

        //创建body
        Map<String,Object> body = new HashMap();
        body.put("username","123");
        body.put("password","123");
        body.put("email","123");

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
     * 注册用户，创建用户失败，应该返回500
     */
    @Test
    public void shouldReturn500WhenRegister() {
        url = "http://localhost:"+port+"/user";

        //创建body
        Map<String,Object> body = new HashMap();
        body.put("username","123");
        body.put("password","123");
        body.put("email","123@qq.com");

        //创建headers
        MultiValueMap<String,String> requestHeaders = new LinkedMultiValueMap<>();
        //设置headers
        requestHeaders.add("Content-Type","application/json; charset=UTF-8");

        //设置http请求数据
        HttpEntity<?> httpEntity = new HttpEntity<Object>(body, requestHeaders);

        //Mock userService，创建用户
        when(this.userService.create(any())).thenReturn(0);  //返回创建数目

        //发起请求
        ResponseEntity response = this.restTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class );

        //返回状态码应该等于500
        assertEquals(500,response.getStatusCode().value());
    }

    /**
     * 注册用户成功，应该返回200和用户ID
     */
    @Test
    public void shouldReturn200AndUserIdWhenRegister() {
        url = "http://localhost:"+port+"/user";

        //创建body
        Map<String,Object> body = new HashMap();
        body.put("username","123");
        body.put("password","123");
        body.put("email","123@qq.com");

        //创建headers
        MultiValueMap<String,String> requestHeaders = new LinkedMultiValueMap<>();
        //设置headers
        requestHeaders.add("Content-Type","application/json; charset=UTF-8");

        //设置http请求数据
        HttpEntity<?> httpEntity = new HttpEntity<Object>(body, requestHeaders);

        //Mock userService，创建用户
        when(this.userService.create(any())).thenReturn(1);  //返回创建数目
        //设置用户ID

        //发起请求
        ResponseEntity response = this.restTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class );

        //返回状态码应该等于200
        assertEquals(200,response.getStatusCode().value());
        //body数据，返回用户ID
        assertEquals("{\"userID\":0}",response.getBody());
    }

    /**
     * 修改密码，参数错误，应该返回400
     */
    @Test
    public void shouldReturn400WhenChangePwd() {
        url = "http://localhost:"+port+"/user";

        //创建body
        Map<String,Object> body = new HashMap();
        body.put("old","123");

        //创建headers
        MultiValueMap<String,String> requestHeaders = new LinkedMultiValueMap<>();
        //设置headers
        requestHeaders.add("Content-Type","application/json; charset=UTF-8");

        //设置http请求数据
        HttpEntity<?> httpEntity = new HttpEntity<Object>(body, requestHeaders);

        //发起请求
        ResponseEntity response = this.restTemplate.exchange(url, HttpMethod.PUT, httpEntity, String.class );

        //返回状态码应该等于400
        assertEquals(400,response.getStatusCode().value());
    }

    /**
     * 修改密码，用户未登录，应该返回401
     */
    @Test
    public void shouldReturn401WhenChangePwd() {
        url = "http://localhost:"+port+"/user";

        //创建body
        Map<String,Object> body = new HashMap();
        body.put("old","123");
        body.put("new","321");

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
     * 修改密码，用户未登录，应该返回401
     */
    @Test
    public void shouldReturn401WithDecodeJwtWhenChangePwd() {
        url = "http://localhost:"+port+"/user";

        //创建body
        Map<String,Object> body = new HashMap();
        body.put("old","123");
        body.put("new","321");

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
     * 修改密码，用户密码错误，应该返回403
     */
    @Test
    public void shouldReturn403WhenChangePwd() {
        url = "http://localhost:"+port+"/user";

        //创建body
        Map<String,Object> body = new HashMap();
        body.put("old","123");
        body.put("new","321");

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

        //查找用户，返回用户信息
        User user = new User();
        user.setPassword("abc");    //密码错误
        //Mock userService，获取用户信息
        when(userService.findByID(1)).thenReturn(user);

        //发起请求
        ResponseEntity response = this.restTemplate.exchange(url, HttpMethod.PUT, httpEntity, String.class );

        //返回状态码应该等于403
        assertEquals(403,response.getStatusCode().value());
    }

    /**
     * 修改密码，修改失败，应该返回500
     */
    @Test
    public void shouldReturn500WhenChangePwd() {
        url = "http://localhost:"+port+"/user";

        //创建body
        Map<String,Object> body = new HashMap();
        body.put("old","123");
        body.put("new","321");

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

        //查找用户，返回用户信息
        User user = new User();
        user.setPassword("123");
        //Mock userService，获取用户信息
        when(userService.findByID(1)).thenReturn(user);
        when(userService.update(any())).thenReturn(0);  //更新失败

        //发起请求
        ResponseEntity response = this.restTemplate.exchange(url, HttpMethod.PUT, httpEntity, String.class );

        //返回状态码应该等于500
        assertEquals(500,response.getStatusCode().value());
    }

    /**
     * 修改密码，修改成功，应该返回200和空对象
     */
    @Test
    public void shouldReturn200WhenChangePwd() {
        url = "http://localhost:"+port+"/user";

        //创建body
        Map<String,Object> body = new HashMap();
        body.put("old","123");
        body.put("new","321");

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

        //查找用户，返回用户信息
        User user = new User();
        user.setPassword("123");
        //Mock userService，获取用户信息
        when(userService.findByID(1)).thenReturn(user);
        when(userService.update(any())).thenReturn(1);  //更新成功

        //发起请求
        ResponseEntity response = this.restTemplate.exchange(url, HttpMethod.PUT, httpEntity, String.class );

        //返回状态码应该等于200
        assertEquals(200,response.getStatusCode().value());
        //返回空对象
        assertEquals("{}",response.getBody().toString());
    }

    /**
     * 永久封号，参数错误，应该返回400
     */
    @Test
    public void shouldReturn400WhenDeleteUser() {
        url = "http://localhost:"+port+"/user/abc"; //用户ID错误

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
     * 永久封号，用户未登录，应该返回401
     */
    @Test
    public void shouldReturn401WhenDeleteUser() {
        url = "http://localhost:"+port+"/user/12";

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
     * 永久封号，用户未登录，jwt解码为空，应该返回401
     */
    @Test
    public void shouldReturn401WithDecodeJwtWhenDeleteUser() {
        url = "http://localhost:"+port+"/user/12";

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

        when(jwt.decode(any())).thenReturn(null);

        //发起请求
        ResponseEntity response = this.restTemplate.exchange(url, HttpMethod.DELETE, httpEntity, String.class );

        //返回状态码应该等于401
        assertEquals(401,response.getStatusCode().value());
    }

    /**
     * 永久封号，登录用户不是管理员，应该返回403
     */
    @Test
    public void shouldReturn403WhenDeleteUser() {
        url = "http://localhost:"+port+"/user/12";

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
        auth.put("userStatus","teacher");   //不是管理员
        //Mock jwt，解码json web token
        when(jwt.decode(any())).thenReturn(auth);

        //发起请求
        ResponseEntity response = this.restTemplate.exchange(url, HttpMethod.DELETE, httpEntity, String.class );

        //返回状态码应该等于403
        assertEquals(403,response.getStatusCode().value());
    }

    /**
     * 永久封号，登录用户是管理员，封号用户不存在，应该返回404
     */
    @Test
    public void shouldReturn404WhenDeleteUser() {
        url = "http://localhost:"+port+"/user/12";

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
        auth.put("userStatus","admin");   //是管理员
        //Mock jwt，解码json web token
        when(jwt.decode(any())).thenReturn(auth);

        //Mock userService，获取用户信息
        when(userService.findByID(1)).thenReturn(null); //用户不存在

        //发起请求
        ResponseEntity response = this.restTemplate.exchange(url, HttpMethod.DELETE, httpEntity, String.class );

        //返回状态码应该等于404
        assertEquals(404,response.getStatusCode().value());
    }

    /**
     * 永久封号，封号失败，应该返回500
     */
    @Test
    public void shouldReturn500WhenDeleteUser() {
        url = "http://localhost:"+port+"/user/12";

        //创建body
        Map<String,Object> body = new HashMap();
        body.put("infinity",true);

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
        auth.put("userStatus","admin");   //是管理员
        //Mock jwt，解码json web token
        when(jwt.decode(any())).thenReturn(auth);

        //查找用户，返回用户信息
        User user = new User();
        //Mock userService，获取用户信息
        when(userService.findByID(12)).thenReturn(user); //用户存在

        //Mock userService，更新用户信息
        when(userService.update(any())).thenReturn(0);  //更新失败

        //发起请求
        ResponseEntity response = this.restTemplate.exchange(url, HttpMethod.DELETE, httpEntity, String.class );

        //返回状态码应该等于500
        assertEquals(500,response.getStatusCode().value());
    }

    /**
     * 临时封号，封号成功，应该返回200并返回空对象
     */
    @Test
    public void shouldReturn200WhenDeleteUser() {
        url = "http://localhost:"+port+"/user/12";

        //创建body
        Map<String,Object> body = new HashMap();
        body.put("infinity",false);
        body.put("lockEndDate", new Date(Calendar.getInstance().getTimeInMillis()+10000).getTime());

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
        auth.put("userStatus","admin");   //是管理员
        //Mock jwt，解码json web token
        when(jwt.decode(any())).thenReturn(auth);

        //查找用户，返回用户信息
        User user = new User();
        //Mock userService，获取用户信息
        when(userService.findByID(12)).thenReturn(user); //用户存在

        //Mock userService，更新用户信息
        when(userService.update(any())).thenReturn(1);  //更新成功

        //发起请求
        ResponseEntity response = this.restTemplate.exchange(url, HttpMethod.DELETE, httpEntity, String.class );

        //返回状态码应该等于200
        assertEquals(200,response.getStatusCode().value());
        //返回空对象
        assertEquals("{}",response.getBody().toString());
    }
}