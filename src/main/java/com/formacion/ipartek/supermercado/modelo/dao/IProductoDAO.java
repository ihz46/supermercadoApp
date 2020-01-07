package com.formacion.ipartek.supermercado.modelo.dao;

import java.util.List;

import com.formacion.ipartek.supermercado.modelo.pojo.Producto;

public interface IProductoDAO extends IDAO<Producto>{
	
	
	/**
	 * Lista los productos de un usuario
	 * @param idUsuario int, identificador del usuario
	 * @return lista de Productos, si no existe devuelve una lista de productos vacía.
	 */
	List<Producto> getAllByUser(int idUsuario);
	
	/**
	 * Recupera un producto de un Usuario concreto
	 * @param idProducto
	 * @param idUsuario
	 * @return Producto si encuentra y null si no encuentra.
	 * @throws ProductoException, cuando intenta recuperar un producto que no pertenece al Usuario.
	 */
	Producto getByIdByUser(int idProducto, int idUsuario) throws ProductoException;
	
	
	/**
	 * 
	 * @param idProducto
	 * @param idUsuario
	 * @param pojo
	 * @return
	 * @throws ProductoException cuando intenta modificar un producto que no pertenece al Usuario
	 */
	public Producto updateByUser(int idProducto, int idUsuario, Producto pojo) throws ProductoException;
	
	
	/**
	 * 
	 * @param idProducto
	 * @param idUsuario
	 * @return Producto si lo encuentra, si no lo encuentra lanza ProductoException.
	 * @throws ProductoException
	 * 
	 * <ol> 
	 * 		<li>Cuando intenta eliminar un producto que no pertenece al usuario</li>
	 * 		<li>Cuando no encuentra el producto por su id para ese usuario</li>
	 * </ol>
	 * 
	 */
	public Producto deleteByUser(int idProducto, int idUsuario) throws ProductoException;
	
}
