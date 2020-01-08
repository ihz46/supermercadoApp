package com.formacion.ipartek.supermercado.modelo.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.formacion.ipartek.supermercado.modelo.bd.ConnectionManager;
import com.formacion.ipartek.supermercado.modelo.pojo.Categoria;

public class CategoriaDAO implements ICategoriaDAO {

	private static CategoriaDAO INSTANCE;
	private final static Logger LOG = Logger.getLogger(CategoriaDAO.class);

	private CategoriaDAO() {
		super();
	}

	public static synchronized CategoriaDAO getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new CategoriaDAO();
		}
		return INSTANCE;
	}

	@Override
	public List<Categoria> getAll() {
		LOG.trace("Recuperamos todas las categorias");
		List<Categoria> listaCategorias = new ArrayList<Categoria>();

		try (Connection con = ConnectionManager.getConnection();
				CallableStatement cs = con.prepareCall("{ CALL pa_categoria_getall() }");) {

			LOG.debug(cs);

			try (ResultSet rs = cs.executeQuery()) {
				
				while (rs.next()) {
					Categoria c = mapper(rs);
					listaCategorias.add(c);
				}
			}

		} catch (Exception e) {
			LOG.error(e);
		}

		return listaCategorias;
	}

	@Override
	public Categoria getById(int id) throws Exception {
		LOG.trace("Obtenemos la categoria con el id " + id);
		Categoria categoria = null;

		try (Connection con = ConnectionManager.getConnection();
				CallableStatement cs = con.prepareCall("{CALL pa_categoria_get_by_id(?)}");) {
			
			// Primer parámetro de entrad = id;
			cs.setInt(1, id);
			
			try (ResultSet rs = cs.executeQuery()) {
				if (rs.next()) {
					
					categoria = mapper(rs);

				}else {
					throw new Exception("No se ha encontrado ninguna categoría con el id " + id);
				}
			}

		}

		return categoria;
	}



	@Override
	public Categoria delete(int id) throws Exception {
		LOG.trace("Eliminamos la categoria" + id);
		Categoria categoria = null;

		try (Connection con = ConnectionManager.getConnection();
				CallableStatement cs = con.prepareCall("{CALL pa_categoria_delete(?)}");) {

			// Primer parámetro de entrada = id;

			cs.setInt(1, id);

			categoria = this.getById(id);

			int filasAfectadas = cs.executeUpdate();

			if (filasAfectadas == 1) {
				LOG.debug("Producto eliminado con éxito");
			} else {
				throw new Exception("No se ha podido eliminar la categoría");
			}
		}

		return categoria;
	}

	@Override
	public Categoria update(int id, Categoria pojo) throws Exception {
		LOG.trace("Actualizamos la categoria" );
		
		Categoria categoria = null;
		
		try(Connection con = ConnectionManager.getConnection();
				CallableStatement cs = con.prepareCall("{ CALL pa_categoria_update(?,?) }");){
			
			//Primer parámetro de entrada -> nombre
			
			cs.setString(1, pojo.getNombre());
			
			//Segundo parámetro de entrada -> id
			
			cs.setInt(2, id);
			
			categoria = this.getById(id);
			
			int filasAfectadas = cs.executeUpdate();
			if (filasAfectadas == 1) {
				pojo.setId(id);
			} else {
				throw new Exception("No se han encontrado registros para el id =" + id);
			}
			
		}
		return categoria;
	}

	@Override
	public Categoria create(Categoria pojo) throws Exception {
		LOG.trace("Crear una categoria" + pojo);
		Categoria categoria = pojo;

		try (Connection con = ConnectionManager.getConnection();
				CallableStatement cs = con.prepareCall(" { CALL pa_categoria_insert(?,?) } ");) {

			// Primer parámetro de entrada
			cs.setString(1, pojo.getNombre());

			// Parámetro de salida
			cs.registerOutParameter(2, java.sql.Types.INTEGER);

			LOG.debug(cs);

			// Ejecutamos el procedimiento almacenado executeUpdate, no es una
			// SELECT->execute query
			cs.executeUpdate();

			// Guardamos el id obtenido en el pojo
			pojo.setId(cs.getInt(2));

		}

		return categoria;
	}
	
	private Categoria mapper(ResultSet rs) throws SQLException {
		Categoria c = new Categoria();
		c.setId(rs.getInt("id"));
		c.setNombre(rs.getString("nombre"));
		return c;
		
	}

}
