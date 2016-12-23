package cn.canlnac.course.util;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-context.xml"})
public class JWTTest {
    @Autowired
    protected JWT jwt;
    private static HashMap<String,Object> userData = new HashMap();

    @Before
    public void before(){
        userData.put("id", "1");
        userData.put("name", "cecil");
        userData.put("icon", "http://url.com");
        userData.put("gender", "male");
    }

    @Test
    public void shouldSignEmpty(){
        String jwtString = jwt.sign(new HashMap());
        assertNotNull(jwtString);
    }

    @Test
    public void shouldDecodeEmpty(){
        String jwtString = jwt.sign(new HashMap());
        Map<String,Object> decode = jwt.decode(jwtString);
        assertNotNull(decode);
    }

    @Test
    public void shouldSignUserData(){
        String jwtString = jwt.sign(userData);
        assertNotNull(jwtString);
    }

    @Test
    public void shouldDecodeUserData(){
        String jwtString = jwt.sign(userData);
        Map<String,Object> decode = jwt.decode(jwtString);
        assertEquals(decode.toString(),userData.toString());
    }

    @Test
    public void shouldReturnNullWhenIssuerIllegal(){
        Map<String,Object> decode = jwt.decode("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHBpcmVkIjoxNDc0MDE2MzA4LCJnZW5kZXIiOiJtYWxlIiwibmFtZSI6ImNlY2lsIiwiaWNvbiI6Imh0dHA6Ly91cmwuY29tIiwiaWQiOiIxIiwiaXNzdWVyIjoiaHR0cDovL2Vycm9yIn0.LpVKZaaZzMQdICPUnDyNTOW7YXgOjHLBnoiz8W8O4PI");
        assertNull(decode);
    }

    @Test
    public void shouldReturnNullWhenExpired(){
        Map<String,Object> decode = jwt.decode("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHBpcmVkIjoxNDczMDE2NTIzLCJnZW5kZXIiOiJtYWxlIiwibmFtZSI6ImNlY2lsIiwiaWNvbiI6Imh0dHA6Ly91cmwuY29tIiwiaWQiOiIxIiwiaXNzdWVyIjoiaHR0cDovL2xvY2FsaG9zdCJ9.PAiinR7fff1JvtAdcyQMCxh7KC-IRAP0f78vxM-9QZ8");
        assertNull(decode);
    }
}