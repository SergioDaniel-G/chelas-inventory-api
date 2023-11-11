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
import com.micheladas.chelas.entidad.Cigarro;

public class CigarroExportarPdfci {

	private List<Cigarro> listarTodasLosCigarros;

	
	public CigarroExportarPdfci(List<Cigarro> listarTodasLosCigarros) {
		super();
		this.listarTodasLosCigarros = listarTodasLosCigarros;
	}

	private void escribirCabeceraDeLaTabla(PdfPTable tabla) {
		PdfPCell celda = new PdfPCell();

		celda.setBackgroundColor(Color.GRAY);
		celda.setPadding(5);

		Font fuente = FontFactory.getFont(FontFactory.HELVETICA);
		fuente.setColor(Color.WHITE);

		celda.setPhrase(new Phrase("id", fuente));
		tabla.addCell(celda);

		celda.setPhrase(new Phrase("marca", fuente));
		tabla.addCell(celda);

		celda.setPhrase(new Phrase("precio", fuente));
		tabla.addCell(celda);

		celda.setPhrase(new Phrase("cantidad", fuente));
		tabla.addCell(celda);

		celda.setPhrase(new Phrase("total", fuente));
		tabla.addCell(celda);
		

	}

	private void escribirDatosDeLaTabla(PdfPTable tabla) {
		for (Cigarro cigarro : listarTodasLosCigarros) {
			tabla.addCell(String.valueOf(cigarro.getId()));
			tabla.addCell(cigarro.getMarca());
			tabla.addCell(cigarro.getPrecio());
			tabla.addCell(cigarro.getCantidad());
			tabla.addCell(cigarro.getTotal());
		}
	}

	public void exportar(HttpServletResponse response) throws DocumentException, IOException {
		Document documento = new Document(PageSize.A4);
		PdfWriter.getInstance(documento, response.getOutputStream());

		documento.open();

		Font fuente = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
		fuente.setColor(Color.BLACK);
		fuente.setSize(14);

		Paragraph titulo = new Paragraph("Lista de compras de cigarros", fuente);
		titulo.setAlignment(Paragraph.ALIGN_CENTER);
		documento.add(titulo);

		PdfPTable tabla = new PdfPTable(5);
		tabla.setWidthPercentage(90);
		tabla.setSpacingBefore(15);
		tabla.setWidths(new float[] { 1f, 3f, 3f, 3f,3f });
		tabla.setWidthPercentage(90);

		escribirCabeceraDeLaTabla(tabla);
		escribirDatosDeLaTabla(tabla);

		documento.add(tabla);
		documento.close();
	}

}
