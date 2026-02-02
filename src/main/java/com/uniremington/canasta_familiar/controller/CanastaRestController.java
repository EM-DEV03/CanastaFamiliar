package com.uniremington.canasta_familiar.controller;

import com.uniremington.canasta_familiar.model.ItemCanasta;
import com.uniremington.canasta_familiar.service.CanastaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controlador REST
 * Expone la información en formato JSON
 * Endpoints:
 * - GET /api/canasta/items
 * - GET /api/canasta/resumen
 * Demuestra el uso de API REST en Spring Boot
 */
@RestController
@RequestMapping("/api/canasta")
public class CanastaRestController {

    @Autowired
    private CanastaService canastaService;

    /**
     * Obtiene todos los items de la canasta en formato JSON
     * GET /api/canasta/items
     * 
     * @return lista de items
     */
    @GetMapping("/items")
    public ResponseEntity<List<ItemCanasta>> obtenerItems() {
        List<ItemCanasta> items = canastaService.obtenerItems();
        return ResponseEntity.ok(items);
    }

    /**
     * Obtiene el resumen completo de la canasta en formato JSON
     * GET /api/canasta/resumen
     * 
     * @return objeto con total, promedio, cantidad de items y productos más
     *         costosos
     */
    @GetMapping("/resumen")
    public ResponseEntity<Map<String, Object>> obtenerResumen() {
        Map<String, Object> resumen = new HashMap<>();

        resumen.put("items", canastaService.obtenerItems());
        resumen.put("cantidadItems", canastaService.obtenerCantidadItems());
        resumen.put("total", canastaService.calcularTotal());
        resumen.put("promedio", canastaService.calcularPromedio());
        resumen.put("productosMasCostosos", canastaService.obtenerProductosMasCostosos());

        return ResponseEntity.ok(resumen);
    }
}