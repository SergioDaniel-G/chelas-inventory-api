package com.micheladas.chelas.export;

import java.io.IOException;
import java.util.List;

import com.micheladas.chelas.genericcontrollerexporter.GenericExcelExporter;
import jakarta.servlet.http.HttpServletResponse;
import com.micheladas.chelas.entity.Cigarette;

/**
 * Excel exporter specialized in Cigarette data, utilizing a generic engine
 * to handle workbook creation, data type mapping, and currency formatting.
 */

public class CigaretteExcelExporter {

	private List<Cigarette> listAllCigarettes;

	public CigaretteExcelExporter(List<Cigarette> listAllCigarettes) {
		this.listAllCigarettes = listAllCigarettes;
	}

	public void exportar(HttpServletResponse response) throws IOException {

		String[] columns = {"ID", "MARCA", "PRECIO UNITARIO", "CANTIDAD", "TOTAL VENTA"};

		GenericExcelExporter<Cigarette> exporter = new GenericExcelExporter<>(
				listAllCigarettes,
				"Reporte Cigarros",
				columns
		);

		exporter.export(response, (row, cigarette) -> {

			exporter.createCell(row, 0, cigarette.getId(), exporter.getNormalStyle());
			exporter.createCell(row, 1, cigarette.getBrand(), exporter.getNormalStyle());
			double priceValue = (cigarette.getUnitPrice() != null) ? cigarette.getUnitPrice().doubleValue() : 0.0;
			exporter.createCell(row, 2, priceValue, exporter.getCurrencyStyle());

			int quantity = (cigarette.getQuantity() != null) ? cigarette.getQuantity() : 0;
			exporter.createCell(row, 3, quantity, exporter.getNormalStyle());

			double totalVal = (cigarette.getTotalAmount() != null) ? cigarette.getTotalAmount().doubleValue() : 0.0;
			exporter.createCell(row, 4, totalVal, exporter.getCurrencyStyle());
		});
	}
}