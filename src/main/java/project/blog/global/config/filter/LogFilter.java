package project.blog.global.config.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.util.UUID;

@Order(1)
@Slf4j
@Component
public class LogFilter extends OncePerRequestFilter {

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();

        return path.contains("/api/login");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);

        String traceId = UUID.randomUUID().toString().substring(0, 8);
        MDC.put("traceId", traceId);

        long startTime = System.currentTimeMillis();

        try {
            filterChain.doFilter(requestWrapper, responseWrapper);
        } finally {
            long duration = System.currentTimeMillis() - startTime;

            logRequest(requestWrapper);
            logResponse(responseWrapper, duration);

            responseWrapper.copyBodyToResponse();
            MDC.clear();
        }
    }

    private void logRequest(ContentCachingRequestWrapper request) {
        String queryString = request.getQueryString();
        log.info("Request: [{}] URI: {}{}, Body: {}",
                request.getMethod(),
                request.getRequestURI(),
                queryString != null ? "?" + queryString : "",
                getContents(request.getContentAsByteArray(), request.getContentType())
        );
    }

    private void logResponse(ContentCachingResponseWrapper response, long duration) {
        log.info("Response: [Status: {}], Time: {}ms, Body: {}",
                response.getStatus(),
                duration,
                getContents(response.getContentAsByteArray(), response.getContentType())
        );
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

}
