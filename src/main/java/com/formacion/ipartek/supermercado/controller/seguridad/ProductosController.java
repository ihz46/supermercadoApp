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
import com.formacion.ipartek.supermercado.modelo.dao.ProductoDAO;
import com.formacion.ipartek.supermercado.modelo.dao.UsuarioDAO;
import com.formacion.ipartek.supermercado.modelo.pojo.Producto;
import com.formacion.ipartek.supermercado.modelo.pojo.Usuario;

/**
 * Servlet implementation class ProductosController
 */
@WebServlet("/seguridad/productos")
public class ProductosController extends HttpServlet {

	// Log

	private final static Logger LOG = Logger.getLogger(ProductosController.class);

	// Constantes
	private static final long serialVersionUID = 1L;
	private static final String VIEW_TABLA = "productos/index.jsp";
	private static final String VIEW_FORM = "productos/formulario.jsp";
	private static String vistaSeleccionada = VIEW_TABLA;
	private static ProductoDAO dao;
	private static UsuarioDAO daoUsuario;

	// Crear Factoria y Validador
	ValidatorFactory factory;
	Validator validator;

	// Acciones:

	public static final String ACCION_LISTAR = "listar";
	public static final String ACCION_IR_FORMULARIO = "formulario";
	public static final String ACCION_GUARDAR = "guardar"; // create y update
	public static final String ACCION_ELIMINAR = "eliminar";

	// Variables globales:

	String pAccion;
	String pId;
	String pNombre;
	String pPrecio;
	String pImagen;
	String pDescripcion;
	String pDescuento;

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		try {
			dao = ProductoDAO.getInstance();
			daoUsuario = UsuarioDAO.getInstance();

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
		daoUsuario = null;
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

	private void doAction(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 1. Recoger parámetros

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
			LOG.error(e);
		} finally {
			List<Producto> registros = dao.getAll();
			request.setAttribute("registros", registros);
			// Obtenemos la lista de todos los usuarios, para luego enviarla a la request
			// como atributo

			request.getRequestDispatcher(vistaSeleccionada).forward(request, response);
		}

	}

	private void eliminar(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			int idParseado = Integer.parseInt(request.getParameter("id"));
			Producto p = dao.getById(idParseado);
			dao.delete(idParseado);
			vistaSeleccionada = VIEW_TABLA;
			request.setAttribute("mensajeAlerta",
					new Alerta(Alerta.TIPO_SUCCESS, "Se ha eliminado el producto " + p.getNombre() + " con éxito"));
		} catch (Exception e) {
			LOG.error("Error al parsear el id");
		}
	}

	private void irFormulario(HttpServletRequest request, HttpServletResponse response) {
		String id = request.getParameter("id");
		Producto p = null;
		List<Usuario> listaUsuarios = new ArrayList<Usuario>();
		try {

			if ("".equals(id) || id == null) {
				// Creamos un nuevo producto para rellenar el formulario
				p = new Producto();

			} else {
				// Parseamos el id
				int idParseado = Integer.parseInt(id);
				p = dao.getById(idParseado);

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
			request.setAttribute("producto", p);
			listaUsuarios =  daoUsuario.getAll();
			request.setAttribute("listaUsuarios", listaUsuarios);

			vistaSeleccionada = VIEW_FORM;
		}

	}

	private void guardar(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Producto producto = null;
		Producto p = new Producto();
		int idParseado = 0;
		Set<ConstraintViolation<Producto>> validaciones = null;
		//TODO necesario arreglar el fallo de que no se recoge el id de usuario.

		// Recogemos los parámetros
		String pId = request.getParameter("id");
		String pNombre = request.getParameter("nombre");
		String pPrecio = request.getParameter("precio");
		String pImagen = request.getParameter("imagen");
		String pDescripcion = request.getParameter("descripcion");
		String pDescuento = request.getParameter("descuento");
		String idUsuario = request.getParameter("");
		try {
			// Comprobamos

			idParseado = Integer.parseInt(pId);
			float precioParseado = Float.parseFloat(pPrecio);
			int descuentoParseado = Integer.parseInt(pDescuento);
			Usuario usuario = new Usuario();
			
			p.setNombre(pNombre);
			p.setPrecio(precioParseado);
			p.setImagen(pImagen);
			p.setDescripcion(pDescripcion);
			p.setDescuento(descuentoParseado);
			p.setUsuario(usuario);

			validaciones = validator.validate(p);
		} catch (Exception e) {
			request.setAttribute("mensajeAlerta", new Alerta(Alerta.TIPO_WARNING, "Error de parseo"));
		}
		// Comprobamos que no haya ningún parametro null

		if (validaciones.size() > 0) {

			StringBuilder mensaje = new StringBuilder();

			for (ConstraintViolation<Producto> cv : validaciones) {
				mensaje.append("<p>");
				mensaje.append(cv.getPropertyPath()).append(": ");
				mensaje.append(cv.getMessage());
				mensaje.append(".</p>");

			}
			vistaSeleccionada = VIEW_FORM;
			request.setAttribute("mensajeAlerta", new Alerta(Alerta.TIPO_WARNING, mensaje.toString()));
		} else {
			if ("0".equals(pId)) {
				// Instanciamos un nuevo pojo (producto)

				producto = dao.create(p);
				vistaSeleccionada = VIEW_FORM;
				request.setAttribute("producto", producto);
				request.setAttribute("mensajeAlerta", new Alerta(Alerta.TIPO_SUCCESS,
						"Se ha creado el producto " + producto.getNombre() + " con éxito."));

			} else {
				producto = dao.update(idParseado, p);
				vistaSeleccionada = VIEW_FORM;
				request.setAttribute("producto", producto);
				request.setAttribute("mensajeAlerta", new Alerta(Alerta.TIPO_SUCCESS,
						"Se ha actualizado el producto " + producto.getNombre() + " con éxito."));
			}
		}

	}

	private void listar(HttpServletRequest request, HttpServletResponse response) {

		List<Producto> listaProductos = dao.getAll();
		request.setAttribute("registros", listaProductos);
		vistaSeleccionada = VIEW_TABLA;

	}

}
