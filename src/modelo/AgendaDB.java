package modelo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class AgendaDB {
	Connection connection = null;
	public AgendaDB() {
		try {
			// create a database connection
			connection = DriverManager.getConnection("jdbc:sqlite:agenda.db");
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(30); // set timeout to 30 sec.

			statement.executeUpdate("create table if not exists users (user string, password string)");
			statement.executeUpdate("create table if not exists contactos (user string, nombre string, telefono string, direccion string)");
			
		} catch (SQLException e) {
			// if the error message is "out of memory",
			// it probably means no database file is found
			System.err.println(e.getMessage());
		} 
	}
	
	public boolean register(String user, String pass) {
		if (connection == null) 
			throw new RuntimeException("No se pudo encontrar una conexión con la base de datos");
		
		Statement statement;
		try {
			statement = connection.createStatement();
			statement.setQueryTimeout(1); // set timeout to 1 sec.
			
			statement.executeUpdate(String.format("insert into users values('%s', '%s')", user, pass));
			
			return true;

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean login(String user, String pass) {
		if (connection == null) 
			throw new RuntimeException("No se pudo encontrar una conexión con la base de datos");
		
		Statement statement;
		try {
			statement = connection.createStatement();
			statement.setQueryTimeout(1); // set timeout to 1 sec.
			
			ResultSet rs = statement.executeQuery(
					String.format("select count(*) from users where user = '%s' and password = '%s'", user, pass));
			rs.next();
			int count = rs.getInt(1);
			return count >= 1;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		

	}
	
	public Contacto[] obtenerContactos(String user) {
		
		if (connection == null) 
			throw new RuntimeException("No se pudo encontrar una conexión con la base de datos");
		
		List<Contacto> contactos = new ArrayList<Contacto>();
		
		try {
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(1); // set timeout to 1 sec.
			
			ResultSet rs = statement.executeQuery(
					String.format("select * from contactos where user='%s'", user));
			
			while (rs.next()) {
				contactos.add(new Contacto(
						rs.getString("nombre"), 
						rs.getString("telefono"), 
						rs.getString("direccion")
						));				
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return contactos.toArray(new Contacto[contactos.size()]);
	}
	
	public Contacto obtenerContacto(String nombre, String user) {
		
		if (connection == null) 
			throw new RuntimeException("No se pudo encontrar una conexión con la base de datos");
		
		try {
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(1); // set timeout to 1 sec.
			
			ResultSet rs = statement.executeQuery(
					String.format("select * from contactos where user='%s' and nombre='%s'", 
							user, nombre));
			
			rs.next();
				
			return new Contacto(
						rs.getString("nombre"), 
						rs.getString("telefono"), 
						rs.getString("direccion")
						);				
			

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public boolean insertarContacto(Contacto c, String user) {
		if (connection == null) 
			throw new RuntimeException("No se pudo encontrar una conexión con la base de datos");
		
		Statement statement;
		try {
			statement = connection.createStatement();
			statement.setQueryTimeout(1); // set timeout to 1 sec.
			
			statement.executeUpdate(
					String.format("insert into contactos values('%s', '%s', '%s', '%s')", 
							user, c.nombre, c.telefono, c.direccion));
			
			return true;

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean borrarContacto(String nombreContacto, String user) {
		if (connection == null) 
			throw new RuntimeException("No se pudo encontrar una conexión con la base de datos");
		
		Statement statement;
		try {
			statement = connection.createStatement();
			statement.setQueryTimeout(1); // set timeout to 1 sec.
			
			statement.executeUpdate(
					String.format("delete from contactos where user='%s' and nombre='%s'", 
							user, nombreContacto));
			
			return true;

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean borrarTodosLosContactos(String user) {
		if (connection == null) 
			throw new RuntimeException("No se pudo encontrar una conexión con la base de datos");
		
		Statement statement;
		try {
			statement = connection.createStatement();
			statement.setQueryTimeout(1); // set timeout to 1 sec.
			
			statement.executeUpdate(
					String.format("delete from contactos where user='%s'", 
							user));
			
			return true;

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean modificarContacto(String nombreContacto, Contacto contactoModificado, String user) {
		if (connection == null) 
			throw new RuntimeException("No se pudo encontrar una conexión con la base de datos");
		
		Statement statement;
		try {
			statement = connection.createStatement();
			statement.setQueryTimeout(1); // set timeout to 1 sec.
			
			statement.executeUpdate(
					String.format("update contactos set nombre='%s', telefono='%s', direccion='%s' "
							+ "where user='%s' and nombre='%s'",
							contactoModificado.nombre, contactoModificado.telefono, contactoModificado.direccion,
							user, nombreContacto));
			
			return true;

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
}
