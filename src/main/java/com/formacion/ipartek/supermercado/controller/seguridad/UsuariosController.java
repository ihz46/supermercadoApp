package com.formacion.ipartek.supermercado.controller.seguridad;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;


import org.apache.log4j.Logger;

import com.formacion.ipartek.supermercado.controller.Alerta;
import com.formacion.ipartek.supermercado.modelo.dao.UsuarioDAO;
import com.formacion.ipartek.supermercado.modelo.pojo.Usuario;

/**
 * Servlet implementation class UsuariosController
 */
@WebServlet("/seguridad/usuarios")
public class UsuariosController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	// LOG

	private final static Logger LOG = Logger.getLogger(ProductosController.class);

	// Constantes

	private static final String VIEW_TABLA = "usuarios/index.jsp";
	private static final String VIEW_FORM = "usuarios/formulario.jsp";
	private static String vistaSeleccionada = VIEW_TABLA;
	private static UsuarioDAO dao;

	// Crear Factoria y Validador
	ValidatorFactory factory;
	Validator validator;

	// Acciones:

	public static final String ACCION_LISTAR = "listar";
	public static final String ACCION_IR_FORMULARIO = "formulario";
	public static final String ACCION_GUARDAR = "guardar"; // create y update
	public static final String ACCION_ELIMINAR = "eliminar";

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		try {
			dao = UsuarioDAO.getInstance();

			// Crear Factoria y Validador
			factory = Validation.buildDefaultValidatorFactory();
			validator = factory.getValidator();

		} catch (Exception e) {

			e.printStackTrace();

		}
	}

	@Override
	public void destroy() {
		super.destroy();
		dao = null;
		factory = null;
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doAction(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doAction(request, response);
	}

	private void doAction(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 1. Recogemos parámetros
		String pAccion = request.getParameter("accion");

		try {

			request.setAttribute("productos", dao.getAll());

			switch (pAccion) {
			case ACCION_LISTAR:
				listar(request, response);
				LOG.debug("listamos");
				break;

			case ACCION_GUARDAR:
				guardar(request, response);
				LOG.debug("guardar");
				break;

			case ACCION_IR_FORMULARIO:
				irFormulario(request, response);
				LOG.debug("formulario");
				break;

			case ACCION_ELIMINAR:
				eliminar(request, response);
				LOG.debug("Eliminamos");
				break;

			default:
				listar(request, response);
				break;
			}

		} catch (Exception e) {
			// TODO: LOG
		} finally {
			List<Usuario> registros = dao.getAll();
			request.setAttribute("registros", registros);
			request.getRequestDispatcher(vistaSeleccionada).forward(request, response);
		}

	}
	
	private void eliminar(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			int idParseado = Integer.parseInt(request.getParameter("id"));
			Usuario u = dao.getById(idParseado);
			dao.delete(idParseado);
			vistaSeleccionada = VIEW_TABLA;
			request.setAttribute("mensajeAlerta",
					new Alerta(Alerta.TIPO_SUCCESS, "Se ha eliminado el usuario " + u.getUser_name() + " con éxito"));
		} catch (Exception e) {
			LOG.error("Error al parsear el id");
		}
	}

	private void irFormulario(HttpServletRequest request, HttpServletResponse response) {
		String id = request.getParameter("id");
		Usuario u = null;
		try {

			if ("".equals(id) || id == null) {
				// Creamos un nuevo usuario para rellenar en el formulario
				u = new Usuario();
				LOG.debug(u);
				
				
			} else {
				// Parseamos el id
				int idParseado = Integer.parseInt(id);
				u = dao.getById(idParseado);
				
			}

		} catch (NumberFormatException e) {
			request.setAttribute("mensajeAlerta", new Alerta(Alerta.TIPO_DANGER, "Error al parsear el id"));
			vistaSeleccionada = VIEW_TABLA;
		} catch (Exception e) {
			request.setAttribute("mensajeAlerta",
					new Alerta(Alerta.TIPO_DANGER, "Excepción no controlada " + e.getMessage()));
			vistaSeleccionada = VIEW_TABLA;
		}finally {
			request.setAttribute("usuario", u);
			vistaSeleccionada = VIEW_FORM;
		}

	}

	private void guardar(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Usuario user = null;
		Usuario u = new Usuario();
		
		int idParseado = 0;
		Set<ConstraintViolation<Usuario>> validaciones = null;

		// Recogemos los parámetros
		String idUsuario = request.getParameter("id");
		String userName = request.getParameter("nombre");
		String password = request.getParameter("password");
		try {
			// Comprobamos

			idParseado = Integer.parseInt(idUsuario);
			
			
			u.setUser_name(userName);
			u.setPassword(password);

			
			validaciones = validator.validate(u);
		} catch (Exception e) {
			request.setAttribute("mensajeAlerta", new Alerta(Alerta.TIPO_WARNING, "Error de parseo"));
		}
		// Comprobamos que no haya ningún parametro null

		if (validaciones.size() > 0) {

			StringBuilder mensaje = new StringBuilder();

			for (ConstraintViolation<Usuario> cv : validaciones) {
				mensaje.append("<p>");
				mensaje.append(cv.getPropertyPath()).append(": ");
				mensaje.append(cv.getMessage());
				mensaje.append(".</p>");

			}
			vistaSeleccionada = VIEW_FORM;
			request.setAttribute("mensajeAlerta", new Alerta(Alerta.TIPO_WARNING, mensaje.toString()));
		} else {
			if ("0".equals(idUsuario)) {
				// Instanciamos un nuevo pojo (usuario)

				user = dao.create(u);
				vistaSeleccionada = VIEW_TABLA;
				request.setAttribute("usuario", user);
				request.setAttribute("mensajeAlerta", new Alerta(Alerta.TIPO_SUCCESS,
						"Se ha creado el usuario " + user.getUser_name() + " con éxito."));

			} else {
				user = dao.update(idParseado, u);
				vistaSeleccionada = VIEW_TABLA;
				request.setAttribute("usuario", user);
				request.setAttribute("mensajeAlerta", new Alerta(Alerta.TIPO_SUCCESS,
						"Se ha actualizado el usuario " + user.getUser_name() + " con éxito."));
			}
		}

	}

	private void listar(HttpServletRequest request, HttpServletResponse response) {

		List<Usuario> listaUsuarios = dao.getAll();
		request.setAttribute("registros", listaUsuarios);
		vistaSeleccionada = VIEW_TABLA;

	}

}
