package utils;

import com.auth0.jwt.JWTSigner;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.JWTVerifyException;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.HashMap;
import java.util.Map;

/**
 * Json web token
 */
public class JWT {
    final String secret = "Bla Bla Bla, secret for jwt";

    /**
     * 对claims签名
     * @param claims    待签名数据
     * @return
     */
    public String sign(HashMap<String, Object> claims){
        JWTSigner signer = new JWTSigner(secret);
        long created = System.currentTimeMillis() / 1000l;
        long expired = created + 7 * 24 * 60 * 60L;
        claims.put("expired", expired);

        return signer.sign(claims);
    }

    /**
     * 对jwt解码
     * @param jwt   json web toke 字符串
     * @return
     */
    public Map<String, Object> decode(String jwt){
        try {
            JWTVerifier verifier = new JWTVerifier(secret);
            Map<String,Object> claims = verifier.verify(jwt);
            long current = System.currentTimeMillis() / 1000l;
            if (claims.get("expired") <= current){

            }
            return claims;
        } catch (JWTVerifyException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SignatureException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }

        return new HashMap<String,Object>();
    }
}