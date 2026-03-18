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
import com.micheladas.chelas.entidad.Prestado;

public class PrestadoExportarPdfpr {

	private List<Prestado> listarTodasLaPrestados;

	public PrestadoExportarPdfpr(List<Prestado> listarTodasLaPrestados) {
		super();
		this.listarTodasLaPrestados = listarTodasLaPrestados;
	}

	private void escribirCabeceraDeLaTabla(PdfPTable tabla) {
		PdfPCell celda = new PdfPCell();

		celda.setBackgroundColor(Color.GRAY);
		celda.setPadding(6);

		Font fuente = FontFactory.getFont("Arial");
		fuente.setColor(Color.WHITE);

		celda.setPhrase(new Phrase("Id", fuente));
		tabla.addCell(celda);

		celda.setPhrase(new Phrase("nombre", fuente));
		tabla.addCell(celda);

		celda.setPhrase(new Phrase("cantidad", fuente));
		tabla.addCell(celda);

		celda.setPhrase(new Phrase("total", fuente));
		tabla.addCell(celda);

		celda.setPhrase(new Phrase("descripcionDelPedido", fuente));
		tabla.addCell(celda);

	}

	private void escribirDatosDeLaTabla(PdfPTable tabla) {
		for (Prestado prestado : listarTodasLaPrestados) {
			tabla.addCell(String.valueOf(prestado.getId()));
			tabla.addCell(prestado.getNombre());
			tabla.addCell(prestado.getCantidad());
			tabla.addCell(prestado.getTotal());
			tabla.addCell(prestado.getDescripcionDelPedido());
		}
	}

	public void exportar(HttpServletResponse response) throws DocumentException, IOException {
		Document documento = new Document(PageSize.A4);
		PdfWriter.getInstance(documento, response.getOutputStream());

		documento.open();

		Font fuente = FontFactory.getFont("Arial");
		fuente.setColor(Color.BLACK);
		fuente.setSize(16);

		Paragraph titulo = new Paragraph("prestados", fuente);
		titulo.setAlignment(Paragraph.ALIGN_CENTER);
		documento.add(titulo);

		PdfPTable tabla = new PdfPTable(5);
		tabla.setWidthPercentage(90);
		tabla.setSpacingBefore(15);
		tabla.setWidths(new float[] { 1f, 2.5f, 2.5f, 1f, 1.5f });
		tabla.setWidthPercentage(90);

		escribirCabeceraDeLaTabla(tabla);
		escribirDatosDeLaTabla(tabla);

		documento.add(tabla);
		documento.close();
	}

}
