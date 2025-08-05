package project.blog.global.config.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import project.blog.domain.member.entity.Member;
import project.blog.domain.member.repository.MemberRepository;
import project.blog.global.exception.custom.UnauthorizedException;

import javax.crypto.SecretKey;
import java.util.Date;

@Slf4j
@Component
public class JwtProvider {

    private long accessExpiration = 1000L * 60 * 100;
    private final SecretKey secretKey;
    private final MemberRepository memberRepository;

    public JwtProvider(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
        byte[] keyBytes = Decoders.BASE64URL.decode("SecretKeySecretKeySecretKeySecretKeySecretKey");
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(Member member) {
        return Jwts.builder()
                .claim("email", member.getEmail())
                .issuer(member.getName()) // 발급자
                .issuedAt(new Date()) // 발급시간
                .expiration(new Date(System.currentTimeMillis() + accessExpiration)) // 토큰의 만료 시간 (5분)
                .signWith(secretKey)
                .compact();
    }

    public void validateToken(String token) {
        Claims claims = getClaims(token);
        String email = claims.get("email", String.class);

        memberRepository.findByEmail(email).orElseThrow(() -> new UnauthorizedException("Invalid token"));
    }

    private Claims getClaims(String jwtToken) {
        Jws<Claims> claimsJws = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(jwtToken);

        return claimsJws.getPayload();
    }

}
