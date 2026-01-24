package project.blog.global.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import project.blog.global.config.common.ErrorCode;
import project.blog.global.config.properties.ApiAuthRoutesProperties;
import project.blog.global.config.properties.ApiAuthRoutesProperties.Route;
import project.blog.global.dto.ErrorResponse;

import java.io.IOException;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private static final String HEADER_KEY = "Authorization";
    private static final String PREFIX = "Bearer ";

    private final JwtProvider jwtProvider;
    private final ApiAuthRoutesProperties apiAuthRoutes;
    private final ObjectMapper objectMapper;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String requestURI = request.getRequestURI();
        String method = request.getMethod();

        return !isAuthRequired(requestURI, method);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String bearerToken = request.getHeader(HEADER_KEY);

        if (!(StringUtils.hasText(bearerToken) && bearerToken.startsWith(PREFIX))) {
            setErrorResponse(response, ErrorCode.NO_TOKEN_PROVIDED);
            return;
        }

        try {
            String token = bearerToken.substring(PREFIX.length());
            jwtProvider.validateToken(token);

            filterChain.doFilter(request, response);
        } catch (SignatureException | MalformedJwtException e) {
            e.printStackTrace();
            setErrorResponse(response, ErrorCode.INVALID_TOKEN);
        } catch (ExpiredJwtException e) {
            e.printStackTrace();
            setErrorResponse(response, ErrorCode.EXPIRED_TOKEN);
        } catch (JwtException e) {
            e.printStackTrace();
            setErrorResponse(response, ErrorCode.MALFORMED_TOKEN);
        }

    }

    private void setErrorResponse(HttpServletResponse response, ErrorCode errorCode) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(ErrorResponse.of(errorCode)));
    }

    private boolean isAuthRequired(String path, String method) {
        List<Route> routes = apiAuthRoutes.getRoutes();
        for (Route route : routes) {
            if (path.matches(route.getPath()) && method.equals(route.getMethod())) {
                return true;
            }
        }
        return false;
    }

}
