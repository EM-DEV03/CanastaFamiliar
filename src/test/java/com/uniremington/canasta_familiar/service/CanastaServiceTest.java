package com.uniremington.canasta_familiar.service;

import com.uniremington.canasta_familiar.model.ItemCanasta;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CanastaServiceTest {

    private CanastaService canastaService;

    // Este método se ejecuta ANTES de cada test
    @BeforeEach
    void setUp() {
        canastaService = new CanastaService();
    }

    // PRUEBA 1: Agregar un item válido
    @Test
    @DisplayName("Test: Agregar item válido a la canasta")
    void testAgregarItemValido() {
        // 1. ARRANGE (Preparar)
        ItemCanasta item = new ItemCanasta("Arroz", 5000, 2);

        // 2. ACT (Actuar)
        canastaService.agregarItem(item);

        // 3. ASSERT (Verificar)
        assertEquals(1, canastaService.obtenerCantidadItems());
    }

    // PRUEBA 2: Calcular total correcto
    @Test
    @DisplayName("Test: Calcular total correcto")
    void testCalcularTotalCorrecto() {
        // Arrange
        canastaService.agregarItem(new ItemCanasta("Arroz", 5000, 2));
        canastaService.agregarItem(new ItemCanasta("Frijol", 3000, 1));

        // Act
        double total = canastaService.calcularTotal();

        // Assert
        // (5000*2) + (3000*1) = 10000 + 3000 = 13000
        assertEquals(13000, total, 0.01);
    }

    // PRUEBA 3: Precio inválido debe lanzar excepción
    @Test
    @DisplayName("Test: Precio inválido lanza excepción")
    void testPrecioInvalido() {
        // Arrange
        ItemCanasta item = new ItemCanasta("Producto", -100, 1);

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            canastaService.agregarItem(item);
        });

        assertEquals("El precio debe ser mayor a cero", exception.getMessage());
    }

    // PRUEBA 4: Cantidad inválida debe lanzar excepción
    @Test
    @DisplayName("Test: Cantidad inválida lanza excepción")
    void testCantidadInvalida() {
        // Arrange
        ItemCanasta item = new ItemCanasta("Producto", 1000, -5);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            canastaService.agregarItem(item);
        });
    }

    // PRUEBA 5: Nombre vacío debe lanzar excepción
    @Test
    @DisplayName("Test: Nombre vacío lanza excepción")
    void testNombreVacio() {
        // Arrange
        ItemCanasta item = new ItemCanasta("", 1000, 1);

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            canastaService.agregarItem(item);
        });

        assertEquals("El nombre del producto no puede estar vacío",
                exception.getMessage());
    }

    // PRUEBA 6: Calcular promedio correcto
    @Test
    @DisplayName("Test: Calcular promedio correcto")
    void testCalcularPromedioCorrecto() {
        // Arrange
        canastaService.agregarItem(new ItemCanasta("Producto1", 1000, 1));
        canastaService.agregarItem(new ItemCanasta("Producto2", 2000, 1));
        canastaService.agregarItem(new ItemCanasta("Producto3", 3000, 1));

        // Act
        double promedio = canastaService.calcularPromedio();

        // Assert
        // (1000 + 2000 + 3000) / 3 = 2000
        assertEquals(2000, promedio, 0.01);
    }

    // PRUEBA 7: Promedio con canasta vacía
    @Test
    @DisplayName("Test: Promedio con canasta vacía es cero")
    void testPromedioCanastaVacia() {
        // Act
        double promedio = canastaService.calcularPromedio();

        // Assert
        assertEquals(0, promedio, 0.01);
    }

    // PRUEBA 8: Obtener productos más costosos
    @Test
    @DisplayName("Test: Obtener productos más costosos")
    void testObtenerProductosMasCostosos() {
        // Arrange
        canastaService.agregarItem(new ItemCanasta("Producto1", 1000, 1));
        canastaService.agregarItem(new ItemCanasta("Producto2", 5000, 1));
        canastaService.agregarItem(new ItemCanasta("Producto3", 3000, 1));
        canastaService.agregarItem(new ItemCanasta("Producto4", 8000, 1));

        // Act
        List<ItemCanasta> costosos = canastaService.obtenerProductosMasCostosos();

        // Assert
        assertEquals(3, costosos.size());
        assertEquals("Producto4", costosos.get(0).getNombre()); // 8000
        assertEquals("Producto2", costosos.get(1).getNombre()); // 5000
        assertEquals("Producto3", costosos.get(2).getNombre()); // 3000
    }

    // PRUEBA 9: Limpiar canasta
    @Test
    @DisplayName("Test: Limpiar canasta")
    void testLimpiarCanasta() {
        // Arrange
        canastaService.agregarItem(new ItemCanasta("Producto1", 1000, 1));
        canastaService.agregarItem(new ItemCanasta("Producto2", 2000, 1));

        // Act
        canastaService.limpiarCanasta();

        // Assert
        assertEquals(0, canastaService.obtenerCantidadItems());
        assertEquals(0, canastaService.calcularTotal(), 0.01);
    }

    // PRUEBA 10: Subtotal calculado correctamente
    @Test
    @DisplayName("Test: Subtotal calculado correctamente")
    void testSubtotalItem() {
        // Arrange
        ItemCanasta item = new ItemCanasta("Producto", 5000, 3);

        // Act
        double subtotal = item.calcularSubtotal();

        // Assert
        assertEquals(15000, subtotal, 0.01);
    }
}