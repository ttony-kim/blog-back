package project.blog.domain.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.blog.domain.auth.dto.LoginDto;
import project.blog.domain.member.entity.Member;
import project.blog.domain.member.repository.MemberRepository;
import project.blog.global.config.common.BCryptEncryptor;
import project.blog.global.config.common.ErrorCode;
import project.blog.global.config.security.JwtProvider;
import project.blog.global.exception.custom.UnauthorizedException;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final MemberRepository memberRepository;
    private final BCryptEncryptor bCryptEncryptor;
    private final JwtProvider jwtProvider;

    public String authenticate(LoginDto loginDto) {
        Member member = memberRepository.findByEmail(loginDto.getEmail())
                .orElseThrow(() -> new UnauthorizedException(ErrorCode.INVALID_EMAIL_OR_PASSWORD));

        if (!bCryptEncryptor.checkPassword(loginDto.getPassword(), member.getPassword())) {
            throw new UnauthorizedException(ErrorCode.INVALID_EMAIL_OR_PASSWORD);
        }

        return jwtProvider.generateToken(member);
    }

}
