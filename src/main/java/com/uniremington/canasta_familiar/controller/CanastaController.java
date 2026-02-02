package com.uniremington.canasta_familiar.controller;

import com.uniremington.canasta_familiar.model.ItemCanasta;
import com.uniremington.canasta_familiar.service.CanastaService;
import com.uniremington.canasta_familiar.service.PdfService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import java.util.List;

/**
 * Controlador MVC
 * Maneja las vistas Thymeleaf
 * Endpoints:
 * - GET: mostrar formulario principal
 * - POST: agregar producto
 * - POST: mostrar resumen
 */
@Controller
public class CanastaController {

    @Autowired
    private CanastaService canastaService;

    @Autowired
    private PdfService pdfService;

    /**
     * Muestra la vista principal con el formulario
     * GET /
     */
    @GetMapping("/")
    public String mostrarFormulario(Model model) {
        model.addAttribute("itemCanasta", new ItemCanasta());
        model.addAttribute("items", canastaService.obtenerItems());
        model.addAttribute("total", canastaService.calcularTotal());
        return "canasta";
    }

    /**
     * Agrega un producto a la canasta
     * POST /agregar
     */
    @PostMapping("/agregar")
    public String agregarProducto(@ModelAttribute ItemCanasta item,
            RedirectAttributes redirectAttributes) {
        try {
            canastaService.agregarItem(item);
            redirectAttributes.addFlashAttribute("mensaje",
                    "Producto agregado exitosamente");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("mensaje",
                    "Error: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "danger");
        }

        return "redirect:/";
    }

    /**
     * Muestra la vista de resumen con estadísticas
     * POST /resumen
     */
    @PostMapping("/resumen")
    public String mostrarResumen(Model model) {
        model.addAttribute("items", canastaService.obtenerItems());
        model.addAttribute("total", canastaService.calcularTotal());
        model.addAttribute("promedio", canastaService.calcularPromedio());
        model.addAttribute("productosCostosos",
                canastaService.obtenerProductosMasCostosos());

        return "resumen";
    }

    @PostMapping("/descargar-pdf")
    public ResponseEntity<byte[]> descargarPdf() {
        List<ItemCanasta> items = canastaService.obtenerItems();
        double total = canastaService.calcularTotal();
        double promedio = canastaService.calcularPromedio();
        List<ItemCanasta> costosos = canastaService.obtenerProductosMasCostosos();

        byte[] pdfBytes = pdfService.generarPdfResumen(items, total, promedio, costosos);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "resumen-canasta-familiar.pdf");
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

        return ResponseEntity.ok()
                .headers(headers)
                .body(pdfBytes);
    }

    /**
     * Limpia la canasta
     * POST /limpiar
     */
    @PostMapping("/limpiar")
    public String limpiarCanasta(RedirectAttributes redirectAttributes) {
        canastaService.limpiarCanasta();
        redirectAttributes.addFlashAttribute("mensaje",
                "Canasta limpiada exitosamente");
        redirectAttributes.addFlashAttribute("tipoMensaje", "info");
        return "redirect:/";
    }
}