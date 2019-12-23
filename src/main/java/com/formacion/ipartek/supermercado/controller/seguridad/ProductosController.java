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
import com.formacion.ipartek.supermercado.modelo.pojo.Producto;

/**
 * Servlet implementation class ProductosController
 */
@WebServlet("/seguridad/productos")
public class ProductosController extends HttpServlet {

	// Log

	private final static Logger log = Logger.getLogger(ProductosController.class);

	// Constantes
	private static final long serialVersionUID = 1L;
	private static final String VIEW_TABLA = "productos/index.jsp";
	private static final String VIEW_FORM = "productos/formulario.jsp";
	private static String vistaSeleccionada = VIEW_TABLA;
	private static ProductoDAO dao;

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

	private void doAction(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 1. Recoger parámetros

		String pAccion = request.getParameter("accion");

		try {

			request.setAttribute("productos", dao.getAll());

			switch (pAccion) {
			case ACCION_LISTAR:
				listar(request, response);
				log.debug("listamos");
				break;

			case ACCION_GUARDAR:
				guardar(request, response);
				log.debug("guardar");
				break;

			case ACCION_IR_FORMULARIO:
				irFormulario(request, response);
				log.debug("formulario");
				break;

			case ACCION_ELIMINAR:
				eliminar(request, response);
				log.debug("Eliminamos");
				break;

			default:
				listar(request, response);
				break;
			}

		} catch (Exception e) {
			// TODO: log
		} finally {
			List<Producto> registros = dao.getAll();
			request.setAttribute("registros", registros);
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
			log.error("Error al parsear el id");
		}
	}

	private void irFormulario(HttpServletRequest request, HttpServletResponse response) {
		String id = request.getParameter("id");
		Producto p = null;
		try {

			if ("".equals(id) || id == null) {
				// Creamos un nuevo producto para rellenar el formulario
				p = new Producto();
				request.setAttribute("producto", p);
				vistaSeleccionada = VIEW_FORM;
			} else {
				// Parseamos el id
				int idParseado = Integer.parseInt(id);
				p = dao.getById(idParseado);
				request.setAttribute("producto", p);
				vistaSeleccionada = VIEW_FORM;

			}

		} catch (NumberFormatException e) {
			request.setAttribute("mensajeAlerta", new Alerta(Alerta.TIPO_DANGER, "Error al parsear el id"));
			vistaSeleccionada = VIEW_TABLA;
		} catch (Exception e) {
			request.setAttribute("mensajeAlerta",
					new Alerta(Alerta.TIPO_DANGER, "Excepción no controlada " + e.getMessage()));
			vistaSeleccionada = VIEW_TABLA;
		}

	}

	private void guardar(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Producto producto = null;
		Producto p = new Producto();
		int idParseado = 0;
		Set<ConstraintViolation<Producto>> validaciones = null;

		// Recogemos los parámetros
		String pId = request.getParameter("id");
		String pNombre = request.getParameter("nombre");
		String pPrecio = request.getParameter("precio");
		String pImagen = request.getParameter("imagen");
		String pDescripcion = request.getParameter("descripcion");
		String pDescuento = request.getParameter("descuento");
		try {
			// Comprobamos

			idParseado = Integer.parseInt(pId);
			float precioParseado = Float.parseFloat(pPrecio);
			int descuentoParseado = Integer.parseInt(pDescuento);

			p.setNombre(pNombre);
			p.setPrecio(precioParseado);
			p.setImagen(pImagen);
			p.setDescripcion(pDescripcion);
			p.setDescuento(descuentoParseado);

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
