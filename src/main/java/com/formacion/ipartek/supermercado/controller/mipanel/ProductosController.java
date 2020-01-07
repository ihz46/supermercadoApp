package com.formacion.ipartek.supermercado.controller.mipanel;

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
import com.formacion.ipartek.supermercado.modelo.dao.ProductoDAO;
import com.formacion.ipartek.supermercado.modelo.dao.ProductoException;
import com.formacion.ipartek.supermercado.modelo.dao.UsuarioDAO;
import com.formacion.ipartek.supermercado.modelo.pojo.Producto;
import com.formacion.ipartek.supermercado.modelo.pojo.Usuario;

/**
 * Servlet implementation class ProductosController
 */
@WebServlet("/mipanel/productos")
public class ProductosController extends HttpServlet {

	// Log

	private final static Logger LOG = Logger.getLogger(ProductosController.class);

	// Constantes
	private static final long serialVersionUID = 1L;
	private static final String VIEW_TABLA = "productos/index.jsp";
	private static final String VIEW_FORM = "productos/formulario.jsp";
	private static String vistaSeleccionada = VIEW_TABLA;
	private static ProductoDAO daoProducto;
	private static UsuarioDAO daoUsuario;
	private Usuario usuarioLogueado;

	// Crear Factoria y Validador
	ValidatorFactory factory;
	Validator validator;

	// Acciones:

	public static final String ACCION_LISTAR = "listar";
	public static final String ACCION_IR_FORMULARIO = "formulario";
	public static final String ACCION_GUARDAR = "guardar"; // create y update
	public static final String ACCION_ELIMINAR = "eliminar";
	private boolean isRedirect;

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
			daoProducto = ProductoDAO.getInstance();
			daoUsuario = UsuarioDAO.getInstance();

			// Crear Factoria y Validador
			factory = Validation.buildDefaultValidatorFactory();
			validator = factory.getValidator();

		} catch (Exception e) {
			LOG.error(e);

		}
	}

	@Override
	public void destroy() {
		super.destroy();
		daoUsuario = null;
		daoProducto = null;
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
		
		isRedirect = false;
		// 1. Recoger parámetros

		String pAccion = request.getParameter("accion");
		usuarioLogueado = (Usuario) request.getSession().getAttribute("usuarioLogueado");

		try {

			request.setAttribute("productos", daoUsuario.getAll());

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

		} catch (ProductoException e) {
			LOG.warn(e);
			isRedirect = true;
			
		}
		catch (Exception e) {
			LOG.error(e);
		} finally {
			if (isRedirect) {
				//Invalidamos la sesion del usuario.
				response.sendRedirect(request.getContextPath() + "/logout" );
			}else {
				request.getRequestDispatcher(vistaSeleccionada).forward(request, response);
			}
			
		}

	}

	private void eliminar(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			int idParseado = Integer.parseInt(request.getParameter("id"));
			
			Producto p = daoProducto.getById(idParseado);
			
			daoProducto.deleteByUser(idParseado, usuarioLogueado.getId());
			
			vistaSeleccionada = VIEW_TABLA;
			request.setAttribute("registros", daoProducto.getAllByUser(usuarioLogueado.getId()));
			request.setAttribute("mensajeAlerta",
					new Alerta(Alerta.TIPO_SUCCESS, "Se ha eliminado el producto " + p.getNombre() + " con éxito"));
		} catch (Exception e) {
			LOG.error("Error al parsear el id");
			request.setAttribute("mensajeAlerta", "Error al eliminar el producto");
		}
	}

	private void irFormulario(HttpServletRequest request, HttpServletResponse response) {
		String id = request.getParameter("id");
		Producto p = null;
		try {

			if ("".equals(id) || id == null) {
				// Creamos un nuevo producto para rellenar el formulario
				p = new Producto();

			} else {
				// Parseamos el id
				int idParseado = Integer.parseInt(id);
				LOG.debug(idParseado);
				p = daoProducto.getByIdByUser(idParseado,usuarioLogueado.getId());
				LOG.debug(p);

			}

		} catch (NumberFormatException e) {
			request.setAttribute("mensajeAlerta", new Alerta(Alerta.TIPO_DANGER, "Error al parsear el id"));
			vistaSeleccionada = VIEW_TABLA;
		} catch (Exception e) {
			request.setAttribute("mensajeAlerta",
					new Alerta(Alerta.TIPO_DANGER, "Excepción no controlada " + e.getMessage()));
			vistaSeleccionada = VIEW_TABLA;
		} finally {
			request.setAttribute("producto", p);
			request.setAttribute("registros", daoProducto.getAllByUser(usuarioLogueado.getId()));
			vistaSeleccionada = VIEW_FORM;
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
			String precioComa = pPrecio.replace(",", ".");
			LOG.debug(pPrecio);
			float precioParseado = Float.parseFloat(precioComa);
			int descuentoParseado = Integer.parseInt(pDescuento);

			p.setId(idParseado);
			p.setNombre(pNombre);
			p.setPrecio(precioParseado);
			p.setImagen(pImagen);
			p.setDescripcion(pDescripcion);
			p.setDescuento(descuentoParseado);

			Usuario u = new Usuario();
			u.setId(usuarioLogueado.getId());
			u.setNombre(usuarioLogueado.getNombre());
			u.setRol(usuarioLogueado.getRol());
			p.setUsuario(u);

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

				producto = daoProducto.create(p);
				vistaSeleccionada = VIEW_FORM;
				request.setAttribute("producto", producto);
				request.setAttribute("listaUsuarios", daoUsuario.getAll());
				request.setAttribute("mensajeAlerta", new Alerta(Alerta.TIPO_SUCCESS,
						"Se ha creado el producto " + producto.getNombre() + " con éxito."));

			} else {
				//TODO arreglar el updateByUser y el DeleteByUser
				producto = daoProducto.updateByUser(p.getId(), usuarioLogueado.getId(), p);
				vistaSeleccionada = VIEW_FORM;
				request.setAttribute("producto", daoProducto.getById(idParseado));
				request.setAttribute("mensajeAlerta", new Alerta(Alerta.TIPO_SUCCESS,
						"Se ha actualizado el producto " + producto.getNombre() + " con éxito."));
			}
		}

	}

	private void listar(HttpServletRequest request, HttpServletResponse response) {

		List<Producto> listaProductos = daoProducto.getAllByUser(usuarioLogueado.getId());

		request.setAttribute("registros", listaProductos);
		vistaSeleccionada = VIEW_TABLA;

	}

}
