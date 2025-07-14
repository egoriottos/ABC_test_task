package org.abc_berkut_test_task.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {
    @Value("${security.secret}")
    private String secret;

    @Value("${security.expiration}")
    private long jwtExpiration;

//    public <T> T extractClaim(String jwt, Function<Claims, T> claimsResolver) {
//        final Claims claims = extractAllClaims(jwt);
//        return claimsResolver.apply(claims);
//    }

    private Claims extractAllClaims(String jwt) {
        return Jwts
                .parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(jwt)
                .getPayload();

    }

    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(Map<String, Object> claims, UserDetails userDetails) {
        return Jwts
                .builder()
                .claims(claims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(getSignInKey(), Jwts.SIG.HS256)
                .compact();
    }

    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    public boolean isTokenValid(String jwt, UserDetails userDetails) {
        final String username = extractLogin(jwt);
        return (username.equals(userDetails.getUsername())) && isTokenExpired(jwt);
    }

    private boolean isTokenExpired(String jwt) {
        Date expiration = Jwts.parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(jwt)
                .getPayload()
                .getExpiration();
        return !expiration.before(new Date());
    }
//    public String extractLogin(String jwt) {
//        return extractClaim(jwt, Claims::getSubject);
//    }

    public String extractLogin(String jwt) {
        return Jwts.parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(jwt)
                .getPayload()
                .getSubject();
    }
}
