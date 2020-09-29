package com.jwt.verification;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Base64;
import java.util.Date;
import java.util.Map;

@Slf4j
@Component
public class JwtFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        String auth = httpServletRequest.getHeader("Authorization");
        if (null == auth) {
            httpServletResponse.sendError(401);
        } else if (verifyToken(auth)) {
            // fetch user details
            filterChain.doFilter(httpServletRequest, httpServletResponse);
        } else {
            httpServletResponse.sendError(401);
        }
    }

    private boolean verifyToken(String auth) {
        Claims claims;
        try{
           claims = Jwts.parser().setSigningKey(encode("abcd".getBytes())).parseClaimsJws(auth).getBody();

        }catch (ExpiredJwtException | SignatureException e){
            return false;
        }
        Map<String, Object> header = Jwts.parser().setSigningKey(encode("abcd".getBytes())).parse(auth).getHeader();
        return isTokenNotExpired(claims.getExpiration()) && isSignatureVerified(claims, header, auth);
    }

    private boolean isSignatureVerified(Claims claims, Map<String, Object> header, String auth) {
        return Jwts.builder().signWith(SignatureAlgorithm.HS256, encode("abcd".getBytes())).setHeader(header).setClaims(claims).setExpiration(claims.getExpiration()).compact().equals(auth);

    }

    private boolean isTokenNotExpired(Date exp) {
        return exp.getTime() > System.currentTimeMillis();
    }

    private static String encode(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }

}
