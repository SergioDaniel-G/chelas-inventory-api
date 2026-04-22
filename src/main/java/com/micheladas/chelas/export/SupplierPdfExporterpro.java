package com.micheladas.chelas.export;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.http.HttpServletResponse;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.micheladas.chelas.entity.Supplier;

/**
 * PDF exporter specialized in Suppliers inventory reports.
 * Utilizes a generic rendering engine to ensure consistent document structure and styling.
 */
public class SupplierPdfExporterpro {

	private List<Supplier> listAllSuppliers;

	public SupplierPdfExporterpro(List<Supplier> listAllSuppliers) {
		this.listAllSuppliers = listAllSuppliers;
	}

	public void export(HttpServletResponse response) throws DocumentException, IOException {

		String[] columns = {"ID", "NOMBRE / RAZÓN SOCIAL", "TELÉFONO", "UBICACIÓN / DIRECCIÓN", "FECHA REGISTRO"};
		float[] width = { 0.7f, 2.5f, 1.5f, 4.0f, 1.8f };

		GenericPdfExporter<Supplier> exporter = new GenericPdfExporter<>(
				listAllSuppliers,
				"CATÁLOGO MAESTRO DE PROVEEDORES",
				columns,
				width
		);

		exporter.export(response, (table, supplier) -> {

			exporter.addSimpleCell(table, String.valueOf(supplier.getId()), Element.ALIGN_CENTER);
			exporter.addSimpleCell(table, supplier.getName(), Element.ALIGN_LEFT);
			exporter.addSimpleCell(table, supplier.getPhone(), Element.ALIGN_CENTER);
			exporter.addSimpleCell(table, supplier.getLocation(), Element.ALIGN_LEFT);
			String date = supplier.getCreatedAt() != null ? supplier.getCreatedAt().toString() : "N/A";
			exporter.addSimpleCell(table, date, Element.ALIGN_CENTER);
		});
	}
}