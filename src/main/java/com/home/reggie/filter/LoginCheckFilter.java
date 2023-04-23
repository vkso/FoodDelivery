package com.home.reggie.filter;

import com.alibaba.fastjson.JSON;
import com.home.reggie.common.BaseContext;
import com.home.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 检查用户是否已经登录过滤器
 */
@WebFilter(filterName = "loginCheckFilter", urlPatterns = "/*")
@Slf4j
public class LoginCheckFilter implements Filter {
    // 地址匹配器
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        // 1. 获取本次请求的 URI
        String requestURI = request.getRequestURI();

//        log.info("拦截到请求：{}", requestURI);

        // 如果用户访问的是如下路径，不需要处理，直接放行(对于非controller的静态页面请求，可以直接放行)
        String[] urls = new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**",
                "/common/**"
        };

        // 2. 判断本次请求是否需要处理(使用类中的 check 方法进行匹配)
        boolean check = check(urls, requestURI);

        // 3. 如果不需要处理，直接放行
        if (check) {
//            log.info("本次请求：{} 不需要处理",requestURI);
            filterChain.doFilter(request, response);
            return;
        }

        // 4. 判断登录状态，如果已经登录，直接放行
        if (request.getSession().getAttribute("employee") != null) {
//            log.info("用户已登录，用户ID为：{}", request.getSession().getAttribute("employee"));
            // 将 empId 保存到 ThreadLocal 线程池的局部变量存储空间中
            Long empId = (Long) request.getSession().getAttribute("employee");
            BaseContext.setCurrentId(empId);

            filterChain.doFilter(request, response);
            return;
        }

        // 5. 如果为登录，则返回登录结果(通过输出流的方式，向客户端页面响应数据)
//        log.info("用户未登录");
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));

        // 如下代码可以拦截到所有的请求，可以在没有处理逻辑的时候，测试是否拦截到所有请求
//        log.info("拦截到请求：{}", request.getRequestURI());
//        filterChain.doFilter(request, response);
    }

    /**
     * 路径匹配，检查本次请求是否需要放行
     *
     * @param urls
     * @param requestURI
     * @return
     */
    public boolean check(String[] urls, String requestURI) {
        for (String url : urls) {
            boolean match = PATH_MATCHER.match(url, requestURI);
            if (match) {
                return true;
            }
        }
        return false;
    }
}























