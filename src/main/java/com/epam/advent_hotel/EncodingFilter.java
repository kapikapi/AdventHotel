package com.epam.advent_hotel;

import org.apache.log4j.Logger;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

/**
 * Created by Elizaveta Kapitonova on 30.01.16.
 */
@WebFilter(filterName = "EncodingFilter")
public class EncodingFilter implements Filter {
    public static final Logger LOG= Logger.getLogger(EncodingFilter.class);

    private String encoding = "UTF-8";

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
            throws ServletException, IOException {
        LOG.debug("Encoding Filter");
        req.setCharacterEncoding(encoding);
        chain.doFilter(req, resp);

    }
    @Override
    public void init(FilterConfig config) throws ServletException {
        String encodingParam = config.getInitParameter("encoding");
        if (encodingParam != null) {
            encoding = encodingParam;
        }

    }

}
