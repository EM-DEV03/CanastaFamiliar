package com.uniremington.canasta_familiar.service;

import com.uniremington.canasta_familiar.model.ItemCanasta;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Clase de servicio donde se implementa toda la lógica de negocio
 * Se encarga de:
 * - Validar datos (precio, cantidad, nombre)
 * - Almacenar productos en memoria (List)
 * - Calcular total de la canasta
 * - Calcular promedio de precios
 * - Obtener los productos más costosos
 */
@Service
public class CanastaService {

    // Almacenamiento en memoria de los items
    private List<ItemCanasta> items = new ArrayList<>();

    /**
     * Agrega un item a la canasta después de validarlo
     * @param item el item a agregar
     * @throws IllegalArgumentException si los datos no son válidos
     */
    public void agregarItem(ItemCanasta item) {
        validarItem(item);
        items.add(item);
    }

    /**
     * Valida que el item tenga datos correctos
     * @param item el item a validar
     * @throws IllegalArgumentException si algún dato es inválido
     */
    private void validarItem(ItemCanasta item) {
        if (item.getNombre() == null || item.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del producto no puede estar vacío");
        }

        if (item.getPrecio() <= 0) {
            throw new IllegalArgumentException("El precio debe ser mayor a cero");
        }

        if (item.getCantidad() <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a cero");
        }
    }

    /**
     * Obtiene todos los items de la canasta
     * @return lista de items
     */
    public List<ItemCanasta> obtenerItems() {
        return new ArrayList<>(items);
    }

    /**
     * Calcula el total de la canasta sumando todos los subtotale
     * @return total de la canasta
     */
    public double calcularTotal() {
        return items.stream()
                .mapToDouble(ItemCanasta::calcularSubtotal)
                .sum();
    }

    /**
     * Calcula el promedio de precios de todos los productos
     * @return promedio de precios, 0 si no hay items
     */
    public double calcularPromedio() {
        if (items.isEmpty()) {
            return 0;
        }

        return items.stream()
                .mapToDouble(ItemCanasta::getPrecio)
                .average()
                .orElse(0);
    }

    /**
     * Obtiene los 3 productos más costosos (por precio unitario)
     * @return lista de hasta 3 productos más costosos
     */
    public List<ItemCanasta> obtenerProductosMasCostosos() {
        return items.stream()
                .sorted(Comparator.comparingDouble(ItemCanasta::getPrecio).reversed())
                .limit(3)
                .collect(Collectors.toList());
    }

    /**
     * Limpia todos los items de la canasta
     */
    public void limpiarCanasta() {
        items.clear();
    }

    /**
     * Obtiene la cantidad de items en la canasta
     * @return número de items
     */
    public int obtenerCantidadItems() {
        return items.size();
    }
}