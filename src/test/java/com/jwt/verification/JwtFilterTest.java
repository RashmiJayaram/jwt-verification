package com.jwt.verification;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.json.JSONException;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.filter.OncePerRequestFilter;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@Test(groups = "unit")
public class JwtFilterTest {

    @InjectMocks
    private JwtFilter jwtFilter;
    private MockMvc mockMvc;
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private MockFilterChain chain;
    private OncePerRequestFilter filter;

    @BeforeClass
    public void setup() {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = standaloneSetup(jwtFilter).build();
        this.request = new MockHttpServletRequest();
        Claims claim = Jwts.claims();
        claim.put("userId", "r0j00cp");
        Map<String, Object> header = new HashMap<>();
        header.put("alg", "HS256");
        header.put("typ", "JWT");
        String encodedSecret = Base64.getEncoder().encodeToString("abcd".getBytes());
        this.request.addHeader("Authorization",Jwts.builder()
                .setClaims(claim).setHeader(header)
                .setExpiration(new Date(System.currentTimeMillis() + 2400 * 1000))
                .signWith(SignatureAlgorithm.HS256, encodedSecret)
                .compact());

        //The following is an expired token. It can be used to test the unauthorised scenario
        //eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOiJyMGowMGNwIiwiZXhwIjoxNjAxMzgzNjE5fQ.QrfZVnGa3KOHDyrOZ3spZtd0Z8kbx4eltgwST9Tg3xg

        //insted of "abcd", a different secret can be passed here to test the wrong secret scenario

        this.response = new MockHttpServletResponse();
        this.chain = new MockFilterChain();
        this.filter = new JwtFilter();

    }

    @Test
    public void filterTest() throws ServletException, IOException {

        JwtFilter jwtFilter=new JwtFilter();
        jwtFilter.doFilterInternal(this.request, this.response, this.chain);
        Assert.assertEquals(this.response.getStatus(),200);

    }

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