package cliente;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

import modelo.Contacto;
import shared.RMIAgendaInterface;

public class MainClient {
	public static void main(String[] args) {
		RMIAgendaInterface agenda = null;
		try {
			System.out.println("Localizando el registro de objetos remitos");
			Registry registry = LocateRegistry.getRegistry("localhost", 5555);
			System.out.println("Obteniendo el objeto remoto");
			agenda = (RMIAgendaInterface) registry.lookup("Agenda");
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Serivicio de agenda listo");
		
		boolean inMenu = true;
		while (inMenu) {
			System.out.println("Acciones disponibles: \n"
					+ " [1] Registrarse \n"
					+ " [2] Login \n"
					+ " [3] Mostrar todos los contactos \n"
					+ " [4] Buscar un único contacto \n"
					+ " [5] Crear nuevo contacto \n"
					+ " [6] Modificar contacto \n"
					+ " [7] Borrar un contacto \n"
					+ " [8] Borrar todos los contactos \n"
					+ " [9] Salir \n"
					);
			switch (InputController.readInteger("  Seleccione una acción", 1, 9)) {
			case 1:
				register(agenda);
				break;
			case 2:
				login(agenda);
				break;
			case 3:
				listadoContactos(agenda);
				break;
			case 4:
				verContacto(agenda);
				break;
			case 5:
				crearContacto(agenda);
				break;
			case 6:
				modificarContacto(agenda);
				break;
			case 7:
				borrarContacto(agenda);
				break;
			case 8:
				borrarTodos(agenda);
				break;
			default:
				inMenu = false;
				break;
			}
			
			if (inMenu)
				InputController.waitForEnter();
		}
		
	}
	
	public static void login(RMIAgendaInterface agenda) {
		String user = InputController.readString("   Usuario");
		String pass = InputController.readString("   Password");
		try {
			if (agenda.login(user, pass)) {
				System.out.println("Sesión iniciada correctamente");
			}
			else {
				System.out.println("El servidor indicó que no se pudo realizar el login. Compruebe que sus credenciales son correctas");
			}
		} catch (RemoteException e) {
			System.out.println("No se pudo completar la acción");
			e.printStackTrace();
		}
	}
	
	public static void register(RMIAgendaInterface agenda) {
		String user = InputController.readString("   Usuario");
		String pass = InputController.readString("   Password");
		try {
			if (agenda.registro(user, pass)) {
				System.out.println("Registrado correctamente");
				if (agenda.login(user, pass)) {
					System.out.println("Sesión iniciada correctamente");
				}
			}
			else {
				System.out.println("El servidor indicó que la operación falló");
			}
		} catch (RemoteException e) {
			System.out.println("No se pudo completar la acción");
			e.printStackTrace();
		}
	}
	
	public static void modificarContacto(RMIAgendaInterface agenda) {
		String nombreOriginal = InputController.readString("   Nombre");
		String nombre = InputController.readString("   Nuevo Nombre");
		String tlfn = InputController.readString("   Nuevo Teléfono");
		String dir = InputController.readString("   Nueva Dirección");
		try {
			if (agenda.modificarUno(nombreOriginal, new Contacto(nombre, tlfn, dir))) {
				System.out.println("Contacto modificado correctamente");
			}
			else {
				System.out.println("El servidor indicó que la operación falló");
			}
		} catch (RemoteException e) {
			System.out.println("No se pudo completar la acción");
			e.printStackTrace();
		}
	}
	
	public static void borrarContacto(RMIAgendaInterface agenda) {
		String nombre = InputController.readString("   Nombre");
		try {
			if (agenda.borrarUno(nombre)) {
				System.out.println("Borrado correctamente");
			}
			else {
				System.out.println("El servidor indicó que la operación falló");
			}
		} catch (RemoteException e) {
			System.out.println("No se pudo completar la acción");
			e.printStackTrace();
		}
	}
	
	public static void borrarTodos(RMIAgendaInterface agenda) {
		try {
			if (agenda.borrarTodo()) {
				System.out.println("Borrado correctamente");
			}
			else {
				System.out.println("El servidor indicó que la operación falló");
			}
		} catch (RemoteException e) {
			System.out.println("No se pudo completar la acción");
			e.printStackTrace();
		}
	}
	
	public static void crearContacto(RMIAgendaInterface agenda) {
		String nombre = InputController.readString("   Nombre");
		String tlfn = InputController.readString("   Teléfono");
		String dir = InputController.readString("   Dirección");
		try {
			if (agenda.crearContacto(new Contacto(nombre, tlfn, dir))) {
				System.out.println("Contacto creado correctamente");
			}
			else {
				System.out.println("El servidor indicó que la operación falló");
			}
		} catch (RemoteException e) {
			System.out.println("No se pudo completar la acción");
			e.printStackTrace();
		}
	}
	
	public static void listadoContactos(RMIAgendaInterface agenda) {
		try {
			Contacto[] contactos = agenda.traerTodos();
			
			if (contactos.length <= 0) System.out.println("No tienes contactos");
				
			for (Contacto contacto : contactos) {
				System.out.println(contacto.toString());
			}

		} catch (RemoteException e) {
			System.out.println("No se pudo completar la acción");
			e.printStackTrace();
		}
	}
	
	public static void verContacto(RMIAgendaInterface agenda) {
		String nombre = InputController.readString("   Nombre");
		try {
			Contacto contacto = agenda.traerUno(nombre);
			
			if (contacto == null) System.out.println("El contacto no se encontró");
			else System.out.println(contacto.toString());

		} catch (RemoteException e) {
			System.out.println("No se pudo completar la acción");
			e.printStackTrace();
		}
	}
}


class InputController {
	
	private static Scanner scanner = new Scanner(System.in);
	
	public static String readString(String message) 
	{
		return readString(message, Integer.MIN_VALUE, Integer.MAX_VALUE);
	}
	
	public static String readString(String message, int min, int max) 
	{
		String input = null;
	    boolean valid = false;
	    
	    do {
	    	if (message == null)
		    	System.out.print("Entrada: ");
	    	else
	    		System.out.print(message + ": ");
		    input = scanner.nextLine().trim();
		    if (input != null && !input.isEmpty() && input.length() >= min && input.length() <= max)
		        	valid = true;
		    else
		        	System.out.println("Entrada inválida");
	    } while (!valid);
	    
	    return input;
	}
	
	public static int readInteger(String message, int min, int max) 
	{
		String input = null;
	    int number = 0;
	    boolean valid = false;
	    
	    do {
		    try {
		    	if (message == null)
		    		System.out.print("Introduzca un número: ");
		    	else
		    		System.out.print(message + ": ");
		        input = scanner.nextLine();
		        number = Integer.parseInt(input);
		        if (number >= min && number <= max)
		        	valid = true;
		        else
		        	System.out.println("Entrada fuera de rango.");
		    } catch (NumberFormatException ex) {
		       System.out.println("Entrada inválida.");
		    }
	    } while (!valid);
	    
	    return number;
	}
	
	public static void waitForEnter() {
		System.out.println();
		System.out.println("Presiona 'ENTER' para continuar...");		
		scanner.nextLine();
	}
	
	public static void waitFor(long ms) 
	{
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {}
	}

}
