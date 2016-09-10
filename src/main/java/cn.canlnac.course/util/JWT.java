package cn.canlnac.course.util;

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
    final String issuer = "http://localhost";

    /**
     * 对claims签名
     * @param claims    待签名数据
     * @return
     */
    public String sign(HashMap<String, Object> claims){
        Map<String, Object> tmpClaims = (HashMap)claims.clone();

        JWTSigner signer = new JWTSigner(secret);
        long created = System.currentTimeMillis() / 1000L;
        long expired = created + 7 * 24 * 60 * 60L;
        tmpClaims.put("issuer", issuer);
        tmpClaims.put("expired", expired);

        return signer.sign(tmpClaims);
    }

    /**
     * 对jwt解码
     * @param jwt   json web toke 字符串
     * @return
     */
    public Map<String, Object> decode(String jwt){
        JWTVerifier verifier;
        Map<String,Object> claims;

        try {
            verifier = new JWTVerifier(secret);
            claims = new HashMap(verifier.verify(jwt));
        } catch (JWTVerifyException e) {
            e.printStackTrace();
            return null;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (SignatureException e) {
            e.printStackTrace();
            return null;
        } catch (InvalidKeyException e) {
            e.printStackTrace();
            return null;
        }

        long current = System.currentTimeMillis() / 1000L;
        //验证issuer和expired是否过期的
        if(!claims.get("issuer").equals(issuer) ||
            Long.valueOf(claims.get("expired").toString()) < current){
           return null;
        }

        claims.remove("issuer");
        claims.remove("expired");
        return claims;
    }
}