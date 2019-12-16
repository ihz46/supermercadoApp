package com.formacion.ipartek.supermercado.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.formacion.ipartek.supermercado.modelo.pojo.Usuario;


/**
 * Servlet implementation class LoginController
 */
@WebServlet("/login")
public class LoginController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String USUARIO = "admin";
	private static final String PASSWORD = "admin";
	
	//private final static Logger log = Logger.getLogger(PerrosController.class);   
  
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String view = "login.jsp";
		//1. Recogemos los parametros de la request
		
		String nombre = request.getParameter("nombre");
		String password = request.getParameter("password");
		
		
		try {
			if (USUARIO.equalsIgnoreCase(nombre) && PASSWORD.equals(password)) {
				Usuario user = new Usuario(nombre, password);
				HttpSession session = request.getSession();
				
				session.setAttribute("usuarioLogueado", user);
				session.setMaxInactiveInterval(60*5);
				//base
				
				
				view = "seguridad/index.jsp";
			}else {
				request.setAttribute("mensajeAlerta",new Alerta(Alerta.TIPO_DANGER, "Credenciales incorrectas, prueba de nuevo"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			request.getRequestDispatcher(view).forward(request, response);
		}
		
	}

}
