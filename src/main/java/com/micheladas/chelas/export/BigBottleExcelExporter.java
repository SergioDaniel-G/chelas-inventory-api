package com.micheladas.chelas.export;

import java.io.IOException;
import java.util.List;

import com.micheladas.chelas.genericcontrollerexporter.GenericExcelExporter;
import jakarta.servlet.http.HttpServletResponse;
import com.micheladas.chelas.entity.BigBottle;

/**
 * Excel exporter specialized in BigBottle data, utilizing a generic engine
 * to handle workbook creation, data type mapping, and currency formatting.
 */

public class BigBottleExcelExporter {

	private List<BigBottle> ListAllSales;

	public BigBottleExcelExporter(List<BigBottle> ListallSales) {
		this.ListAllSales = ListallSales;
	}

	public void export(HttpServletResponse response) throws IOException {
		String[] columns = {"ID", "MARCA", "PRECIO UNITARIO", "CANTIDAD", "TOTAL VENTA"};

		GenericExcelExporter<BigBottle> exporter = new GenericExcelExporter<>(
				ListAllSales,
				"Reporte Caguamas",
				columns
		);

		exporter.export(response, (row, bigBottle) -> {
			exporter.createCell(row, 0, bigBottle.getId(), exporter.getNormalStyle());
			exporter.createCell(row, 1, bigBottle.getBrand(), exporter.getNormalStyle());

			double priceVal = (bigBottle.getUnitPrice() != null) ? bigBottle.getUnitPrice().doubleValue() : 0.0;
			exporter.createCell(row, 2, priceVal, exporter.getCurrencyStyle());

			int quantity = (bigBottle.getQuantity() != null) ? bigBottle.getQuantity() : 0;
			exporter.createCell(row, 3, quantity, exporter.getNormalStyle());

			double totalVal = (bigBottle.getTotalAmount() != null) ? bigBottle.getTotalAmount().doubleValue() : 0.0;
			exporter.createCell(row, 4, totalVal, exporter.getCurrencyStyle());
		});
	}
}