package com.formacion.ipartek.supermercado.controller;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.formacion.ipartek.supermercado.modelo.dao.CategoriaDAO;
import com.formacion.ipartek.supermercado.modelo.dao.ProductoDAO;
import com.formacion.ipartek.supermercado.modelo.pojo.Categoria;
import com.formacion.ipartek.supermercado.modelo.pojo.Producto;

/**
 * Servlet implementation class InicioController
 */
@WebServlet("/inicio")
public class InicioController extends HttpServlet {
	
	private final static Logger LOG = Logger.getLogger(InicioController.class);
	private static final long serialVersionUID = 1L;
	private static ProductoDAO dao;
	private static CategoriaDAO daoCategoria;
   
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		try {
			dao = ProductoDAO.getInstance();
			daoCategoria = CategoriaDAO.getInstance();
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		
	}
    
	@Override
	public void destroy() {
		super.destroy();
		dao = null;
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//TODO -> llamar al DAO
		ArrayList<Producto> productos = (ArrayList<Producto>) dao.getAll();
		ArrayList<Categoria> categorias = (ArrayList<Categoria>) daoCategoria.getAll();
		/*
		try {
			
			Prueba crear categoria
			Categoria c = new Categoria();
			
			c.setNombre("prueba1");
			Categoria categoria = daoCategoria.create(c);
			
			
			categorias.add(c);
			
			Prueba eliminar
			daoCategoria.delete(20);
			
				
			//Prueba actualizar
			Categoria prueba1 = daoCategoria.update(13, c);
			LOG.debug(prueba1);
			
			Prueba getByid
			Categoria prueba = daoCategoria.getById(categoria.getId());
			LOG.debug(prueba);
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		*/
		request.setAttribute("mensajeAlerta", new Alerta(Alerta.TIPO_PRIMARY, "Estos son los últimos productos destacados para ti:" ));
		request.setAttribute("productos", productos);
		request.setAttribute("categorias", categorias);
		request.getRequestDispatcher("index.jsp").forward(request, response);
	}

}
