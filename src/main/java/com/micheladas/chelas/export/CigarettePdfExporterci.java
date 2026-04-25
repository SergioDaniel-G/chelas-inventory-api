package com.micheladas.chelas.export;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import com.micheladas.chelas.genericcontrollerexporter.GenericPdfExporter;
import jakarta.servlet.http.HttpServletResponse;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.micheladas.chelas.entity.Cigarette;

/**
 * PDF exporter specialized in Cigarette inventory reports.
 * Utilizes a generic rendering engine to ensure consistent document structure and styling.
 */

public class CigarettePdfExporterci {

	private List<Cigarette> listAllCigarettes;
	private final NumberFormat moneyFormat = NumberFormat.getCurrencyInstance(Locale.US);

	public CigarettePdfExporterci(List<Cigarette> listAllCigarettes) {
		this.listAllCigarettes = listAllCigarettes;
	}

	public void export(HttpServletResponse response) throws DocumentException, IOException {

		String[] columns = {"ID", "MARCA", "PRECIO", "CANTIDAD", "TOTAL", "FECHA"};

		float[] width = { 0.7f, 2.5f, 1.5f, 1.0f, 1.5f, 2.3f };

		GenericPdfExporter<Cigarette> exporter = new GenericPdfExporter<>(
				listAllCigarettes,
				"LISTA DE COMPRAS DE CIGARROS",
				columns,
				width
		);

		exporter.export(response, (table, cigarette) -> {

			exporter.addSimpleCell(table, String.valueOf(cigarette.getId()), Element.ALIGN_CENTER);
			exporter.addSimpleCell(table, cigarette.getBrand(), Element.ALIGN_LEFT);
			String price = cigarette.getUnitPrice() != null ? moneyFormat.format(cigarette.getUnitPrice()) : "$0.00";
			exporter.addSimpleCell(table, price, Element.ALIGN_CENTER);
			exporter.addSimpleCell(table, String.valueOf(cigarette.getQuantity()), Element.ALIGN_CENTER);
			String total = cigarette.getTotalAmount() != null ? moneyFormat.format(cigarette.getTotalAmount()) : "$0.00";
			exporter.addSimpleCell(table, total, Element.ALIGN_CENTER);
			String date = cigarette.getCreatedAt() != null ? cigarette.getCreatedAt().toString() : "N/A";
			exporter.addSimpleCell(table, date, Element.ALIGN_CENTER);
		});
	}
}