package com.formacion.ipartek.supermercado.modelo.dao;

import com.formacion.ipartek.supermercado.modelo.pojo.Usuario;

public interface IUsuarioDAO extends IDAO<Usuario>{
	
	
	/**
	 * Comprueba si existe el usuario en la base de datos
	 * @param user_name
	 * @param password
	 * @return usuario con datos si lo encuentra y sino devuelve null
	 */
	Usuario exist (String user_name, String password);

}
