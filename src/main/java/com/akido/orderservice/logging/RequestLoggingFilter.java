package com.akido.orderservice.logging;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RequestLoggingFilter extends OncePerRequestFilter {
    private static final String REQUEST_ID = "requestId";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain
    ) throws ServletException, IOException {
        UUID requestId = UUID.randomUUID();

        MDC.put(REQUEST_ID, requestId.toString());

        long started = System.nanoTime();

        try {
            log.info(
                    "Request id={} started method={} uri={}",
                    requestId,
                    request.getMethod(),
                    request.getRequestURI()
            );

            filterChain.doFilter(request, response);

            long durationMs = TimeUnit.NANOSECONDS.toMillis(
                    System.nanoTime() - started
            );

            String errorMessage = (String) request.getAttribute("errorMessage");

            if (response.getStatus() >= 400) {
                log.error(
                        "Request id={} completed with error message={} method={} uri={} status={} durationMs={}",
                        requestId,
                        errorMessage,
                        request.getMethod(),
                        request.getRequestURI(),
                        response.getStatus(),
                        durationMs
                );
            } else {
                log.info(
                        "Request id={} completed method={} uri={} status={} durationMs={}",
                        requestId,
                        request.getMethod(),
                        request.getRequestURI(),
                        response.getStatus(),
                        durationMs
                );
            }

        } catch (Exception exception) {

            long durationMs = TimeUnit.NANOSECONDS.toMillis(
                    System.nanoTime() - started
            );

            log.error(
                    "Request id={} failed message={} method={} uri={} durationMs={}",
                    requestId,
                    exception.getMessage(),
                    request.getMethod(),
                    request.getRequestURI(),
                    durationMs,
                    exception
            );

            throw exception;

        } finally {
            MDC.remove(REQUEST_ID);
        }
    }
}
