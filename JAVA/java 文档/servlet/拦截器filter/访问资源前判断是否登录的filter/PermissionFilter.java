package com.tz.book.filter;

import java.io.IOException;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tz.book.entity.User;

/**
 * Servlet Filter implementation class PermissionFilter
 */
@WebFilter(filterName = "PermissionFilter", urlPatterns = "/permission/*", 
		   dispatcherTypes={DispatcherType.REQUEST})
public class PermissionFilter implements Filter {

    public PermissionFilter() {
        // TODO Auto-generated constructor stub
    }

	public void destroy() {
		// TODO Auto-generated method stub
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		
		HttpServletRequest req=(HttpServletRequest) request;
		HttpServletResponse resp=(HttpServletResponse) response;
		
		User user=(User) req.getSession().getAttribute("user");
		if(user==null){
			resp.sendRedirect(req.getContextPath()+"/jsp/server/login.jsp?info=4");
			return;
		}else{
			chain.doFilter(request, response);
		}
	}

	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
