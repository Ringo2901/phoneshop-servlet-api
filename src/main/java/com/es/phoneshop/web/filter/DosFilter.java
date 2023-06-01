package com.es.phoneshop.web.filter;

import com.es.phoneshop.model.product.service.security.DosProtectionService;
import com.es.phoneshop.model.product.service.security.impl.DefaultDosProtectionService;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class DosFilter implements Filter {
    private DosProtectionService filterService;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        filterService = DefaultDosProtectionService.getInstance();
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        if (filterService.isAllowed(servletRequest.getRemoteAddr())) {
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            ((HttpServletResponse) servletResponse).setStatus(429);
        }
    }

    @Override
    public void destroy() {

    }
}
