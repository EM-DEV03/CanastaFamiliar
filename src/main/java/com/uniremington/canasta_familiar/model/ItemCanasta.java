package com.uniremington.canasta_familiar.model;

/**
 * Representa un producto de la canasta familiar
 * Contiene nombre, precio y cantidad
 * No usa base de datos ni anotaciones JPA
 */
public class ItemCanasta {

    private String nombre;
    private double precio;
    private int cantidad;

    // Constructor vacío
    public ItemCanasta() {
    }

    // Constructor con parámetros
    public ItemCanasta(String nombre, double precio, int cantidad) {
        this.nombre = nombre;
        this.precio = precio;
        this.cantidad = cantidad;
    }

    // Getters y Setters
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

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    /**
     * Calcula el subtotal del item (precio * cantidad)
     * 
     * @return subtotal calculado
     */
    public double calcularSubtotal() {
        return this.precio * this.cantidad;
    }

    @Override
    public String toString() {
        return "ItemCanasta{" +
                "nombre='" + nombre + '\'' +
                ", precio=" + precio +
                ", cantidad=" + cantidad +
                ", subtotal=" + calcularSubtotal() +
                '}';
    }
}