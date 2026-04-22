package com.micheladas.chelas.export;

import java.io.IOException;
import java.util.List;
import jakarta.servlet.http.HttpServletResponse;
import com.micheladas.chelas.entity.Supplier;

/**
 * Excel exporter specialized in Sales data, utilizing a generic engine
 * to handle workbook creation, data type mapping, and currency formatting.
 */
public class SupplierExcelExporterpro {

	private List<Supplier> listAllSuppliers;

	public SupplierExcelExporterpro(List<Supplier> listAllSuppliers) {
		this.listAllSuppliers = listAllSuppliers;
	}

	public void export(HttpServletResponse response) throws IOException {

		String[] columns = {"ID", "NOMBRE", "TELÉFONO", "UBICACIÓN"};

		GenericExcelExporter<Supplier> exporter = new GenericExcelExporter<>(
				listAllSuppliers,
				"Proveedores",
				columns
		);

		exporter.export(response, (row, supplier) -> {

			exporter.createCell(row, 0, supplier.getId(), exporter.getNormalStyle());
			exporter.createCell(row, 1, supplier.getName(), exporter.getNormalStyle());
			exporter.createCell(row, 2, supplier.getPhone(), exporter.getNormalStyle());
			exporter.createCell(row, 3, supplier.getLocation(), exporter.getNormalStyle());
		});
	}
}