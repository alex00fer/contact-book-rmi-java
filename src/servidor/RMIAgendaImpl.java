package servidor;

import java.rmi.RemoteException;

import modelo.AgendaDB;
import modelo.Contacto;
import shared.RMIAgendaInterface;

public class RMIAgendaImpl implements RMIAgendaInterface {

	private static AgendaDB db = new AgendaDB();
	private boolean isLoggedIn = false;
	private String loggedInAsUser = null;
	
	@Override
	public boolean registro(String user, String password) throws RemoteException {
		return db.register(user, password);
	}

	@Override
	public boolean login(String user, String password) throws RemoteException {
		boolean res = db.login(user, password);
		if (res) {
			isLoggedIn = true;
			loggedInAsUser = user;
		}
		return res;
	}

	@Override
	public boolean crearContacto(Contacto nuevo) throws RemoteException {
		if (!isLoggedIn) 
			throw new RuntimeException("El usuario no ha iniciado sesión");
		
		return db.insertarContacto(nuevo, loggedInAsUser);
	}

	@Override
	public boolean borrarUno(String nombre) throws RemoteException {
		if (!isLoggedIn) 
			throw new RuntimeException("El usuario no ha iniciado sesión");
		
		return db.borrarContacto(nombre, loggedInAsUser);
	}

	@Override
	public boolean borrarTodo() throws RemoteException {
		if (!isLoggedIn) 
			throw new RuntimeException("El usuario no ha iniciado sesión");
		
		return db.borrarTodosLosContactos(loggedInAsUser);
	}

	@Override
	public boolean modificarUno(String nombre, Contacto contactoModificado) throws RemoteException {
		if (!isLoggedIn) 
			throw new RuntimeException("El usuario no ha iniciado sesión");
		
		return db.modificarContacto(nombre, contactoModificado, loggedInAsUser);
	}

	@Override
	public Contacto traerUno(String nombre) throws RemoteException {
		if (!isLoggedIn) 
			throw new RuntimeException("El usuario no ha iniciado sesión");

		return db.obtenerContacto(nombre, loggedInAsUser);
	}

	@Override
	public Contacto[] traerTodos() throws RemoteException {
		if (!isLoggedIn) 
			throw new RuntimeException("El usuario no ha iniciado sesión");
		
		return db.obtenerContactos(loggedInAsUser);
	}

}
