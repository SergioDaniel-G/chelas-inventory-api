package com.micheladas.chelas.export;

import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.micheladas.chelas.entity.BigBottle;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

/**
 * PDF exporter specialized in BigBottle inventory reports.
 * Utilizes a generic rendering engine to ensure consistent document structure and styling.
 */
public class BigBottlePdfExporter {

	private List<BigBottle> bigBottleList;
	private final NumberFormat MoneyFormat = NumberFormat.getCurrencyInstance(Locale.US);

	public BigBottlePdfExporter(List<BigBottle> bigBottleList) {
		this.bigBottleList = bigBottleList;
	}

	public void export(HttpServletResponse response) throws DocumentException, IOException {
		String[] columns = {"ID", "MARCA", "PRECIO", "CANT.", "TOTAL", "FECHA"};
		float[] width = { 0.7f, 2.5f, 1.5f, 1.0f, 1.5f, 2.3f };

		GenericPdfExporter<BigBottle> exporter = new GenericPdfExporter<>(bigBottleList, "INVENTARIO DE COMPRA - PRODUCTO: CAGUAMAS", columns, width);

		exporter.export(response, (table, bigBottle) -> {
			exporter.addSimpleCell(table, String.valueOf(bigBottle.getId()), Element.ALIGN_CENTER);
			exporter.addSimpleCell(table, bigBottle.getBrand(), Element.ALIGN_LEFT);
			exporter.addSimpleCell(table, bigBottle.getUnitPrice() != null ? MoneyFormat.format(bigBottle.getUnitPrice()) : "$0.00", Element.ALIGN_CENTER);
			exporter.addSimpleCell(table, String.valueOf(bigBottle.getQuantity()), Element.ALIGN_CENTER);
			exporter.addSimpleCell(table, bigBottle.getTotalAmount() != null ? MoneyFormat.format(bigBottle.getTotalAmount()) : "$0.00", Element.ALIGN_CENTER);
			exporter.addSimpleCell(table, bigBottle.getCreatedAt() != null ? bigBottle.getCreatedAt().toString() : "N/A", Element.ALIGN_CENTER);
		});
	}
}