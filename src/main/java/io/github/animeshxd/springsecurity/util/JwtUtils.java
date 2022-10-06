package io.github.animeshxd.springsecurity.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class JwtUtils {
    @Autowired
    private Key SECRET_KEY;

    public JwtUtils(Key SECRET_KEY) {
        this.SECRET_KEY = SECRET_KEY;
    }
    public JwtUtils() {}

    public String generateToken(UserDetails userDetails){
        return Jwts.builder().signWith(SECRET_KEY).setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 5 * 50 * 1000))
                .compact();
    }

    public boolean validateToken(String jws){
        try{
            Jwts.parserBuilder().setSigningKey(SECRET_KEY).build().parseClaimsJws(jws);
            return true;
        }catch (Exception e){
            return false;
        }
    }
    public Claims getClaims(String jws){
        return Jwts.parserBuilder().setSigningKey(SECRET_KEY).build().parseClaimsJws(jws).getBody();
    }
}
