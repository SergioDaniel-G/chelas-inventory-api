package com.micheladas.chelas.export;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import com.micheladas.chelas.genericcontrollerexporter.GenericPdfExporter;
import jakarta.servlet.http.HttpServletResponse;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.micheladas.chelas.entity.Sale;

/**
 * PDF exporter specialized in Sales inventory reports.
 * Utilizes a generic rendering engine to ensure consistent document structure and styling.
 */

public class SalePdfExporter {

	private List<Sale> listAllSales;
	private final NumberFormat moneyFormat = NumberFormat.getCurrencyInstance(Locale.US);

	public SalePdfExporter(List<Sale> listAllSales) {
		this.listAllSales = listAllSales;
	}

	public void export(HttpServletResponse response) throws DocumentException, IOException {

		String[] columns = {"ID", "VASOS", "CIG.", "CAG.", "STOCK CAG.", "STOCK CIG.", "TOTAL", "FECHA"};
		float[] width = { 0.6f, 1.0f, 1.0f, 1.0f, 1.5f, 1.5f, 1.5f, 2.0f };

		GenericPdfExporter<Sale> exporter = new GenericPdfExporter<>(
				listAllSales,
				"RESUMEN GENERAL DE VENTAS E INVENTARIO",
				columns,
				width
		);

		exporter.export(response, (table, sale) -> {

			exporter.addSimpleCell(table, String.valueOf(sale.getId()), Element.ALIGN_CENTER);
			exporter.addSimpleCell(table, String.valueOf(sale.getCupsQuantity()), Element.ALIGN_CENTER);
			exporter.addSimpleCell(table, String.valueOf(sale.getCigarettesQuantity()), Element.ALIGN_CENTER);
			exporter.addSimpleCell(table, String.valueOf(sale.getBigBottleQuantity()), Element.ALIGN_CENTER);
			exporter.addSimpleCell(table, String.valueOf(sale.getBigBottleStock()), Element.ALIGN_CENTER);
			exporter.addSimpleCell(table, String.valueOf(sale.getCigarettesStock()), Element.ALIGN_CENTER);
			String total = sale.getTotalAmount() != null ? moneyFormat.format(sale.getTotalAmount()) : "$0.00";
			exporter.addSimpleCell(table, total, Element.ALIGN_CENTER);
			String date = sale.getCreatedAt() != null ? sale.getCreatedAt().toString() : "N/A";
			exporter.addSimpleCell(table, date, Element.ALIGN_CENTER);
		});
	}
}