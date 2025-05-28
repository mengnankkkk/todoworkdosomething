package com.mengnankk.designpattern;

import org.omg.PortableInterceptor.INACTIVE;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

@WebFilter("/*")
public class RequestLogFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) {

    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        System.out.println("记录请求日志");
        chain.doFilter(req, res);
    }

    @Override
    public void destroy() {
        // 资源释放逻辑（可选）
    }
}

