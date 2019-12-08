package server.security.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class TokenHandler {

    @Value("${jwt.token.secret}")
    private String secret;

    @Value("${jwt.token.expired}")
    private Long validityInMilliseconds;

    private static final String KEY_USERNAME = "username";

//    @PostConstruct
//    protected void init() {
//        secret = Base64.getEncoder().encodeToString(secret.getBytes());
//    }

    public Optional<String> extractUsername(String token) {

        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
        Claims body = claimsJws.getBody();
        return Optional
                .ofNullable(body.getSubject());
    }

    public String createToken(String username) {

        Claims claims = Jwts.claims().setSubject(username);
//        claims.put(KEY_USERNAME, username);

        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);
//        Date validity = new Date(now.getTime() + 3600000);

        return Jwts.builder()//
                .setClaims(claims)//
                .setIssuedAt(now)//
                .setExpiration(validity)//
                .signWith(SignatureAlgorithm.HS256, secret)//
//                .signWith(SignatureAlgorithm.HS256, Base64.getEncoder().encodeToString("snejok".getBytes()))
                .compact();
    }

}
