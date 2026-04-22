package com.micheladas.chelas.export;

import java.io.IOException;
import java.util.List;
import jakarta.servlet.http.HttpServletResponse;
import com.micheladas.chelas.entity.Sale;

/**
 * Excel exporter specialized in Sales  data, utilizing a generic engine
 * to handle workbook creation, data type mapping, and currency formatting.
 */
public class SaleExcelExporter {

	private List<Sale> listAllSales;

	public SaleExcelExporter(List<Sale> listAllSales) {
		this.listAllSales = listAllSales;
	}

	public void export(HttpServletResponse response) throws IOException {

		String[] columns = {"ID", "VASOS", "CIGARROS", "CAGUAMAS", "STOCK CAGUAMAS", "STOCK CIGARROS", "TOTAL"};

		GenericExcelExporter<Sale> exporter = new GenericExcelExporter<>(
				listAllSales,
				"Ventas generales",
				columns
		);

		exporter.export(response, (row, sale) -> {

			exporter.createCell(row, 0, sale.getId(), exporter.getNormalStyle());
			exporter.createCell(row, 1, sale.getCupsQuantity(), exporter.getNormalStyle());
			exporter.createCell(row, 2, sale.getCigarettesQuantity(), exporter.getNormalStyle());
			exporter.createCell(row, 3, sale.getBigBottleQuantity(), exporter.getNormalStyle());
			exporter.createCell(row, 4, sale.getBigBottleStock(), exporter.getNormalStyle());
			exporter.createCell(row, 5, sale.getCigarettesStock(), exporter.getNormalStyle());

			double totalVal = (sale.getTotalAmount() != null) ? sale.getTotalAmount().doubleValue() : 0.0;
			exporter.createCell(row, 6, totalVal, exporter.getCurrencyStyle());
		});
	}
}