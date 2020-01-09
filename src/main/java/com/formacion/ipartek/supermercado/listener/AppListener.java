package com.formacion.ipartek.supermercado.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.apache.log4j.Logger;

/**
 * Application Lifecycle Listener implementation class AppListener
 *
 */
@WebListener
public class AppListener implements ServletContextListener {

	private final static Logger LOG = Logger.getLogger(AppListener.class);

	public void contextInitialized(ServletContextEvent sce) {
		LOG.debug("Se ha iniciado la aplicacion ");
	
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		LOG.debug("Ha finalizado la ejecución de la aplicación");
		
	}

	

}
