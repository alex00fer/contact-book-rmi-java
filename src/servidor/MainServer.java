package servidor;

import java.rmi.Remote;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class MainServer {
	public static void main(String[] args) {
		Registry reg = null;
		try {
			System.out.println("Crea el registro escuchando en el puerto 5555");
			reg = LocateRegistry.createRegistry(5555);
		} catch (Exception e) {
			System.err.println("ERROR: No se ha podido crear el registro");
			e.printStackTrace();
		}
		System.out.println("Creando el objeto servidor");
		RMIAgendaImpl agendaObject = new RMIAgendaImpl();
		try {
			System.out.println("Inscribiendo el objeto servidor en el registro");
			System.out.println("Nombre de registro: Agenda");
			reg.rebind("Agenda", UnicastRemoteObject.exportObject((Remote) agendaObject, 0));
		} catch (Exception e) {
			System.err.println("ERROR: No se ha podido inscribir el objeto servidor.");
			e.printStackTrace();
		}
		System.out.println("Escuchando peticiones...");
	}
}
