	package com.formacion.ipartek.supermercado.modelo.pojo;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

public class Producto {
	
	
	//Constantes que serán útiles para trabajar con los descuentos.
	public static final int DESCUENTO_MIN=0;
	public static final int DESCUENTO_MAX=100;
	
	private int id;
	
	@NotNull
	@Size(min = 2, max=50)
	@NotBlank
	private String nombre;
	
	@NotNull
	private float precio;
	
	
	private String imagen;
	private String descripcion;
	
	@Min(0)
	@Max(100)
	private int descuento;
	
	
	//Constructor de inicialización
	public Producto() {
		super();
		this.id=0;
		this.nombre="";
		this.precio=0;
		this.imagen="https://image.flaticon.com/icons/png/512/372/372627.png";
		this.descripcion="";
		this.descuento=DESCUENTO_MIN;
	}
	
	//Getters y Setters
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public float getPrecio() {
		return precio;
	}
	public void setPrecio(float precio) {
		this.precio = precio;
	}
	public String getImagen() {
		return imagen;
	}
	public void setImagen(String imagen) {
		this.imagen = imagen;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public int getDescuento() {
		return descuento;
	}
	public void setDescuento(int descuento) {
		this.descuento = descuento;
	}

	@Override
	public String toString() {
		return "Supermercado [id=" + id + ", nombre=" + nombre + ", precio=" + precio + ", imagen=" + imagen
				+ ", descripcion=" + descripcion + ", descuento=" + descuento + "]";
	}
	
	public float getPrecioDescuento() {
		float aDescontar = (this.precio * this.descuento) / 100; 
		float precioFinal = this.precio - aDescontar;
		return precioFinal;
	}
	
	
	
}
