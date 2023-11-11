package com.micheladas.chelas.exportar;

import java.awt.Color;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.micheladas.chelas.entidad.Precio;

public class PrecioExportarPdf {

	private List<Precio> listarTodosLosPrecios;

	public PrecioExportarPdf(List<Precio> listarTodosLosPrecios) {
		super();
		this.listarTodosLosPrecios = listarTodosLosPrecios;
	}

	private void escribirCabeceraDeLaTabla(PdfPTable tabla) {
		PdfPCell celda = new PdfPCell();

		celda.setBackgroundColor(Color.GRAY);
		celda.setPadding(5);

		Font fuente = FontFactory.getFont("Arial");
		fuente.setColor(Color.WHITE);

		celda.setPhrase(new Phrase("id", fuente));
		tabla.addCell(celda);

		celda.setPhrase(new Phrase("id_producto", fuente));
		tabla.addCell(celda);

		celda.setPhrase(new Phrase("descripcion", fuente));
		tabla.addCell(celda);

		celda.setPhrase(new Phrase("existencia", fuente));
		tabla.addCell(celda);

		celda.setPhrase(new Phrase("precio", fuente));
		tabla.addCell(celda);

	}

	private void escribirDatosDeLaTabla(PdfPTable tabla) {
		for (Precio precio : listarTodosLosPrecios) {
			tabla.addCell(String.valueOf(precio.getId()));
			tabla.addCell(precio.getId_producto());
			tabla.addCell(precio.getDescripcion());
			tabla.addCell(precio.getExistencia());
			tabla.addCell(precio.getPrecio_producto());
		}
	}

	public void exportar(HttpServletResponse response) throws DocumentException, IOException {
		Document documento = new Document(PageSize.A4);
		PdfWriter.getInstance(documento, response.getOutputStream());

		documento.open();

		Font fuente = FontFactory.getFont("Arial");
		fuente.setColor(Color.BLACK);
		fuente.setSize(16);

		Paragraph titulo = new Paragraph("precios", fuente);
		titulo.setAlignment(Paragraph.ALIGN_CENTER);
		documento.add(titulo);

		PdfPTable tabla = new PdfPTable(5);
		tabla.setWidthPercentage(90);
		tabla.setSpacingBefore(15);
		tabla.setWidths(new float[] { 1f, 2.5f, 2.5f, 2.5f, 2.5f });
		tabla.setWidthPercentage(90);

		escribirCabeceraDeLaTabla(tabla);
		escribirDatosDeLaTabla(tabla);

		documento.add(tabla);
		documento.close();
	}

}
