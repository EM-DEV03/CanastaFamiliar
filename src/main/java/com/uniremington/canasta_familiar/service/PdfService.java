package com.uniremington.canasta_familiar.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.uniremington.canasta_familiar.model.ItemCanasta;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class PdfService {

    private final DecimalFormat formatoMoneda = new DecimalFormat("$#,##0");

    public byte[] generarPdfResumen(List<ItemCanasta> items, double total,
            double promedio, List<ItemCanasta> costosos) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Document documento = new Document(PageSize.A4);
            PdfWriter writer = PdfWriter.getInstance(documento, baos);

            // Agregar encabezado y pie de página
            writer.setPageEvent(new PdfPageEventHelper() {
                @Override
                public void onEndPage(PdfWriter writer, Document document) {
                    // Pie de página
                    ColumnText.showTextAligned(writer.getDirectContent(),
                            Element.ALIGN_CENTER,
                            new Phrase("Universidad de Remington - Canasta Familiar",
                                    FontFactory.getFont(FontFactory.HELVETICA, 8, BaseColor.GRAY)),
                            297.5f, 30, 0);

                    // Número de página
                    ColumnText.showTextAligned(writer.getDirectContent(),
                            Element.ALIGN_RIGHT,
                            new Phrase("Página " + writer.getPageNumber(),
                                    FontFactory.getFont(FontFactory.HELVETICA, 8, BaseColor.GRAY)),
                            550, 30, 0);
                }
            });

            documento.open();

            // ========== ENCABEZADO ==========

            // Título principal
            Font tituloFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 24, new BaseColor(41, 128, 185));
            Paragraph titulo = new Paragraph("🛒 CANASTA FAMILIAR", tituloFont);
            titulo.setAlignment(Element.ALIGN_CENTER);
            titulo.setSpacingAfter(10);
            documento.add(titulo);

            // Subtítulo
            Font subtituloFont = FontFactory.getFont(FontFactory.HELVETICA, 16, BaseColor.DARK_GRAY);
            Paragraph subtitulo = new Paragraph("Resumen de Compra", subtituloFont);
            subtitulo.setAlignment(Element.ALIGN_CENTER);
            subtitulo.setSpacingAfter(20);
            documento.add(subtitulo);

            // Línea separadora
            LineSeparator linea = new LineSeparator();
            linea.setLineColor(new BaseColor(41, 128, 185));
            documento.add(new Chunk(linea));
            documento.add(new Paragraph(" "));

            // Fecha y hora
            String fecha = new SimpleDateFormat("dd 'de' MMMM 'de' yyyy - HH:mm").format(new Date());
            Font fechaFont = FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.GRAY);
            Paragraph fechaP = new Paragraph("Generado el: " + fecha, fechaFont);
            fechaP.setAlignment(Element.ALIGN_RIGHT);
            fechaP.setSpacingAfter(20);
            documento.add(fechaP);

            // ========== PANEL DE ESTADÍSTICAS ==========

            PdfPTable panelEstadisticas = new PdfPTable(3);
            panelEstadisticas.setWidthPercentage(100);
            panelEstadisticas.setSpacingBefore(10);
            panelEstadisticas.setSpacingAfter(20);

            // Celda 1: Total
            PdfPCell celdaTotal = crearCeldaEstadistica(
                    "TOTAL A PAGAR",
                    formatoMoneda.format(total),
                    new BaseColor(46, 204, 113));
            panelEstadisticas.addCell(celdaTotal);

            // Celda 2: Promedio
            PdfPCell celdaPromedio = crearCeldaEstadistica(
                    "PROMEDIO",
                    formatoMoneda.format(promedio),
                    new BaseColor(52, 152, 219));
            panelEstadisticas.addCell(celdaPromedio);

            // Celda 3: Cantidad
            PdfPCell celdaCantidad = crearCeldaEstadistica(
                    "PRODUCTOS",
                    String.valueOf(items.size()),
                    new BaseColor(155, 89, 182));
            panelEstadisticas.addCell(celdaCantidad);

            documento.add(panelEstadisticas);

            // ========== TABLA DE PRODUCTOS ==========

            Font tituloSeccionFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, new BaseColor(52, 73, 94));
            Paragraph tituloTabla = new Paragraph("📋 DETALLE DE PRODUCTOS", tituloSeccionFont);
            tituloTabla.setSpacingBefore(10);
            tituloTabla.setSpacingAfter(10);
            documento.add(tituloTabla);

            PdfPTable tabla = new PdfPTable(5);
            tabla.setWidthPercentage(100);
            tabla.setWidths(new int[] { 1, 4, 2, 2, 2 });
            tabla.setSpacingBefore(10);

            // Encabezados de la tabla
            String[] encabezados = { "N°", "Producto", "Precio", "Cant.", "Subtotal" };
            for (String encabezado : encabezados) {
                PdfPCell celda = new PdfPCell(new Phrase(encabezado,
                        FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11, BaseColor.WHITE)));
                celda.setBackgroundColor(new BaseColor(52, 73, 94));
                celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                celda.setPadding(8);
                celda.setBorderWidth(0);
                tabla.addCell(celda);
            }

            // Datos de la tabla
            int contador = 1;
            Font datosFont = FontFactory.getFont(FontFactory.HELVETICA, 10);
            boolean colorAlternado = false;

            for (ItemCanasta item : items) {
                BaseColor colorFondo = colorAlternado ? new BaseColor(236, 240, 241) : BaseColor.WHITE;

                tabla.addCell(crearCeldaDatos(String.valueOf(contador++), datosFont, colorFondo, Element.ALIGN_CENTER));
                tabla.addCell(crearCeldaDatos(item.getNombre(), datosFont, colorFondo, Element.ALIGN_LEFT));
                tabla.addCell(crearCeldaDatos(formatoMoneda.format(item.getPrecio()), datosFont, colorFondo,
                        Element.ALIGN_RIGHT));
                tabla.addCell(crearCeldaDatos(String.valueOf(item.getCantidad()), datosFont, colorFondo,
                        Element.ALIGN_CENTER));
                tabla.addCell(crearCeldaDatos(formatoMoneda.format(item.calcularSubtotal()), datosFont, colorFondo,
                        Element.ALIGN_RIGHT));

                colorAlternado = !colorAlternado;
            }

            // Fila de total
            PdfPCell celdaTotalLabel = new PdfPCell(new Phrase("TOTAL",
                    FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12)));
            celdaTotalLabel.setColspan(4);
            celdaTotalLabel.setHorizontalAlignment(Element.ALIGN_RIGHT);
            celdaTotalLabel.setBackgroundColor(new BaseColor(52, 73, 94));
            celdaTotalLabel.setPadding(8);
            celdaTotalLabel.setBorderWidth(0);
            tabla.addCell(celdaTotalLabel);

            PdfPCell celdaTotalValor = new PdfPCell(new Phrase(formatoMoneda.format(total),
                    FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.WHITE)));
            celdaTotalValor.setHorizontalAlignment(Element.ALIGN_RIGHT);
            celdaTotalValor.setBackgroundColor(new BaseColor(46, 204, 113));
            celdaTotalValor.setPadding(8);
            celdaTotalValor.setBorderWidth(0);
            tabla.addCell(celdaTotalValor);

            documento.add(tabla);

            // ========== TOP 3 PRODUCTOS MÁS COSTOSOS ==========

            if (!costosos.isEmpty()) {
                Paragraph tituloTop3 = new Paragraph("🏆 TOP 3 PRODUCTOS MÁS COSTOSOS", tituloSeccionFont);
                tituloTop3.setSpacingBefore(20);
                tituloTop3.setSpacingAfter(10);
                documento.add(tituloTop3);

                int posicion = 1;
                for (ItemCanasta item : costosos) {
                    String emoji = posicion == 1 ? "🥇" : posicion == 2 ? "🥈" : "🥉";
                    Font rankFont = FontFactory.getFont(FontFactory.HELVETICA, 11);

                    Paragraph itemTop = new Paragraph(
                            emoji + " " + posicion + ". " + item.getNombre() +
                                    " - " + formatoMoneda.format(item.getPrecio()),
                            rankFont);
                    itemTop.setSpacingAfter(5);
                    itemTop.setIndentationLeft(20);
                    documento.add(itemTop);
                    posicion++;
                }
            }

            // ========== NOTA FINAL ==========

            documento.add(new Paragraph(" "));
            documento.add(new Paragraph(" "));

            Font notaFont = FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 9, BaseColor.GRAY);
            Paragraph nota = new Paragraph(
                    "Este documento fue generado automáticamente por el sistema de Canasta Familiar. " +
                            "Los precios y cantidades reflejan la información ingresada por el usuario.",
                    notaFont);
            nota.setAlignment(Element.ALIGN_JUSTIFIED);
            nota.setSpacingBefore(30);
            documento.add(nota);

            documento.close();
            return baos.toByteArray();

        } catch (Exception e) {
            e.printStackTrace();
            return new byte[0];
        }
    }

    // Método auxiliar para crear celdas de estadísticas
    private PdfPCell crearCeldaEstadistica(String titulo, String valor, BaseColor color) throws DocumentException {
        PdfPTable miniTabla = new PdfPTable(1);
        miniTabla.setWidthPercentage(100);

        // Título
        PdfPCell celdaTitulo = new PdfPCell(new Phrase(titulo,
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, BaseColor.WHITE)));
        celdaTitulo.setBackgroundColor(color);
        celdaTitulo.setHorizontalAlignment(Element.ALIGN_CENTER);
        celdaTitulo.setPadding(8);
        celdaTitulo.setBorder(0);
        miniTabla.addCell(celdaTitulo);

        // Valor
        PdfPCell celdaValor = new PdfPCell(new Phrase(valor,
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, color)));
        celdaValor.setHorizontalAlignment(Element.ALIGN_CENTER);
        celdaValor.setPadding(10);
        celdaValor.setBorder(0);
        miniTabla.addCell(celdaValor);

        PdfPCell contenedor = new PdfPCell(miniTabla);
        contenedor.setPadding(5);
        contenedor.setBorderColor(color);
        contenedor.setBorderWidth(2);

        return contenedor;
    }

    // Método auxiliar para crear celdas de datos
    private PdfPCell crearCeldaDatos(String texto, Font font, BaseColor colorFondo, int alineacion) {
        PdfPCell celda = new PdfPCell(new Phrase(texto, font));
        celda.setBackgroundColor(colorFondo);
        celda.setHorizontalAlignment(alineacion);
        celda.setPadding(6);
        celda.setBorderWidth(0);
        return celda;
    }
}