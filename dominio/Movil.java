package SMA_BusquedaMoviles.dominio;

import java.io.Serializable;

public class Movil implements Serializable {
	private String nombre;
	private double precio;
	private String tienda;
	
	public Movil(String nombre, double precio, String tienda) {
		this.nombre=nombre;
		this.precio=precio;
		this.tienda=tienda;
	}
	
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public double getPrecio() {
		return precio;
	}

	public void setPrecio(double precio) {
		this.precio = precio;
	}
	
	public String getTienda() {
		return tienda;
	}

	public void setTienda(String tienda) {
		this.tienda = tienda;
	}
}

