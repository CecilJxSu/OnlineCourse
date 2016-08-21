package util;

import com.auth0.jwt.JWTSigner;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.JWTVerifyException;

import java.util.HashMap;

public class JWT {
    final String secret = "Bla Bla Bla, secret for jwt";
    final String issuer = "http://localhost";
    final long iat = System.currentTimeMillis() / 1000l;
    final long exp = iat + 60L;

    public String sign(HashMap<String, Object> claims){
        JWTSigner signer = new JWTSigner(secret);
        claims.put("iss", issuer);
        claims.put("exp", exp);
        claims.put("iat", iat);

        return signer.sign(claims);
    }

    public HashMap<String, Object> decode(String jwt){
        try {
            JWTVerifier verifier = new JWTVerifier(secret);
            Map<String,Object> claims = verifier.verify(jwt);
            return claims;
        } catch (JWTVerifyException e) {

            return new HashMap<String,Object>();
        }
    }
}