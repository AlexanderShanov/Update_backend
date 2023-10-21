package simple.config;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter("/*")
public class AddResponseHeaderFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setHeader("Access-Control-Allow-Origin", "*");
        //httpServletResponse.setHeader("Access-Control-Allow-Headers","application/json, Content-Type, " +
        //        "origin, content-type, accept, authorization, X-Auth-Token");
        httpServletResponse.setHeader("Access-Control-Allow-Headers","*");
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "*");
        chain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // ...
    }

    @Override
    public void destroy() {
        // ...
    }
}
