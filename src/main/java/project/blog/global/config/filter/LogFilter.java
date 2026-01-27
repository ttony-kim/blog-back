package project.blog.global.config.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingResponseWrapper;
import project.blog.global.config.common.ReadableRequestWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Order(1)
@Slf4j
@Component
public class LogFilter extends OncePerRequestFilter {

    private static final int MAX_LOG_LENGTH= 2000;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return request.getRequestURI().contains("/api/login");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        ReadableRequestWrapper requestWrapper = new ReadableRequestWrapper(request);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);

        String traceId = UUID.randomUUID().toString().substring(0, 8);
        MDC.put("traceId", traceId);

        long startTime = System.currentTimeMillis();

        try {
            logRequest(requestWrapper);
            filterChain.doFilter(requestWrapper, responseWrapper);
        } finally {
            long duration = System.currentTimeMillis() - startTime;
            logResponse(responseWrapper, duration);

            responseWrapper.copyBodyToResponse();
            MDC.clear();
        }
    }

    private void logRequest(ReadableRequestWrapper request) {
        String contentType = request.getContentType();

        String requestBody;
        if (contentType != null && contentType.contains("multipart")) {
            requestBody = formatParameterMap(request.getParameterMap());
        } else {
            byte[] contents = request.getContentAsByteArray();
            requestBody = contents.length > 0
                    ? new String(contents, StandardCharsets.UTF_8)
                    : "Empty Body";
        }

        log.info("Request: [{}] URI: {}{}, IP: {}, UA: {}, Body: {}",
                request.getMethod(),
                request.getRequestURI(),
                request.getQueryString() != null ? "?" + request.getQueryString() : "",
                getClientIp(request),
                request.getHeader("User-Agent"),
                requestBody
        );
    }

    private void logResponse(ContentCachingResponseWrapper response, long duration) {
        String body = getContents(response.getContentAsByteArray(), response.getContentType());

        log.info("Response: [Status: {}], Time: {}ms, Body: {}",
                response.getStatus(),
                duration,
                truncate(body)
        );
    }

    private String formatParameterMap(Map<String, String[]> map) {
        return map.entrySet().stream()
                .map(entry -> entry.getKey() + "=" + Arrays.toString(entry.getValue()))
                .collect(Collectors.joining(", ", "{", "}"));
    }

    private String getClientIp(HttpServletRequest request) {
        String[] headerNames = {
                "X-Real-IP",
                "X-Forwarded-For",
                "Proxy-Client-IP",
                "WL-Proxy-Client-IP",
                "HTTP_CLIENT_IP",
                "HTTP_X_FORWARDED_FOR",
        };

        for (String header : headerNames) {
            String ip = request.getHeader(header);
            if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
                return ip.contains(",") ? ip.split(",")[0] : ip;
            }
        }

        return request.getRemoteAddr();
    }

    private String getContents(byte[] contents, String contentType) {
        if (contents.length == 0) {
            return "Empty";
        }

        if (contentType != null && (contentType.contains("multipart") || contentType.contains("image"))) {
            return "Binary Data";
        }

        try {
            return new String(contents, "UTF-8");
        } catch (Exception e) {
            return "Unknown/Binary Content";
        }
    }

    private String truncate(String content) {
        if (content == null) return null;
        return content.length() > MAX_LOG_LENGTH
                ? content.substring(0, MAX_LOG_LENGTH) + "...(truncated)"
                : content;
    }

}
