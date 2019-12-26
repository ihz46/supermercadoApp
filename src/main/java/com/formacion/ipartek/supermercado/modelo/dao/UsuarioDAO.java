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
import com.formacion.ipartek.supermercado.modelo.pojo.Producto;
import com.formacion.ipartek.supermercado.modelo.pojo.Usuario;

public class UsuarioDAO implements IUsuarioDAO {

	private final static Logger LOG = Logger.getLogger(UsuarioDAO.class);

	private static final String SQL_GET_ALL = "SELECT id, user_name, password FROM usuario;";
	private static final String SQL_GET_BY_ID = "SELECT id, user_name, password from usuario WHERE id= ?;";
	private static final String SQL_INSERT = "INSERT INTO usuario ( user_name, password ) VALUES (?,?);";
	private static final String SQL_DELETE = "DELETE FROM usuario WHERE id=?;";
	private static final String SQL_UPDATE = "UPDATE usuario SET user_name=?, password = ? WHERE id = ?;";
	private static final String SQL_EXIST = "SELECT id, user_name, PASSWORD FROM usuario WHERE user_name=? AND password=?;";

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
			
			while(rs.next()) {
				Usuario u = new Usuario();
				u.setId(rs.getInt("id"));
				u.setUser_name(rs.getString("user_name"));
				u.setPassword(rs.getString("password"));
				registros.add(u);
			}
			
		} catch (Exception e) {
			LOG.error("Error al obtener la lista de usuarios");
		}

		return registros;
	}

	@Override
	public Usuario getById(int id) throws Exception {
		Usuario usuario = null;
		
		try(Connection con = ConnectionManager.getConnection();
				PreparedStatement pst = con.prepareStatement(SQL_GET_BY_ID)) {
			
			
			pst.setInt(1, id);
			
			try(ResultSet rs = pst.executeQuery()){
				if (rs.next()) {
					usuario = new Usuario();
					usuario.setId(rs.getInt("id"));
					usuario.setUser_name(rs.getString("user_name"));
					usuario.setPassword(rs.getString("password"));
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
				throw new Exception("No se han encontrado registros para el id =" + id);
			}

		}
		return usuario;
	}

	@Override
	public Usuario update(int id, Usuario pojo) throws Exception {
		Usuario usuario = null;
		try (Connection con = ConnectionManager.getConnection();
				PreparedStatement pst = con.prepareStatement(SQL_UPDATE)) {
			pst.setString(1, pojo.getUser_name());
			pst.setString(2, pojo.getPassword());
			
			pst.setInt(3,id);

			usuario = this.getById(id);

			int filasAfectadas = pst.executeUpdate();
			if (filasAfectadas == 1) {
				usuario.setId(id);
				usuario.setUser_name(pojo.getUser_name());
				usuario.setPassword(pojo.getPassword());
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

			pst.setString(1, pojo.getUser_name());
			pst.setString(2, pojo.getPassword());
			
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
	public Usuario exist(String user_name, String password) {
		Usuario user = null;

		LOG.debug("usuario= " + user_name + " contraseña= " + password);

		try (Connection con = ConnectionManager.getConnection();
				PreparedStatement pst = con.prepareStatement(SQL_EXIST);) {

			pst.setString(1, user_name);
			pst.setString(2, password);
			LOG.debug(pst);

			try (ResultSet rs = pst.executeQuery()) {

				if (rs.next()) {
					user = new Usuario();
					user.setId(rs.getInt("id"));
					user.setUser_name(rs.getString("user_name"));
					user.setPassword(rs.getString("password"));
				}
			}

		} catch (Exception e) {
			LOG.error(e);
		}

		return user;
	}

}
