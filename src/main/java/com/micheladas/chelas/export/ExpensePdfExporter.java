package com.micheladas.chelas.export;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import jakarta.servlet.http.HttpServletResponse;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.micheladas.chelas.entity.Expense;

/**
 * PDF exporter specialized in Expenses inventory reports.
 * Utilizes a generic rendering engine to ensure consistent document structure and styling.
 */
public class ExpensePdfExporter {

	private List<Expense> listAllExpenses;
	private final NumberFormat moneyFormat = NumberFormat.getCurrencyInstance(Locale.US);

	public ExpensePdfExporter(List<Expense> listAllExpenses) {
		this.listAllExpenses = listAllExpenses;
	}

	public void export(HttpServletResponse response) throws DocumentException, IOException {

		String[] columns = {"ID", "ARTÍCULO", "PRECIO UNIT.", "CANT.", "TOTAL", "FECHA"};
		float[] width = { 0.7f, 3.0f, 1.5f, 1.0f, 1.5f, 2.3f };

		GenericPdfExporter<Expense> exporter = new GenericPdfExporter<>(
				listAllExpenses,
				"REPORTE OPERATIVO DE GASTOS",
				columns,
				width
		);

		exporter.export(response, (table, expense) -> {

			exporter.addSimpleCell(table, String.valueOf(expense.getId()), Element.ALIGN_CENTER);
			exporter.addSimpleCell(table, expense.getItemName(), Element.ALIGN_LEFT);
			String price = expense.getUnitPrice() != null ? moneyFormat.format(expense.getUnitPrice()) : "$0.00";
			exporter.addSimpleCell(table, price, Element.ALIGN_CENTER);
			exporter.addSimpleCell(table, String.valueOf(expense.getQuantity()), Element.ALIGN_CENTER);
			String total = expense.getTotalAmount() != null ? moneyFormat.format(expense.getTotalAmount()) : "$0.00";
			exporter.addSimpleCell(table, total, Element.ALIGN_CENTER);
			String date = expense.getCreatedAt() != null ? expense.getCreatedAt().toString() : "N/A";
			exporter.addSimpleCell(table, date, Element.ALIGN_CENTER);
		});
	}
}