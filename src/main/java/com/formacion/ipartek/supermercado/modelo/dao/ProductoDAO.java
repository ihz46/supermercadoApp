package com.formacion.ipartek.supermercado.modelo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.formacion.ipartek.supermercado.modelo.bd.ConnectionManager;
import com.formacion.ipartek.supermercado.modelo.pojo.Producto;

public class ProductoDAO implements IDAO<Producto> {

	private static ProductoDAO INSTANCE;

	private static final String SQL_GET_ALL = "SELECT id , nombre, precio, imagen, descripcion, descuento FROM producto ORDER BY id DESC LIMIT 500;";
	private static final String SQL_GET_BY_ID = "SELECT id, nombre, precio, imagen, descripcion, descuento FROM producto id = ?";
	private static final String SQL_GET_ALL_BY_NOMBRE = "SELECT u.id, u.nombre, r.id as 'id_rol', r.nombre as 'nombre_rol',contrasenya, fecha_creacion, fecha_eliminacion FROM usuario as u, rol as r WHERE u.id_rol = r.id AND u.nombre LIKE ? ORDER BY u.nombre ASC LIMIT 500;";
	private static final String SQL_INSERT = "INSERT INTO producto ( nombre, precio, imagen, descripcion, descuento) VALUES ( ? , ?, ?, ? , ?);";
	private static final String SQL_UPDATE = "UPDATE usuario SET nombre= ?, contrasenya= ? WHERE id = ?;";
	// private static final String SQL_DELETE = "DELETE FROM usuario WHERE id = ?;";
	private static final String SQL_DELETE_LOGICO = "UPDATE usuario SET fecha_eliminacion = CURRENT_TIMESTAMP() WHERE id = ?;";

	private ProductoDAO() throws Exception {
		super();

	}

	public static synchronized ProductoDAO getInstance() throws Exception {
		if (INSTANCE == null) {
			INSTANCE = new ProductoDAO();
		}

		return INSTANCE;
	}

	@Override
	public List<Producto> getAll() {
		List<Producto> registros = new ArrayList<Producto>();

		try (Connection con = ConnectionManager.getConnection();
				PreparedStatement pst = con.prepareStatement(SQL_GET_ALL);
				ResultSet rs = pst.executeQuery()) {

			while (rs.next()) {
				Producto p = new Producto();
							
				p.setId(rs.getInt("id"));
				p.setNombre(rs.getString("nombre"));
				p.setPrecio(rs.getFloat("precio"));
				p.setImagen( rs.getString("imagen"));
				p.setDescripcion(rs.getString("descripcion"));
				p.setDescuento(rs.getInt("descuento"));	
				registros.add(p);

			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return registros;
	}

	@Override
	public Producto getById(int id) throws Exception {
		Producto producto = null;

		try (Connection con = ConnectionManager.getConnection();
				PreparedStatement pst = con.prepareStatement(SQL_GET_BY_ID)) {

			pst.setInt(1, id);

			try (ResultSet rs = pst.executeQuery()) {
				if (rs.next()) {
					producto = new Producto();
					producto.setId(rs.getInt("id"));
					producto.setNombre(rs.getString("nombre"));
					producto.setPrecio(rs.getFloat("precio"));
					producto.setImagen( rs.getString("imagen"));
					producto.setDescripcion(rs.getString("descripcion"));
					producto.setDescuento(rs.getInt("descuento"));	

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return producto;
	}

	@Override
	public Producto delete(int id) throws Exception {
		Producto producto = null;
		/*
		 * for (Producto p : registros) { if (id == p.getId()) { producto = p;
		 * registros.remove(p); break; }
		 * 
		 * }
		 */
		if (producto == null) {
			throw new Exception("Perro no encontrado por su id");
		}
		return producto;
	}

	@Override
	public Producto update(int id, Producto pojo) throws Exception {
		Producto producto = null;
		

		if (producto == null) {
			throw new Exception("No se ha encontrado el producto id " + pojo.getId());
		}
		return producto;
	}

	@Override
	public Producto create(Producto pojo) throws Exception {
		
		try (Connection con = ConnectionManager.getConnection();	
			PreparedStatement pst = con.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS)){
			
			pst.setString(1, pojo.getNombre());
			pst.setFloat(2, pojo.getPrecio());
			pst.setString(3, pojo.getImagen());
			pst.setString(4, pojo.getDescripcion());
			pst.setString(5, pojo.getDescripcion());
			
			int filasAfectadas = pst.executeUpdate();
			if (filasAfectadas==1) {
				//Obtenemos el id del elemento creado
				ResultSet rs = pst.getGeneratedKeys();
				if (rs.next()) {
					pojo.setId(rs.getInt(1));
				}
			}
			
			}
		
			return pojo;
			
		} 

}
