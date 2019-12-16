package com.formacion.ipartek.supermercado.modelo.dao;
import java.util.List;


public interface IDAO<P> {
	
		
	/**
	 * Obtiene todos los datos
	 * @return lista de pojos
	 */
	List<P> getAll();
	
	/**
	 * 
	 * @param id
	 * @return Pojo 
	 * @throws Exception si no encuentra el perro por id
	 */
	P getById(int id) throws Exception;
	
	/**
	 * Elimina
	 * @param id identificador
	 * @return Pojo eliminado
	 * @throws Exception si no se puede eliminar o no encontrado
	 */
	P delete(int id) throws Exception;

	/**
	 * Modifica un Pojo
	 * @param id identificador
	 * @param pojo contiene los datos a modificar
	 * @return pojo modificado
	 * @throws Exception si no puede modificar o no lo encuentra
	 */
	P update(int id, P pojo)  throws Exception;
	
	/**
	 * crea un nuevo pojo
	 * @param pojo a crear
	 * @return pojo creado con el id nuevo
	 * @throws Exception si no puede crear
	 */
	P create(P pojo)  throws Exception;

}
