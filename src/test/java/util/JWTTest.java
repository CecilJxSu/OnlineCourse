package util;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertNotNull;

public class JWTTest {
    private JWT jwt = new JWT();
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
        String jwtString = jwt.sign(new HashMap<String,Object>());
        assertNotNull(jwtString);
    }

    @Test
    public void shouldDecodeEmpty(){
        String jwtString = jwt.sign(new HashMap<String,Object>());
        Map<String,Object> decode = jwt.decode(jwtString);
        assertNotNull(decode.toString());
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
        assertNotNull(decode.toString());
    }
}