package com.micheladas.chelas.export;

import java.io.IOException;
import java.util.List;
import jakarta.servlet.http.HttpServletResponse;
import com.micheladas.chelas.entity.Loan;

/**
 * Excel exporter specialized in Loan data, utilizing a generic engine
 * to handle workbook creation, data type mapping, and currency formatting.
 */
public class LoanExcelExporterpr {

	private List<Loan> listAllLoans;

	public LoanExcelExporterpr(List<Loan> listAllLoans) {
		this.listAllLoans = listAllLoans;
	}

	public void export(HttpServletResponse response) throws IOException {

		String[] columns = {"ID", "NOMBRE", "CANTIDAD", "TOTAL", "DESCRIPCIÓN"};

		GenericExcelExporter<Loan> exporter = new GenericExcelExporter<>(
				listAllLoans,
				"Reporte Prestados",
				columns
		);

		exporter.export(response, (row, loan) -> {
			exporter.createCell(row, 0, loan.getId(), exporter.getNormalStyle());
			exporter.createCell(row, 1, loan.getCustomerName(), exporter.getNormalStyle());
			int quantity = (loan.getQuantity() != null) ? loan.getQuantity() : 0;
			exporter.createCell(row, 2, quantity, exporter.getNormalStyle());
			double totalValue = (loan.getTotalAmount() != null) ? loan.getTotalAmount().doubleValue() : 0.0;
			exporter.createCell(row, 3, totalValue, exporter.getCurrencyStyle());
			exporter.createCell(row, 4, loan.getOrderDescription(), exporter.getNormalStyle());
		});
	}
}