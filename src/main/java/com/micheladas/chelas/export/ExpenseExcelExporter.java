package com.micheladas.chelas.export;

import java.io.IOException;
import java.util.List;
import jakarta.servlet.http.HttpServletResponse;
import com.micheladas.chelas.entity.Expense;

/**
 * Excel exporter specialized in Expense data, utilizing a generic engine
 * to handle workbook creation, data type mapping, and currency formatting.
 */
public class ExpenseExcelExporter {

	private List<Expense> listAllExpenses;

	public ExpenseExcelExporter(List<Expense> listAllExpenses) {
		this.listAllExpenses = listAllExpenses;
	}

	public void export(HttpServletResponse response) throws IOException {
		
		String[] columns = {"ID", "ARTÍCULO", "CANTIDAD", "PRECIO", "TOTAL"};
		GenericExcelExporter<Expense> exporter = new GenericExcelExporter<>(
				listAllExpenses,
				"Reporte Gastos",
				columns
		);

		exporter.export(response, (row, expense) -> {
			
			exporter.createCell(row, 0, expense.getId(), exporter.getNormalStyle());
			exporter.createCell(row, 1, expense.getItemName(), exporter.getNormalStyle());
			int quantity = (expense.getQuantity() != null) ? expense.getQuantity() : 0;
			exporter.createCell(row, 2, quantity, exporter.getNormalStyle());
			
			double priceValue = (expense.getUnitPrice() != null) ? expense.getUnitPrice().doubleValue() : 0.0;
			exporter.createCell(row, 3, priceValue, exporter.getCurrencyStyle());
			
			double totalVal = (expense.getTotalAmount() != null) ? expense.getTotalAmount().doubleValue() : 0.0;
			exporter.createCell(row, 4, totalVal, exporter.getCurrencyStyle());
		});
	}
}