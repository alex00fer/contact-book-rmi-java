package shared;

import java.rmi.Remote;
import java.rmi.RemoteException;

import modelo.Contacto;

public interface RMIAgendaInterface extends Remote {
	public boolean registro(String user, String password) throws RemoteException;
	public boolean login(String user, String password) throws RemoteException;
	public boolean crearContacto(Contacto nuevo) throws RemoteException;
	public boolean borrarUno(String nombre) throws RemoteException;
	public boolean borrarTodo() throws RemoteException;
	public boolean modificarUno(String nombre, Contacto contactoModificado) throws RemoteException;
	public Contacto traerUno(String nombre) throws RemoteException;
	public Contacto[] traerTodos() throws RemoteException;
}
