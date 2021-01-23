package modelo;

import java.io.Serializable;


public class Contacto implements Serializable {

	private static final long serialVersionUID = 56390134548030L;
	
	String nombre;
	String telefono;
	String direccion;
	
	public Contacto(String nombre, String telefono, String direccion) {
		this.nombre = nombre;
		this.telefono = telefono;
		this.direccion = direccion;
	}

	@Override
	public String toString() {
		return String.format("> Nombre = %s, Telefono=%s, Direccion=%s", 
				nombre, telefono, direccion);
	}
	
	
}
