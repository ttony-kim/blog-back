package project.blog.support;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import project.blog.domain.member.entity.Member;
import project.blog.domain.member.repository.MemberRepository;
import project.blog.global.config.security.JwtProvider;

@Component
public class TestJwtTokenProvider {

    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;
    private Member testMember;

    public TestJwtTokenProvider(MemberRepository memberRepository, JwtProvider jwtProvider) {
        this.memberRepository = memberRepository;
        this.jwtProvider = jwtProvider;
    }

    @PostConstruct
    public void init() {
        memberRepository.deleteAll();
        testMember = memberRepository.save(Member.of("test", "password", "tester"));
    }

    public String getToken() {
        return jwtProvider.generateToken(testMember);
    }

}

