package mx.eureka.firma.digital.servlet;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class BaseServlet extends HttpServlet {
    
    
	protected void forwardTo( HttpServletRequest request, HttpServletResponse response, String pagina) throws IOException, ServletException {	
		final RequestDispatcher dispatcher = getServletContext().getRequestDispatcher( pagina);
		dispatcher.forward( request, response);
	}
    
}
