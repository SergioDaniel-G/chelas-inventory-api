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
import com.micheladas.chelas.entidad.Caguama;

public class CaguamaExportarPdf {

	private List<Caguama> listarTodasLaVentas;

	public CaguamaExportarPdf(List<Caguama> listarTodasLaVentas) {
		super();
		this.listarTodasLaVentas = listarTodasLaVentas;
	}

	private void escribirCabeceraDeLaTabla(PdfPTable tabla) {
		PdfPCell celda = new PdfPCell();

		celda.setBackgroundColor(Color.GRAY);
		celda.setPadding(5);

		Font fuente = FontFactory.getFont("Arial");
		fuente.setColor(Color.WHITE);

		celda.setPhrase(new Phrase("Id", fuente));
		tabla.addCell(celda);

		celda.setPhrase(new Phrase("Marca", fuente));
		tabla.addCell(celda);

		celda.setPhrase(new Phrase("Precio", fuente));
		tabla.addCell(celda);

		celda.setPhrase(new Phrase("Cantidad", fuente));
		tabla.addCell(celda);

		celda.setPhrase(new Phrase("Total", fuente));
		tabla.addCell(celda);

	}

	private void escribirDatosDeLaTabla(PdfPTable tabla) {
		for (Caguama caguama : listarTodasLaVentas) {
			tabla.addCell(String.valueOf(caguama.getId()));
			tabla.addCell(caguama.getMarca());
			tabla.addCell(caguama.getPrecio());
			tabla.addCell(caguama.getCantidad());
			tabla.addCell(caguama.getTotal());
		}
	}

	public void exportar(HttpServletResponse response) throws DocumentException, IOException {
		Document documento = new Document(PageSize.A4);
		PdfWriter.getInstance(documento, response.getOutputStream());

		documento.open();

		Font fuente = FontFactory.getFont("Arial");
		fuente.setColor(Color.BLACK);
		fuente.setSize(16);

		Paragraph titulo = new Paragraph("COMPRA DE CAGUAMAS", fuente);
		titulo.setAlignment(Paragraph.ALIGN_CENTER);
		documento.add(titulo);

		PdfPTable tabla = new PdfPTable(5);
		tabla.setWidthPercentage(90);
		tabla.setSpacingBefore(15);
		tabla.setWidths(new float[] { 1f, 3f, 2.5f, 2.5f, 2f });
		tabla.setWidthPercentage(90);

		escribirCabeceraDeLaTabla(tabla);
		escribirDatosDeLaTabla(tabla);

		documento.add(tabla);
		documento.close();
	}
}
