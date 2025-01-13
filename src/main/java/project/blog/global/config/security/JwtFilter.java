package project.blog.global.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import project.blog.global.dto.ErrorResponse;

import java.io.IOException;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtFilter implements Filter {

    private static final List<String> IGNORE_URI = List.of("/api/login");
    private static final String HEADER_KEY = "Authorization";
    private static final String PREFIX = "Bearer ";

    private final JwtProvider jwtProvider;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        String requestURI = httpServletRequest.getRequestURI();

        chain.doFilter(request, response);
//
//        if (IGNORE_URI.contains(requestURI)) {
//            log.info("Ignore Page: {}", requestURI);
//            chain.doFilter(request, response);
//            return;
//        }
//
//        String bearerToken = httpServletRequest.getHeader(HEADER_KEY);
//        if (!(StringUtils.hasText(bearerToken) && bearerToken.startsWith(PREFIX))) {
//            setErrorResponse(httpServletResponse, "토큰이 존재하지 않습니다.", "NO_TOKEN_PROVIDED");
//            return;
//        }
//
//        try {
//            jwtProvider.validateToken(bearerToken.substring(PREFIX.length()));
//            chain.doFilter(request, response);
//        } catch (SignatureException | MalformedJwtException e) {
//            e.printStackTrace();
//            setErrorResponse(httpServletResponse, "유효하지 않은 토큰입니다.", "INVALID_TOKEN");
//        } catch (ExpiredJwtException e) {
//            e.printStackTrace();
//            setErrorResponse(httpServletResponse, "만료된 토큰입니다.", "EXPIRED_TOKEN");
//        } catch (Exception e) {
//            e.printStackTrace();
//            setErrorResponse(httpServletResponse, "잘못된 토큰입니다.", "MALFORMED_TOKEN");
//        }
    }

    private void setErrorResponse(HttpServletResponse response, String message, String errorCode) throws IOException {
        ErrorResponse errorResponse = new ErrorResponse(message, errorCode);

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(new ObjectMapper().writeValueAsString(errorResponse));
    }

}
