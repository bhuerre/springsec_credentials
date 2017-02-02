package com.ixtechsol.sec.security;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Optional;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

@Component
public class CustomFilterLogging extends GenericFilterBean {

    private final Logger log = Logger.getLogger(CustomFilterLogging.class);

    @Override
    public void doFilter(final ServletRequest servletRequest, final ServletResponse servletResponse, final FilterChain filterChain) throws IOException, ServletException {
    	//Custom section adding a log step
        final HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        final String url = httpServletRequest.getRequestURL().toString();
        final String queryString = Optional.ofNullable(httpServletRequest.getQueryString()).map(value -> "?" + value).orElse("");
        log.info(String.format("Applying specific filter logic found in LssLoggingFilter for URI: %s%s", url, queryString));
        
        // Regular filter
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
