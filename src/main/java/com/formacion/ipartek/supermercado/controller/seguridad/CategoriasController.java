package com.formacion.ipartek.supermercado.controller.seguridad;

import java.io.IOException;
import java.util.ArrayList;
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
import com.formacion.ipartek.supermercado.modelo.dao.CategoriaDAO;
import com.formacion.ipartek.supermercado.modelo.dao.ProductoDAO;
import com.formacion.ipartek.supermercado.modelo.dao.UsuarioDAO;
import com.formacion.ipartek.supermercado.modelo.pojo.Categoria;
import com.formacion.ipartek.supermercado.modelo.pojo.Producto;
import com.formacion.ipartek.supermercado.modelo.pojo.Usuario;

/**
 * Servlet implementation class CategoriasController
 */
@WebServlet("/categorias")
public class CategoriasController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	// Log

	private final static Logger LOG = Logger.getLogger(CategoriasController.class);

	// Constantes

	private static final String VIEW_TABLA = "categorias/index.jsp";
	private static final String VIEW_FORM = "categorias/formulario.jsp";
	private static String vistaSeleccionada = VIEW_TABLA;
	private static CategoriaDAO daoCategoria;

	// Crear Factoria y Validador
	ValidatorFactory factory;
	Validator validator;

	// Acciones:

	public static final String ACCION_LISTAR = "listar";
	public static final String ACCION_IR_FORMULARIO = "formulario";
	public static final String ACCION_GUARDAR = "guardar"; // create y update
	public static final String ACCION_ELIMINAR = "eliminar";

	// Variables globales

	private String pAccion;
	private int idCategoria;
	private String nombreCategoria;

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		try {
			daoCategoria = CategoriaDAO.getInstance();

			// Crear Factoria y Validador
			factory = Validation.buildDefaultValidatorFactory();
			validator = factory.getValidator();
		} catch (Exception e) {
			LOG.error(e + "Error en el init");
		}
	}
	
	@Override
	public void destroy() {
		
		super.destroy();
		daoCategoria = null;
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
		// 1. Recoger parámetros

				String pAccion = request.getParameter("accion");

				try {

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
					LOG.error(e);
				} finally {
					List<Categoria> registros = daoCategoria.getAll();
					
					request.setAttribute("registros", registros);
					

					request.getRequestDispatcher(vistaSeleccionada).forward(request, response);
				}
		
	}
	
	private void eliminar(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			int idParseado = Integer.parseInt(request.getParameter("id"));
			Categoria c = daoCategoria.getById(idParseado);
			daoCategoria.delete(idParseado);
			vistaSeleccionada = VIEW_TABLA;
			request.setAttribute("mensajeAlerta",
					new Alerta(Alerta.TIPO_SUCCESS, "Se ha eliminado la categoría " + c.getNombre() + " con éxito"));
		} catch (Exception e) {
			LOG.error(e + "Error al parsear el id");
		}
	}

	private void irFormulario(HttpServletRequest request, HttpServletResponse response) {
		String id = request.getParameter("id");
		Categoria c = null;
		List<Categoria> listaCategorias = new ArrayList<Categoria>();
		
		try {

			if ("".equals(id) || id == null) {
				// Creamos un nuevo producto para rellenar el formulario
				c = new Categoria();
				
			} else {
				// Parseamos el id
				int idParseado = Integer.parseInt(id);
				c = daoCategoria.getById(idParseado);

			}

		} catch (NumberFormatException e) {
			request.setAttribute("mensajeAlerta", new Alerta(Alerta.TIPO_DANGER, "Error al parsear el id"));
			vistaSeleccionada = VIEW_TABLA;
		} catch (Exception e) {
			LOG.error(e);
			e.printStackTrace();
			request.setAttribute("mensajeAlerta",
					new Alerta(Alerta.TIPO_DANGER, "Excepción no controlada " + e.getMessage()));
			vistaSeleccionada = VIEW_TABLA;
		} finally {
			request.setAttribute("categoria", c);
			listaCategorias =  daoCategoria.getAll();
			request.setAttribute("listaCategorias", listaCategorias);

			vistaSeleccionada = VIEW_FORM;
		}

	}

	private void guardar(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Categoria categoria = null;
		Categoria c = new Categoria();
		
		int idParseado = 0;
		Set<ConstraintViolation<Categoria>> validaciones = null;
		//TODO necesario arreglar el fallo de que no se recoge el id de usuario.

		// Recogemos los parámetros
		String idCategoria = request.getParameter("id");
		nombreCategoria = request.getParameter("nombre");
		
		try {
			// Comprobamos

			idParseado = Integer.parseInt(idCategoria);
			
			c.setNombre(nombreCategoria);
			
			validaciones = validator.validate(c);
		} catch (Exception e) {
			request.setAttribute("mensajeAlerta", new Alerta(Alerta.TIPO_WARNING, "Error de parseo"));
		}
		// Comprobamos que no haya ningún parametro null

		if (validaciones.size() > 0) {

			StringBuilder mensaje = new StringBuilder();

			for (ConstraintViolation<Categoria> cv : validaciones) {
				mensaje.append("<p>");
				mensaje.append(cv.getPropertyPath()).append(": ");
				mensaje.append(cv.getMessage());
				mensaje.append(".</p>");

			}
			vistaSeleccionada = VIEW_FORM;
			request.setAttribute("mensajeAlerta", new Alerta(Alerta.TIPO_WARNING, mensaje.toString()));
		} else {
			if ("0".equals(idCategoria)) {
				// Instanciamos un nuevo pojo (producto)
				
				categoria = daoCategoria.create(c);
				vistaSeleccionada = VIEW_FORM;
				request.setAttribute("categoria", categoria);
				request.setAttribute("mensajeAlerta", new Alerta(Alerta.TIPO_SUCCESS,
						"Se ha creado la categoría " + categoria.getNombre() + " con éxito."));

			} else {
				categoria = daoCategoria.update(idParseado, c);
				vistaSeleccionada = VIEW_FORM;
				request.setAttribute("categoria", categoria);
				request.setAttribute("mensajeAlerta", new Alerta(Alerta.TIPO_SUCCESS,
						"Se ha actualizado la categoria " + categoria.getNombre() + " con éxito."));
			}
		}

	}

	private void listar(HttpServletRequest request, HttpServletResponse response) {
		List<Categoria> listaCategorias = daoCategoria.getAll();
		request.setAttribute("registros", listaCategorias);
		vistaSeleccionada = VIEW_TABLA;

	}

}
