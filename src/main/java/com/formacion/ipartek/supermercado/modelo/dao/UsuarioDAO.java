package com.formacion.ipartek.supermercado.modelo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.formacion.ipartek.supermercado.modelo.bd.ConnectionManager;
import com.formacion.ipartek.supermercado.modelo.pojo.Rol;
import com.formacion.ipartek.supermercado.modelo.pojo.Usuario;

public class UsuarioDAO implements IUsuarioDAO {

	private final static Logger LOG = Logger.getLogger(UsuarioDAO.class);

	private static final String SQL_GET_ALL = "SELECT u.id, user_name, PASSWORD, id_rol , r.nombre AS nombre_rol FROM usuario u, rol r WHERE u.id_rol = r.id ;";
	private static final String SQL_GET_BY_ID = "SELECT id, user_name, password , id_rol FROM usuario WHERE id= ?;";
	private static final String SQL_INSERT = "INSERT INTO usuario ( user_name, password, id_rol ) VALUES (?,?,?);";
	private static final String SQL_DELETE = "DELETE FROM usuario WHERE id=?;";
	private static final String SQL_UPDATE = "UPDATE usuario SET user_name=?, password = ?, id_rol = ? WHERE id = ?;";
	private static final String SQL_EXIST = "SELECT usuario.id, user_name 'nombre', password , id_rol FROM usuario INNER JOIN rol ON id_rol = rol.id WHERE user_name=? AND password=? ;";

	private static UsuarioDAO INSTANCE;

	private UsuarioDAO() {
		super();
	}

	public static synchronized UsuarioDAO getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new UsuarioDAO();
		}

		return INSTANCE;
	}

	@Override
	public List<Usuario> getAll() {
		List<Usuario> registros = new ArrayList<Usuario>();

		try (Connection con = ConnectionManager.getConnection();
				PreparedStatement pst = con.prepareStatement(SQL_GET_ALL);
				
				ResultSet rs = pst.executeQuery()) {
			Rol r = new Rol();
			LOG.debug(pst);
			while(rs.next()) {
				Usuario u = new Usuario();
				u.setId(rs.getInt("id"));
				u.setNombre(rs.getString("user_name"));
				u.setPassword(rs.getString("password"));
				r.setId(rs.getInt("id_rol"));
				r.setNombre(rs.getString("nombre_rol"));
				u.setRol(r);
				registros.add(u);
			}
			
		} catch (Exception e) {
			LOG.error(e);
		}

		return registros;
	}

	@Override
	public Usuario getById(int id) throws Exception {
		Usuario usuario = null;
		
		try(Connection con = ConnectionManager.getConnection();
				PreparedStatement pst = con.prepareStatement(SQL_GET_BY_ID)) {
			
			
			pst.setInt(1, id);
			LOG.debug(pst);
			try(ResultSet rs = pst.executeQuery()){
				if (rs.next()) {
					usuario = new Usuario();
					usuario.setId(rs.getInt("id"));
					usuario.setNombre(rs.getString("user_name"));
					usuario.setPassword(rs.getString("password"));
					int idRol = rs.getInt("id_rol");
					String nombreRol = rs.getString("nombre_rol");
					Rol r = new Rol(idRol, nombreRol);
					usuario.setRol(r);
				}
			}
		} catch (Exception e) {
			LOG.error("Error al obtener el usuario por id");
		}
		
		return usuario;
	}

	@Override
	public Usuario delete(int id) throws Exception {
		Usuario usuario = null;
		try (Connection con = ConnectionManager.getConnection();
				PreparedStatement pst = con.prepareStatement(SQL_DELETE)) {

			pst.setInt(1, id);

			usuario = this.getById(id);

			int filasAfectadas = pst.executeUpdate();
			if (filasAfectadas == 1) {
				usuario = null;
			} else {
				throw new Exception("No se han encontrado registros para el id =" + id );
			}

		}
		return usuario;
	}

	@Override
	public Usuario update(int id, Usuario pojo) throws Exception {
		Usuario usuario = null;
		try (Connection con = ConnectionManager.getConnection();
				PreparedStatement pst = con.prepareStatement(SQL_UPDATE)) {
			pst.setString(1, pojo.getNombre());
			pst.setString(2, pojo.getPassword());
			pst.setInt(3, pojo.getRol().getId());
			
			pst.setInt(4,id);

			usuario = this.getById(id);

			int filasAfectadas = pst.executeUpdate();
			if (filasAfectadas == 1) {
				usuario.setId(id);
				usuario.setNombre(pojo.getNombre());
				usuario.setPassword(pojo.getPassword());
				
				//TODO rolUsuario usuario.setRol(pojo.getRol().getId());
			} else {
				throw new Exception("No se han encontrado registros para el id =" + id);
			}

		}
		return usuario;
	}

	@Override
	public Usuario create(Usuario pojo) throws Exception {
		try (Connection con = ConnectionManager.getConnection();
				PreparedStatement pst = con.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS)) {

			pst.setString(1, pojo.getNombre());
			pst.setString(2, pojo.getPassword());
			pst.setInt(3, pojo.getRol().getId());
			
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

	@Override
	public Usuario exist(String nombre, String password) {
		Usuario user = null;

		LOG.debug("usuario= " + nombre + " contraseña= " + password);

		try (Connection con = ConnectionManager.getConnection();
				PreparedStatement pst = con.prepareStatement(SQL_EXIST);) {

			pst.setString(1, nombre);
			pst.setString(2, password);
			LOG.debug(pst);

			try (ResultSet rs = pst.executeQuery()) {

				if (rs.next()) {
					user = new Usuario();
					user.setId(rs.getInt("id"));
					user.setNombre(rs.getString("nombre"));
					user.setPassword(rs.getString("password"));
					int idRol = rs.getInt("id_rol");
					
					if (Rol.ROL_ADMIN == idRol) {
						user.setRol(new Rol(idRol, "admin"));
					}else {
						user.setRol(new Rol(idRol, "usuario"));
					}
					
				}
			}

		} catch (Exception e) {
			LOG.error(e);
		}

		return user;
	}
	
	private Usuario mapper(ResultSet rs) throws SQLException {

		Usuario u = new Usuario();
		u.setId(rs.getInt("id_usuario"));
		u.setNombre(rs.getString("user_name"));
		u.setPassword(rs.getString("password"));

		Rol r = new Rol();
		r.setId(rs.getInt("id_rol"));
		r.setNombre(rs.getString("nombre_rol"));

		u.setRol(r);

		return u;
	}


}
