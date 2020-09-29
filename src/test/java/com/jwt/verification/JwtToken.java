package com.jwt.verification;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.json.JSONException;
import org.testng.annotations.Test;

import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Test(groups = "unit")
public class JwtToken {

    @Test
    public void generateToken() {

        Claims claim = Jwts.claims();
        claim.put("userId", "r0j00cp");
        Map<String, Object> header = new HashMap<>();
        header.put("alg", "HS256");
        header.put("typ", "JWT");

        String encodedSecret = Base64.getEncoder().encodeToString("abcd".getBytes());
        System.out.println("Encoded secret : " + encodedSecret);
        System.out.println(Jwts.builder()
                .setClaims(claim).setHeader(header)
                .setExpiration(new Date(System.currentTimeMillis() + 2400 * 1000))
                .signWith(SignatureAlgorithm.HS256, encodedSecret)
                .compact());
    }

    @Test
    public void mapToString() throws JSONException {
        Map<String, Object> header = new HashMap<>();
        header.put("alg", "HS256");
        header.put("typ", "JWT");

        System.out.println(header.toString());
    }

}
