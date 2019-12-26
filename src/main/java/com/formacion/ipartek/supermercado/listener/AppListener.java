package com.formacion.ipartek.supermercado.listener;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.apache.log4j.Logger;

import com.formacion.ipartek.supermercado.modelo.bd.ConnectionManager;

/**
 * Application Lifecycle Listener implementation class AppListener
 *
 */
@WebListener
public class AppListener implements ServletContextListener {

	private final static Logger LOG = Logger.getLogger(AppListener.class);

	public void contextInitialized(ServletContextEvent sce) {
		LOG.debug("Se ha iniciado la aplicacion");

		ServletContext sc = sce.getServletContext();
		if (ConnectionManager.getConnection() == null) {
			sc.getRequestDispatcher("/error.jsp");
		}else {
			sc.getRequestDispatcher("/inicio");
		}

	}

	public void contextDestroyed(ServletContextEvent sce) {
		// TODO Auto-generated method stub
	}

}
