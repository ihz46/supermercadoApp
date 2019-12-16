package com.formacion.ipartek.supermercado.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;




/**
 * Servlet implementation class LogoutController
 */
@WebServlet("/logout")
public class LogoutController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	//private final static Logger log = Logger.getLogger(LogoutController.class);   
  
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//Obtenemos la sesión de la request
		HttpSession session = request.getSession();
		//Eliminamos el atributo usuarioLogueado de la sesión
		session.removeAttribute("usuarioLogueado");
		//Invalidamos la sesión
		session.invalidate();
		
		request.setAttribute("mensajeAlerta", new Alerta(Alerta.TIPO_PRIMARY, "Gracias por tu visita, te echaremos de menos."));
		request.getRequestDispatcher("login.jsp").forward(request, response);
	}

}
