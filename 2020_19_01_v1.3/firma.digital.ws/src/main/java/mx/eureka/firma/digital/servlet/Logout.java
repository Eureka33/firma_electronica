package mx.eureka.firma.digital.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class Logout extends BaseServlet {
    
    private static final long serialVersionUID = 2389655495605997697L;

    
	public Logout() {
		super();
	}

    
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getSession().invalidate();
        
        response.sendRedirect( "firmaDocumento?ts=" + Math.random());
	}
    
}