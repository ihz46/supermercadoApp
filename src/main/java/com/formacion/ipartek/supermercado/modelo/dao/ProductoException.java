package com.formacion.ipartek.supermercado.modelo.dao;

public class ProductoException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public static final String EXCEPTION_UNAUTORIZED = "El producto no pertenece al usuario";
	
	public ProductoException(String mensaje) {
		super(mensaje);
	}
}
