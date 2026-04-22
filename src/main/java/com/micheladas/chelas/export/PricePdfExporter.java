package com.micheladas.chelas.export;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import jakarta.servlet.http.HttpServletResponse;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.micheladas.chelas.entity.Price;

/**
 * PDF exporter specialized in Prices inventory reports.
 * Utilizes a generic rendering engine to ensure consistent document structure and styling.
 */
public class PricePdfExporter {

	private List<Price> listAllPrices;
	private final NumberFormat formatoMoneda = NumberFormat.getCurrencyInstance(Locale.US);

	public PricePdfExporter(List<Price> listAllPrices) {
		this.listAllPrices = listAllPrices;
	}

	public void export(HttpServletResponse response) throws DocumentException, IOException {

		String[] columns = {"ID", "PRODUCTO", "DESCRIPCIÓN", "EXISTENCIA", "PRECIO", "FECHA"};
		float[] width = { 0.7f, 1.5f, 3.3f, 1.2f, 1.5f, 2.0f };

		GenericPdfExporter<Price> exporter = new GenericPdfExporter<>(
				listAllPrices,
				"LISTA GENERAL DE PRECIOS E INVENTARIO",
				columns,
				width
		);

		exporter.export(response, (table, price) -> {
			exporter.addSimpleCell(table, String.valueOf(price.getId()), Element.ALIGN_CENTER);
			exporter.addSimpleCell(table, price.getProductId(), Element.ALIGN_CENTER);
			exporter.addSimpleCell(table, price.getDescription(), Element.ALIGN_LEFT);
			exporter.addSimpleCell(table, String.valueOf(price.getStock()), Element.ALIGN_CENTER);
			String salePrice = price.getProductPrice() != null ? formatoMoneda.format(price.getProductPrice()) : "$0.00";
			exporter.addSimpleCell(table, salePrice, Element.ALIGN_CENTER);
			String date = price.getCreatedAt() != null ? price.getCreatedAt().toString() : "N/A";
			exporter.addSimpleCell(table, date, Element.ALIGN_CENTER);
		});
	}
}