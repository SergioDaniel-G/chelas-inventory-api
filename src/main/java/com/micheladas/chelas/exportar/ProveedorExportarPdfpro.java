package com.micheladas.chelas.exportar;

import java.awt.Color;
import java.io.IOException;
import java.util.List;

import jakarta.servlet.http.HttpServletResponse;

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
import com.micheladas.chelas.entidad.Proveedor;

public class ProveedorExportarPdfpro {

	private List<Proveedor> listarTodasLaProveedores;

	public ProveedorExportarPdfpro(List<Proveedor> listarTodasLaProveedores) {
		super();
		this.listarTodasLaProveedores = listarTodasLaProveedores;
	}

	private void escribirCabeceraDeLaTabla(PdfPTable tabla) {
		PdfPCell celda = new PdfPCell();

		celda.setBackgroundColor(Color.GRAY);
		celda.setPadding(4);

		Font fuente = FontFactory.getFont("Arial");
		fuente.setColor(Color.WHITE);

		celda.setPhrase(new Phrase("id", fuente));
		tabla.addCell(celda);

		celda.setPhrase(new Phrase("nombre", fuente));
		tabla.addCell(celda);

		celda.setPhrase(new Phrase("telefono", fuente));
		tabla.addCell(celda);

		celda.setPhrase(new Phrase("ubicacion", fuente));
		tabla.addCell(celda);

	}

	private void escribirDatosDeLaTabla(PdfPTable tabla) {
		for (Proveedor proveedor : listarTodasLaProveedores) {
			tabla.addCell(String.valueOf(proveedor.getId()));
			tabla.addCell(proveedor.getNombre());
			tabla.addCell(proveedor.getTelefono());
			tabla.addCell(proveedor.getUbicacion());

		}
	}

	public void exportar(HttpServletResponse response) throws DocumentException, IOException {
		Document documento = new Document(PageSize.A4);
		PdfWriter.getInstance(documento, response.getOutputStream());

		documento.open();

		Font fuente = FontFactory.getFont("Arial");
		fuente.setColor(Color.BLACK);
		fuente.setSize(16);

		Paragraph titulo = new Paragraph("proveedores", fuente);
		titulo.setAlignment(Paragraph.ALIGN_CENTER);
		documento.add(titulo);

		PdfPTable tabla = new PdfPTable(4);
		tabla.setWidthPercentage(90);
		tabla.setSpacingBefore(15);
		tabla.setWidths(new float[] { 1f, 1.5f, 2f, 4f });
		tabla.setWidthPercentage(90);

		escribirCabeceraDeLaTabla(tabla);
		escribirDatosDeLaTabla(tabla);

		documento.add(tabla);
		documento.close();
	}

}
