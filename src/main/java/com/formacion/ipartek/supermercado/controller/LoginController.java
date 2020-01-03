package com.formacion.ipartek.supermercado.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.formacion.ipartek.supermercado.modelo.dao.UsuarioDAO;
import com.formacion.ipartek.supermercado.modelo.pojo.Rol;
import com.formacion.ipartek.supermercado.modelo.pojo.Usuario;

/**
 * Servlet implementation class LoginController
 */
@WebServlet("/login")
public class LoginController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private static UsuarioDAO usuarioDAO = UsuarioDAO.getInstance();

	private final static Logger LOG = Logger.getLogger(LoginController.class);

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String view = "login.jsp";
		// 1. Recogemos los parametros de la request

		String nombre = request.getParameter("nombre");
		String password = request.getParameter("password");

		try {

			Usuario usuario = usuarioDAO.exist(nombre, password);
			if (usuario != null) {

				LOG.info("Login correcto " + usuario);
				HttpSession session = request.getSession();

				session.setAttribute("usuarioLogueado", usuario);
				session.setMaxInactiveInterval(60 * 5);
				LOG.debug(usuario.getRol());
				if (usuario.getRol().getId() == Rol.ROL_ADMIN) {

					view = "seguridad/index.jsp";

				} else {
					view = "mipanel/index.jsp";
				}

			} else {
				request.setAttribute("mensajeAlerta",
						new Alerta(Alerta.TIPO_DANGER, "Credenciales incorrectas, prueba de nuevo"));
			}
		} catch (Exception e) {
			LOG.error(e);
		} finally {
			request.getRequestDispatcher(view).forward(request, response);
		}

	}

}
