package com.web.filter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;


@WebFilter(filterName = "CharacterEncodingFilter", urlPatterns = "/*", 
           initParams = {@WebInitParam(name = "req", value = "utf-8"),
        		         @WebInitParam(name = "resp", value = "utf-8")},
           dispatcherTypes={DispatcherType.REQUEST,DispatcherType.FORWARD})
public class CharacterEncodingFilter implements Filter {

	private String requestCharset;
	private String responseCharset;
	
    public CharacterEncodingFilter() {
        // TODO Auto-generated constructor stub
    }

	public void init(FilterConfig fConfig) throws ServletException {
		requestCharset = fConfig.getInitParameter("req");
		responseCharset = fConfig.getInitParameter("resp");
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;
		
		req.setCharacterEncoding(requestCharset);
		resp.setCharacterEncoding(responseCharset);
		resp.setContentType("text/html;charset="+responseCharset);
		chain.doFilter(new MyRequest(req), resp);//MyRequest重写了request.getparameter()方法;
	}
	
	public void destroy() {
		// TODO Auto-generated method stub
	}
	
	/*
	1.写一个类，实现与被增强对象相同的接口
	2.定义一个变量，记住被增强对象
	3.定义一个构造方法，接收被增强对象
	4.覆盖想增强的方法
	5.对于不想增强的方法，直接调用被增强对象（目标对象）的方法
	 */
	//实现HttpServletRequest接口，sun公司已写好默认实现类HttpServletRequestWrapper,继承它就好了
	class MyRequest extends HttpServletRequestWrapper{

		private HttpServletRequest request;
		public MyRequest(HttpServletRequest request) {
			super(request);//传递request给包装类来重写所有方法
			this.request = request;
		}
		@Override
		public String getParameter(String name) {
			//调用传递进来的request对象的getParameter方法，获取value
			String value = this.request.getParameter(name);
			//如果不是get方式提及的，直接返回value
			if(!request.getMethod().equalsIgnoreCase("get")){
				return value;
			}
			//如果value为null,也不需要转码
			if(value==null){
				return null;
			}
			//如果是get方式提交的，则需要反向编码
			try {
				return value = new String(value.getBytes("iso8859-1"),request.getCharacterEncoding());
			} catch (UnsupportedEncodingException e) {
				throw new RuntimeException(e);
			}
			
		}
		
	}

}
