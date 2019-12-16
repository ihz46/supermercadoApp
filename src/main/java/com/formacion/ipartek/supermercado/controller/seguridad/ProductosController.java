package com.formacion.ipartek.supermercado.controller.seguridad;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.formacion.ipartek.supermercado.modelo.dao.ProductoDAO;
import com.formacion.ipartek.supermercado.modelo.pojo.Producto;

/**
 * Servlet implementation class ProductosController
 */
@WebServlet("/seguridad/productos")
public class ProductosController extends HttpServlet {
	
	//Log
	
	private final static Logger log = Logger.getLogger(ProductosController.class);
	
	//Constantes
	private static final long serialVersionUID = 1L;
	private static final String VIEW_TABLA ="/productos/index.jsp";
	private static final String VIEW_FORM = "/productos/formulario.jsp";
	private static  String vistaSeleccionada = VIEW_TABLA;
	private static  ProductoDAO dao;
	
	
	//Acciones:
	
	public static final String ACCION_LISTAR ="listar";
	public static final String ACCION_IR_FORMULARIO ="formulario";
	public static final String ACCION_GUARDAR ="guardar"; //create y update
	public static final String ACCION_ELIMINAR = "eliminar";
	
	//Variables globales:
	
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
		dao = ProductoDAO.getInstance();
	}
	
	
	@Override
	public void destroy() {
		super.destroy();
		dao = null;
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doAction(request,response);
	}

	
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doAction(request, response);
	}
	
	private void doAction(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//1. Recoger parámetros
		
		String pAccion = request.getParameter("accion");
		
		String pId = request.getParameter("id");
		String pNombre = request.getParameter("nombre");
		String pPrecio = request.getParameter("precio");
		String pImagen = request.getParameter("imagen");
		String pDescripcion = request.getParameter("descripcion");
		String pDescuento = request.getParameter("descuento");
		
		String base = request.getContextPath();
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
		}finally {
			String url = base + vistaSeleccionada;
			System.out.println(url);
			request.getRequestDispatcher( vistaSeleccionada).forward(request, response);
		}
		
	}


	private void eliminar(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			int idParseado = Integer.parseInt(pId);	
			dao.delete(idParseado);
		} catch (Exception e) {
			log.error("Error al parsear el id");
		}
	}


	private void irFormulario(HttpServletRequest request, HttpServletResponse response) {
		//Comprobamos que el id introducido sea mayor que 0.
		
		
		
		
		//dao.getbyid = >implementar
		
		request.setAttribute("producto", new Producto());
		vistaSeleccionada = VIEW_FORM;
	}


	private void guardar(HttpServletRequest request, HttpServletResponse response) {
		try {
			
			int idNumerico = Integer.parseInt(pId);
			if (idNumerico>0) {
				//Comprobamos que no este en la lista
				List<Producto> listaProductos = dao.getAll();
				
				for (int i = 0; i < listaProductos.size(); i++) {
					if (idNumerico == listaProductos.get(i).getId()) {
						Producto p = new Producto();
						p.setId(idNumerico);
						p.setNombre(pNombre);
						dao.create(p);
						
					}
				}
			}else {
				//error
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	private void listar(HttpServletRequest request, HttpServletResponse response) {
		
		
	}

}
