package project.blog.global.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtProvider {

    private long accessExpiration = 1000L * 60 * 5;
    private final SecretKey secretKey;


    public JwtProvider(String secretKey) {
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(secretKey));
    }

    public String createAccessToken() {
        return Jwts.builder()
                .claim("id", "admin")
                .issuer("som") // 발급자
                .issuedAt(new Date()) // 발급시간
                .expiration(new Date(System.currentTimeMillis()))//+ accessExpiration)) // 토큰의 만료 시간 (5분)
                .signWith(secretKey)
                .compact();
    }

    public void verifyToken(String jwtToken) {
        Jws<Claims> claimsJws = Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(jwtToken);

        Claims payload = claimsJws.getPayload();
        System.out.println(payload.get("id", String.class));

    }

    public static void main(String[] args) throws InterruptedException {
        JwtProvider jp = new JwtProvider("secretKeycheckififiddddfffffffffffdddddfifififififii");

        String jwtToken = jp.createAccessToken();
        Thread.sleep(1000);
        jp.verifyToken(jwtToken);
    }
}
