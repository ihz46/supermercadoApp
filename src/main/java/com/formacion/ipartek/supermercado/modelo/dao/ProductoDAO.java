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

public class ProductoDAO implements IProductoDAO {

	private static ProductoDAO INSTANCE;
	private final static Logger LOG = Logger.getLogger(ProductoDAO.class);

	private static final String SQL_GET_ALL = "SELECT u.id AS id_usuario, u.user_name, u.password, p.id AS id_producto, p.nombre, p.precio, p.imagen, p.descripcion, p.descuento FROM producto p  INNER JOIN usuario u WHERE p.id_usuario = u.id ORDER BY p.nombre ASC LIMIT 500";
	private static final String SQL_GET_BY_ID = "SELECT p.id 'id_producto', p.nombre 'nombre_producto', p.precio, p.imagen, p.descripcion, p.descuento, u.id 'id_usuario', u.user_name 'nombre_usuario', r.nombre 'nombre_rol', u.id_rol FROM producto p INNER JOIN usuario u ON p.id_usuario = u.id AND p.id=? INNER JOIN rol r ON u.id_rol = r.id ORDER BY p.id DESC LIMIT 500;";
	private static final String SQL_UPDATE = "UPDATE producto SET nombre = ?, precio = ?, imagen = ?, descripcion = ?, descuento = ?, id_usuario = ? WHERE id = ?";
	private static final String SQL_INSERT = "INSERT INTO producto ( nombre, precio, imagen, descripcion, descuento, id_usuario) VALUES ( ? , ?, ?, ? , ?, ?);";
	private static final String SQL_DELETE_LOGICO = "DELETE  FROM producto WHERE id=?;";
	private static final String SQL_GET_ALL_BY_USER = "SELECT u.id AS id_usuario, u.user_name, u.password, u.id_rol, rol.nombre AS nombre_rol, p.id AS id_producto, p.nombre AS nombre_producto , p.precio, p.imagen, p.descripcion, p.descuento FROM producto p  INNER JOIN usuario u ON p.id_usuario = u.id AND u.id = ? INNER JOIN rol ON u.id_rol = rol.id ORDER BY p.nombre ASC LIMIT 500";

	// Consultas personalizadas para usuario
	private static final String SQL_UPDATE_BY_USER = "UPDATE producto SET nombre = ?, precio = ?, imagen = ?, descripcion = ?, descuento = ?, id_usuario = ? WHERE id = ? AND id_usuario = ?";
	private static final String SQL_DELETE_BY_USER = "DELETE FROM producto WHERE id =? AND id_usuario=?";
	private static final String SQL_GET_PRODUCTO_BY_ID_USER = "SELECT producto.id AS id_producto, nombre, precio, imagen, descripcion, descuento, id_usuario, usuario.user_name AS nombre_usuario, id_rol  FROM producto INNER JOIN usuario ON producto.id_usuario = usuario.id WHERE producto.id = ? AND id_usuario = ? ;";

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
		
		try (Connection con = ConnectionManager.getConnection();
				PreparedStatement pst = con.prepareStatement(SQL_GET_BY_ID)) {

			pst.setInt(1, id);
			LOG.debug(pst);

			try (ResultSet rs = pst.executeQuery()) {
				if (rs.next()) {
					producto = mapper(rs);

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return producto;
	}
	
	

	/**
	 * Recupera un producto de un Usuario concreto
	 * @param idProducto
	 * @param idUsuario
	 * @return Producto si encuentra y null si no encuentra.
	 * @throws ProductoException, cuando intenta recuperar un producto que no pertenece al Usuario.
	 */
	@Override
	public Producto getByIdByUser(int idProducto, int idUsuario) throws ProductoException {
		Producto producto = null;
		
		try (Connection con = ConnectionManager.getConnection();
				PreparedStatement pst = con.prepareStatement(SQL_GET_PRODUCTO_BY_ID_USER)) {

			pst.setInt(1, idProducto);
			pst.setInt(2, idUsuario);
			LOG.debug(pst);

			try (ResultSet rs = pst.executeQuery()) {
				if (rs.next()) {
					producto = new Producto();
					producto = mapper(rs);

				}else {
					throw new ProductoException(ProductoException.EXCEPTION_UNAUTORIZED);
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
				LOG.debug("Producto eliminado");
			} else {
				throw new Exception("No se han encontrado registros para el id =" + id);
			}

		}
		return producto;
	}

	@Override
	public Producto deleteByUser(int idProducto, int idUsuario) throws ProductoException {
		Producto producto = null;

		try (Connection con = ConnectionManager.getConnection();
				PreparedStatement pst = con.prepareStatement(SQL_DELETE_BY_USER)) {

			pst.setInt(1, idProducto);
			pst.setInt(2, idUsuario);

			producto = this.getByIdByUser(idProducto, idUsuario);

			int filasAfectadas = pst.executeUpdate();
			if (filasAfectadas == 1) {
				LOG.debug("Producto eliminado con éxito");
				;
			} else {
				throw new ProductoException(ProductoException.EXCEPTION_UNAUTORIZED);
			}
		} catch (SQLException e) {
			LOG.error(e);
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
	public Producto updateByUser(int idProducto, int idUsuario, Producto pojo) throws ProductoException {
		Producto producto = null;
		try (Connection con = ConnectionManager.getConnection();
				PreparedStatement pst = con.prepareStatement(SQL_UPDATE_BY_USER)) {

			pst.setString(1, pojo.getNombre());
			pst.setFloat(2, pojo.getPrecio());
			pst.setString(3, pojo.getImagen());
			pst.setString(4, pojo.getDescripcion());
			pst.setInt(5, pojo.getDescuento());
			pst.setInt(6, idUsuario);
			pst.setInt(7, idProducto);
			pst.setInt(8, idUsuario);
			
			LOG.debug(pst);
			
			producto = this.getByIdByUser(idProducto, idUsuario);

			int filasAfectadas = pst.executeUpdate();
			if (filasAfectadas == 1) {
			  producto = this.getByIdByUser(idProducto, idUsuario);
			} else {
				throw new ProductoException(ProductoException.EXCEPTION_UNAUTORIZED);
			}

		} catch (SQLException e) {
			LOG.error(e);
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
					try {
						Producto p = new Producto();
						p.setId(rs.getInt("id_producto"));
						p.setNombre(rs.getString("nombre_producto"));
						p.setPrecio(rs.getFloat("precio"));
						p.setImagen(rs.getString("imagen"));
						p.setDescripcion(rs.getString("descripcion"));
						p.setDescuento(rs.getInt("descuento"));
						Usuario u = new Usuario();
						u.setId(rs.getInt("id_usuario"));
						u.setNombre(rs.getString("user_name"));
						u.setPassword(rs.getString("password"));
						p.setUsuario(u);
						Rol r = new Rol();
						r.setId(rs.getInt("id_rol"));
						r.setNombre(rs.getString("nombre_rol"));
						u.setRol(r);
						listaProductos.add(p);
						
					} catch (Exception e) {
						LOG.error(e);
					}
				}

			} // executeQuery();
		} catch (SQLException e) {
			LOG.error(e);
		}

		return listaProductos;

	}

	private Producto mapper(ResultSet rs) throws Exception {
		
		UsuarioDAO daoUsuario = UsuarioDAO.getInstance();
		
		Producto p = new Producto();
		p.setId(rs.getInt("id_producto"));
		p.setNombre(rs.getString("nombre"));
		p.setPrecio(rs.getFloat("precio"));
		p.setImagen(rs.getString("imagen"));
		p.setDescripcion(rs.getString("descripcion"));
		p.setDescuento(rs.getInt("descuento"));
		p.setUsuario(daoUsuario.getById(rs.getInt("id_usuario")));

		

		return p;
	}

}
