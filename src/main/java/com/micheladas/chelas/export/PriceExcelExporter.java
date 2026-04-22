package com.micheladas.chelas.export;

import java.io.IOException;
import java.util.List;
import jakarta.servlet.http.HttpServletResponse;
import com.micheladas.chelas.entity.Price;

/**
 * Excel exporter specialized in Price data, utilizing a generic engine
 * to handle workbook creation, data type mapping, and currency formatting.
 */
public class PriceExcelExporter {

	private List<Price> listAllPrices;

	public PriceExcelExporter(List<Price> listAllPrices) {
		this.listAllPrices = listAllPrices;
	}

	public void exportar(HttpServletResponse response) throws IOException {

		String[] columns = {"ID", "PRODUCTO", "DESCRIPCIÓN", "EXISTENCIA", "PRECIO"};

		GenericExcelExporter<Price> exporter = new GenericExcelExporter<>(
				listAllPrices,
				"Lista de Precios",
				columns
		);

		exporter.export(response, (row, price) -> {
			exporter.createCell(row, 0, price.getId(), exporter.getNormalStyle());
			exporter.createCell(row, 1, price.getProductId(), exporter.getNormalStyle());
			exporter.createCell(row, 2, price.getDescription(), exporter.getNormalStyle());
			int stock = (price.getStock() != null) ? price.getStock() : 0;
			exporter.createCell(row, 3, stock, exporter.getNormalStyle());
			double priceValue = (price.getProductPrice() != null) ? price.getProductPrice().doubleValue() : 0.0;
			exporter.createCell(row, 4, priceValue, exporter.getCurrencyStyle());
		});
	}
}