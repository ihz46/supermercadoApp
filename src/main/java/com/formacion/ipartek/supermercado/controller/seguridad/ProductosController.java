package com.formacion.ipartek.supermercado.controller.seguridad;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.formacion.ipartek.supermercado.modelo.dao.ProductoDAO;

/**
 * Servlet implementation class ProductosController
 */
@WebServlet("/seguridad/productos")
public class ProductosController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String VIEW_TABLA ="/productos/index.jsp";
	private static final String VIEW_FORM = "/productos/formulario.jsp";
	private static final String FORWARD = "/productos/index.jsp";
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
		
		try {
			
			request.setAttribute("productos", dao.getAll());
			
			switch (pAccion) {
			case ACCION_LISTAR:
				listar(request, response);
				break;
				
			case ACCION_GUARDAR:
				guardar(request, response);
				break;
				
			case ACCION_IR_FORMULARIO:
				irFormulario(request, response);
				break;
				
			case ACCION_ELIMINAR:
				eliminar(request, response);
				break;
				
			default:
				listar(request, response);
				break;
			}
			
			
			
		} catch (Exception e) {
			// TODO: log
		}finally {
			request.getRequestDispatcher(FORWARD).forward(request, response);
		}
		
	}


	private void eliminar(HttpServletRequest request, HttpServletResponse response) {
			
	}


	private void irFormulario(HttpServletRequest request, HttpServletResponse response) {
				
	}


	private void guardar(HttpServletRequest request, HttpServletResponse response) {

	}


	private void listar(HttpServletRequest request, HttpServletResponse response) {
		
		
	}

}
