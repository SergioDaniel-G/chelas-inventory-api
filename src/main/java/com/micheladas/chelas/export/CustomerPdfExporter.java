package com.micheladas.chelas.export;

import java.io.IOException;
import java.util.List;

import com.micheladas.chelas.genericcontrollerexporter.GenericPdfExporter;
import jakarta.servlet.http.HttpServletResponse;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.micheladas.chelas.entity.Customer;

/**
 * PDF exporter specialized in Customers inventory reports.
 * Utilizes a generic rendering engine to ensure consistent document structure and styling.
 */

public class CustomerPdfExporter {

	private List<Customer> listAllCustomers;

	public CustomerPdfExporter(List<Customer> listAllCustomers) {
		this.listAllCustomers = listAllCustomers;
	}

	public void export(HttpServletResponse response) throws DocumentException, IOException {

		String[] columns = {"ID", "NUM/CLIENTE", "NOMBRE COMPLETO", "DIRECCIÓN", "CP", "FECHA"};
		float[] width = { 0.5f, 1.5f, 3.3f, 3.5f, 0.7f, 2.1f };

		GenericPdfExporter<Customer> exporter = new GenericPdfExporter<>(
				listAllCustomers,
				"DIRECTORIO GENERAL DE CLIENTES",
				columns,
				width
		);

		exporter.export(response, (table, customer) -> {

			exporter.addSimpleCell(table, String.valueOf(customer.getId()), Element.ALIGN_CENTER);
			exporter.addSimpleCell(table, customer.getCustomerNumber(), Element.ALIGN_CENTER);
			exporter.addSimpleCell(table, customer.getFullName(), Element.ALIGN_LEFT);
			exporter.addSimpleCell(table, customer.getAddress(), Element.ALIGN_LEFT);
			exporter.addSimpleCell(table, customer.getZipCode(), Element.ALIGN_CENTER);

			String date = customer.getCreatedAt() != null ? customer.getCreatedAt().toString() : "N/A";
			exporter.addSimpleCell(table, date, Element.ALIGN_CENTER);
		});
	}
}