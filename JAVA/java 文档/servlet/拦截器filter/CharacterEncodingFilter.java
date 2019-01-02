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
		chain.doFilter(new MyRequest(req), resp);//MyRequest��д��request.getparameter()����;
	}
	
	public void destroy() {
		// TODO Auto-generated method stub
	}
	
	/*
	1.дһ���࣬ʵ���뱻��ǿ������ͬ�Ľӿ�
	2.����һ����������ס����ǿ����
	3.����һ�����췽�������ձ���ǿ����
	4.��������ǿ�ķ���
	5.���ڲ�����ǿ�ķ�����ֱ�ӵ��ñ���ǿ����Ŀ����󣩵ķ���
	 */
	//ʵ��HttpServletRequest�ӿڣ�sun��˾��д��Ĭ��ʵ����HttpServletRequestWrapper,�̳����ͺ���
	class MyRequest extends HttpServletRequestWrapper{

		private HttpServletRequest request;
		public MyRequest(HttpServletRequest request) {
			super(request);//����request����װ������д���з���
			this.request = request;
		}
		@Override
		public String getParameter(String name) {
			//���ô��ݽ�����request�����getParameter��������ȡvalue
			String value = this.request.getParameter(name);
			//�������get��ʽ�ἰ�ģ�ֱ�ӷ���value
			if(!request.getMethod().equalsIgnoreCase("get")){
				return value;
			}
			//���valueΪnull,Ҳ����Ҫת��
			if(value==null){
				return null;
			}
			//�����get��ʽ�ύ�ģ�����Ҫ�������
			try {
				return value = new String(value.getBytes("iso8859-1"),request.getCharacterEncoding());
			} catch (UnsupportedEncodingException e) {
				throw new RuntimeException(e);
			}
			
		}
		
	}

}
