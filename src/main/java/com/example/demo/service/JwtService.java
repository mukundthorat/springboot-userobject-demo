package com.example.demo.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {
    @Value("${security.jwt.secret-key}")
    private String secretKey;

    @Value("${security.jwt.expiration-time}")
    private long jwtExpiration;

    public long getExpirationTime() {
        return jwtExpiration;
    }


    //helper class --> getSignInKey
    private Key getSignInkey(){
        byte[] keyBytes= Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    //build token
    private String buildToken(
            Map<String,Object> extraClaims,
            UserDetails userDetails,
            long expiration
    ){
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() +expiration))
                .signWith(getSignInkey(), SignatureAlgorithm.HS256)
                .compact();
    }

//---------------------------------------------------------------------------------------

    //helper class --> extractAllClaims
    private Claims extractAllClaims(String token){
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInkey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    //helper class --> extractClaim
    public <T> T extractClaim(String token,Function<Claims,T> claimsResolver){
        final Claims claims=extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    //helper class---> extractUsername from the token
    public String extractUsername(String token){
        return extractClaim(token,Claims::getSubject);
    }

//---------------------------------------------------------------------------------------------



    // ****getExpirationTime().
    //helper class---> extractExpiration class
    private Date extractExpiration(String token){
        return extractClaim(token,Claims::getExpiration);
    }

    //helper class---> isTokenExpired
    private boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }

    // ***isTokenValid() and
    public boolean isTokenValid(String token,UserDetails userDetails){
        final String username=extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }
//---------------------------------------------------------------------------
    // ***generateToken(),
    public String generateToken(UserDetails userDetails){
        return  generateToken(new HashMap<>(),userDetails);
    }

    // ***generateToken()
    public String generateToken(Map<String,Object> extraClaims,UserDetails userDetails) {
        return buildToken(extraClaims,userDetails,jwtExpiration);
    }


}
//-----------------------------------------------------------------------------

