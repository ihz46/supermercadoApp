package com.formacion.ipartek.supermercado.modelo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.formacion.ipartek.supermercado.controller.mipanel.ProductosController;
import com.formacion.ipartek.supermercado.modelo.bd.ConnectionManager;
import com.formacion.ipartek.supermercado.modelo.pojo.Producto;
import com.formacion.ipartek.supermercado.modelo.pojo.Rol;
import com.formacion.ipartek.supermercado.modelo.pojo.Usuario;

public class ProductoDAO implements IDAO<Producto> {

	private static ProductoDAO INSTANCE;
	private final static Logger LOG = Logger.getLogger(ProductoDAO.class);

	private static final String SQL_GET_ALL = "SELECT u.id AS id_usuario, u.user_name, u.password, p.id AS id_producto, p.nombre, p.precio, p.imagen, p.descripcion, p.descuento FROM producto p  INNER JOIN usuario u WHERE p.id_usuario = u.id ORDER BY p.nombre ASC LIMIT 500";
	private static final String SQL_GET_BY_ID = "SELECT p.id 'id_producto', p.nombre 'nombre_producto', p.precio, p.imagen, p.descripcion, p.descuento, u.id 'id_usuario', u.user_name 'nombre_usuario', r.nombre 'nombre_rol', u.id_rol FROM producto p INNER JOIN usuario u ON p.id_usuario = u.id AND p.id=? INNER JOIN rol r ON u.id_rol = r.id ORDER BY p.id DESC LIMIT 500;";
	private static final String SQL_UPDATE = "UPDATE producto SET nombre = ?, precio = ?, imagen = ?, descripcion = ?, descuento = ?, id_usuario = ? WHERE id = ?";
	private static final String SQL_INSERT = "INSERT INTO producto ( nombre, precio, imagen, descripcion, descuento, id_usuario) VALUES ( ? , ?, ?, ? , ?, ?);";
	private static final String SQL_DELETE_LOGICO = "DELETE  FROM producto WHERE id=?;";
	private static final String SQL_GET_ALL_BY_USER = "SELECT u.id AS id_usuario, u.user_name, u.password, p.id AS id_producto, p.nombre, p.precio, p.imagen, p.descripcion, p.descuento FROM producto p  INNER JOIN usuario u WHERE p.id_usuario = u.id AND u.id = ? ORDER BY p.nombre ASC LIMIT 500";

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

				p.setId(rs.getInt("id_producto"));
				p.setNombre(rs.getString("nombre"));
				p.setPrecio(rs.getFloat("precio"));
				p.setImagen(rs.getString("imagen"));
				p.setDescripcion(rs.getString("descripcion"));
				p.setDescuento(rs.getInt("descuento"));

				Usuario u = new Usuario();
				u.setId(rs.getInt("id_usuario"));
				u.setNombre(rs.getString("user_name"));
				u.setPassword(rs.getString("password"));
				p.setUsuario(u);
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
		Usuario u = null;
		try (Connection con = ConnectionManager.getConnection();
				PreparedStatement pst = con.prepareStatement(SQL_GET_BY_ID)) {

			pst.setInt(1, id);
			LOG.debug(pst);

			try (ResultSet rs = pst.executeQuery()) {
				if (rs.next()) {
					producto = new Producto();
					producto.setId(rs.getInt("id_producto"));
					producto.setNombre(rs.getString("nombre_producto"));
					producto.setPrecio(rs.getFloat("precio"));
					producto.setImagen(rs.getString("imagen"));
					producto.setDescripcion(rs.getString("descripcion"));
					producto.setDescuento(rs.getInt("descuento"));
					u = new Usuario();
					u.setId(rs.getInt("id_usuario"));
					u.setNombre(rs.getString("nombre_usuario"));
					u.setRol(new Rol(rs.getInt("id_rol"), rs.getString("nombre_rol")));
					producto.setUsuario(u);
					

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

		try (Connection con = ConnectionManager.getConnection();
				PreparedStatement pst = con.prepareStatement(SQL_DELETE_LOGICO)) {

			pst.setInt(1, id);

			producto = this.getById(id);

			int filasAfectadas = pst.executeUpdate();
			if (filasAfectadas == 1) {
				producto = null;
			} else {
				throw new Exception("No se han encontrado registros para el id =" + id);
			}

		}
		return producto;
	}

	@Override
	public Producto update(int id, Producto pojo) throws Exception {
		Producto producto = null;
		try (Connection con = ConnectionManager.getConnection();
				PreparedStatement pst = con.prepareStatement(SQL_UPDATE)) {

			pst.setString(1, pojo.getNombre());
			pst.setFloat(2, pojo.getPrecio());
			pst.setString(3, pojo.getImagen());
			pst.setString(4, pojo.getDescripcion());
			pst.setInt(5, pojo.getDescuento());

			pst.setInt(6, pojo.getUsuario().getId());
			
			pst.setInt(7, id);

			producto = this.getById(id);

			int filasAfectadas = pst.executeUpdate();
			if (filasAfectadas == 1) {
				pojo.setId(id);
			} else {
				throw new Exception("No se han encontrado registros para el id =" + id);
			}

		}

		return producto;
	}

	@Override
	public Producto create(Producto pojo) throws Exception {

		try (Connection con = ConnectionManager.getConnection();
				PreparedStatement pst = con.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS)) {

			pst.setString(1, pojo.getNombre());
			pst.setFloat(2, pojo.getPrecio());
			pst.setString(3, pojo.getImagen());
			pst.setString(4, pojo.getDescripcion());
			pst.setInt(5, pojo.getDescuento());
			pst.setInt(6, pojo.getUsuario().getId());

			int filasAfectadas = pst.executeUpdate();
			if (filasAfectadas == 1) {
				// Obtenemos el id del elemento creado
				ResultSet rs = pst.getGeneratedKeys();
				if (rs.next()) {
					pojo.setId(rs.getInt(1));
				}
			}

		}

		return pojo;

	}

	public List<Producto> getAllByUser(int idUsuario) {
		ArrayList<Producto> listaProductos = new ArrayList<Producto>();

		try (Connection con = ConnectionManager.getConnection();
				PreparedStatement pst = con.prepareStatement(SQL_GET_ALL_BY_USER);) {
			pst.setInt(1, idUsuario);
			LOG.debug(pst);
			
			try (ResultSet rs = pst.executeQuery()) {
				
				while (rs.next()) {
					listaProductos.add(mapper(rs));
				}

			} // executeQuery();
		} catch (SQLException e) {
			LOG.error(e);
		}
	
	return listaProductos;

	}

	private Producto mapper(ResultSet rs) throws SQLException {
		Producto p = new Producto();
		p.setId(rs.getInt("id_producto"));
		p.setNombre(rs.getString("nombre"));
		p.setPrecio(rs.getFloat("precio"));
		p.setImagen(rs.getString("imagen"));
		p.setDescripcion(rs.getString("descripcion"));
		p.setDescuento(rs.getInt("descuento"));

		Usuario u = new Usuario();
		u.setId(rs.getInt("id_usuario"));
		u.setNombre(rs.getString("user_name"));
		p.setUsuario(u);

		return p;
	}

}
