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
import com.micheladas.chelas.entidad.Venta;

public class VentaExportarPdf {

	private List<Venta> listarTodasLaVentas;

	public VentaExportarPdf(List<Venta> listarTodasLaVentas) {
		super();
		this.listarTodasLaVentas = listarTodasLaVentas;
	}

	private void escribirCabeceraDeLaTabla(PdfPTable tabla) {
		PdfPCell celda = new PdfPCell();

		celda.setBackgroundColor(Color.GRAY);
		celda.setPadding(7);

		Font fuente = FontFactory.getFont("Arial");
		fuente.setColor(Color.WHITE);

		celda.setPhrase(new Phrase("id", fuente));
		tabla.addCell(celda);

		celda.setPhrase(new Phrase("vasos", fuente));
		tabla.addCell(celda);

		celda.setPhrase(new Phrase("cigarros", fuente));
		tabla.addCell(celda);

		celda.setPhrase(new Phrase("caguamas", fuente));
		tabla.addCell(celda);

		celda.setPhrase(new Phrase("stock_caguamas", fuente));
		tabla.addCell(celda);

		celda.setPhrase(new Phrase("stock_cigarros", fuente));
		tabla.addCell(celda);

		celda.setPhrase(new Phrase("total", fuente));
		tabla.addCell(celda);

	}

	private void escribirDatosDeLaTabla(PdfPTable tabla) {
		for (Venta venta : listarTodasLaVentas) {
			tabla.addCell(String.valueOf(venta.getId()));
			tabla.addCell(venta.getVasos());
			tabla.addCell(venta.getCigarros());
			tabla.addCell(venta.getCaguamas());
			tabla.addCell(venta.getStock_caguamas());
			tabla.addCell(venta.getStock_cigarros());
			tabla.addCell(venta.getTotal());
		}
	}

	public void exportar(HttpServletResponse response) throws DocumentException, IOException {
		Document documento = new Document(PageSize.A4);
		PdfWriter.getInstance(documento, response.getOutputStream());

		documento.open();

		Font fuente = FontFactory.getFont("Arial");
		fuente.setColor(Color.BLACK);
		fuente.setSize(16);

		Paragraph titulo = new Paragraph("ventas en general", fuente);
		titulo.setAlignment(Paragraph.ALIGN_CENTER);
		documento.add(titulo);

		PdfPTable tabla = new PdfPTable(7);
		tabla.setWidthPercentage(100);
		tabla.setSpacingBefore(15);
		tabla.setWidths(new float[] { 1f, 2f, 2f, 2.5f, 4f, 3.5f, 2.5f });
		tabla.setWidthPercentage(100);

		escribirCabeceraDeLaTabla(tabla);
		escribirDatosDeLaTabla(tabla);

		documento.add(tabla);
		documento.close();
	}

}
