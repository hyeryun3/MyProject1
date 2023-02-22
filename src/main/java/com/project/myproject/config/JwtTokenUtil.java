package com.project.myproject.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class JwtTokenUtil {

    public String createToken(String key, String id){

        Long expiredTime = 1000 * 60L * 60L * 2L; // 2시간
        return Jwts.builder()
                .signWith(SignatureAlgorithm.HS512, key.getBytes())
                .setHeaderParam("typ","JWT")
                .setSubject(id)
                .setIssuer("issuer")
                .setExpiration(new Date(new Date().getTime() + expiredTime))
                .setIssuedAt(new Date())
                .compact();
    }

    public Map<String,Object> verifyJWT(String jwtSecretKey, String authorization) throws UnsupportedEncodingException{

        Map<String, Object> claimMap = null;

        try{
            Claims claims = Jwts.parser()
                    .setSigningKey(jwtSecretKey.getBytes())
                    .parseClaimsJws( authorization)
                    .getBody();

            claimMap = claims;
        }catch (ExpiredJwtException e){
            System.out.println("토큰 만료");
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }

        return claimMap;
    }
}
