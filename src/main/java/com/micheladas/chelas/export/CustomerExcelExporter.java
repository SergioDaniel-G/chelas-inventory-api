package com.micheladas.chelas.export;

import java.io.IOException;
import java.util.List;

import com.micheladas.chelas.genericcontrollerexporter.GenericExcelExporter;
import jakarta.servlet.http.HttpServletResponse;
import com.micheladas.chelas.entity.Customer;

/**
 * Excel exporter specialized in Customer data, utilizing a generic engine
 * to handle workbook creation, data type mapping, and currency formatting.
 */

public class CustomerExcelExporter {

	private List<Customer> listAllCustomers;

	public CustomerExcelExporter(List<Customer> listAllCustomers) {
		this.listAllCustomers = listAllCustomers;
	}

	public void export(HttpServletResponse response) throws IOException {

		String[] columns = {"ID", "NÚM. CLIENTE", "NOMBRE COMPLETO", "DIRECCIÓN", "C.P."};
		GenericExcelExporter<Customer> exporter = new GenericExcelExporter<>(
				listAllCustomers,
				"Reporte Clientes",
				columns
		);

		exporter.export(response, (row, customer) -> {
			exporter.createCell(row, 0, customer.getId(), exporter.getNormalStyle());
			exporter.createCell(row, 1, customer.getCustomerNumber(), exporter.getNormalStyle());
			exporter.createCell(row, 2, customer.getFullName(), exporter.getNormalStyle());
			exporter.createCell(row, 3, customer.getAddress(), exporter.getNormalStyle());
			exporter.createCell(row, 4, customer.getZipCode(), exporter.getNormalStyle());
		});
	}
}