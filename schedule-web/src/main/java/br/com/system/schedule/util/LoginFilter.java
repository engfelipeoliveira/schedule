package br.com.system.schedule.util;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import br.com.system.schedule.model.Usuario;

public class LoginFilter implements Filter {

	@Override
	public void destroy() {
		System.err.println("ssss");
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doFilter(ServletRequest arg0, ServletResponse arg1,
			FilterChain arg2) throws IOException, ServletException {
		
		Usuario usuario = null;
		HttpSession session = ((HttpServletRequest)arg0).getSession(false);
        
		if (session != null){
            usuario = (Usuario) session.getAttribute("usuarioLogado");
		}  
		
		
		if (usuario == null) {
			String contextPath = ((HttpServletRequest) arg0).getContextPath();
			((HttpServletResponse) arg1).sendRedirect(contextPath + "/index.jsf?faces-redirect=true");
		}else{
			arg2.doFilter(arg0, arg1);
		}

	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
		
	}

}
