package com.micheladas.chelas.export;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import jakarta.servlet.http.HttpServletResponse;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.micheladas.chelas.entity.Loan;

/**
 * PDF exporter specialized in Loans inventory reports.
 * Utilizes a generic rendering engine to ensure consistent document structure and styling.
 */
public class LoanPdfExporterpr {

	private List<Loan> listAllLoans;
	private final NumberFormat moneyFormat = NumberFormat.getCurrencyInstance(Locale.US);

	public LoanPdfExporterpr(List<Loan> listAllLoans) {
		this.listAllLoans = listAllLoans;
	}

	public void export(HttpServletResponse response) throws DocumentException, IOException {

		String[] columns = {"ID", "NOMBRE CLIENTE", "CANT.", "TOTAL DEUDA", "DESCRIPCIÓN PEDIDO", "FECHA"};
		float[] width = { 0.7f, 2.5f, 1.0f, 1.5f, 3.5f, 2.0f };

		GenericPdfExporter<Loan> exporter = new GenericPdfExporter<>(
				listAllLoans,
				"REPORTE DETALLADO DE PRODUCTOS PRESTADOS / CRÉDITOS",
				columns,
				width
		);

		exporter.export(response, (table, loan) -> {
			exporter.addSimpleCell(table, String.valueOf(loan.getId()), Element.ALIGN_CENTER);
			exporter.addSimpleCell(table, loan.getCustomerName(), Element.ALIGN_LEFT);
			exporter.addSimpleCell(table, String.valueOf(loan.getQuantity()), Element.ALIGN_CENTER);
			String total = loan.getTotalAmount() != null ? moneyFormat.format(loan.getTotalAmount()) : "$0.00";
			exporter.addSimpleCell(table, total, Element.ALIGN_CENTER);
			exporter.addSimpleCell(table, loan.getOrderDescription(), Element.ALIGN_LEFT);

			// 6. Fecha (createdAt)
			String fecha = loan.getCreatedAt() != null ? loan.getCreatedAt().toString() : "N/A";
			exporter.addSimpleCell(table, fecha, Element.ALIGN_CENTER);
		});
	}
}