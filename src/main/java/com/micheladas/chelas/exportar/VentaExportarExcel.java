package com.micheladas.chelas.exportar;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.micheladas.chelas.entidad.Venta;

public class VentaExportarExcel {

	private XSSFWorkbook libro;
	private XSSFSheet hoja;

	private List<Venta> listarTodasLaVentas;

	public VentaExportarExcel(List<Venta> listarTodasLaVentas) {
		super();
		this.listarTodasLaVentas = listarTodasLaVentas;
		libro = new XSSFWorkbook();
		hoja = libro.createSheet("Ventas generales");
	}

	private void escribirCabeceraDeTabla() {
		Row fila = hoja.createRow(0);

		CellStyle estilo = libro.createCellStyle();
		XSSFFont fuente = libro.createFont();
		fuente.setBold(true);
		fuente.setFontHeight(14);
		estilo.setFont(fuente);

		Cell celda = fila.createCell(0);
		celda.setCellValue("Id");
		celda.setCellStyle(estilo);

		celda = fila.createCell(1);
		celda.setCellValue("vasos");
		celda.setCellStyle(estilo);

		celda = fila.createCell(2);
		celda.setCellValue("cigarros");
		celda.setCellStyle(estilo);

		celda = fila.createCell(3);
		celda.setCellValue("caguamas");
		celda.setCellStyle(estilo);

		celda = fila.createCell(4);
		celda.setCellValue("stock_caguamas");
		celda.setCellStyle(estilo);

		celda = fila.createCell(5);
		celda.setCellValue("stock_cigarros");
		celda.setCellStyle(estilo);

		celda = fila.createCell(6);
		celda.setCellValue("total");
		celda.setCellStyle(estilo);

	}

	private void escribirDatosDeLaTabla() {
		int numeroFilas = 1;

		CellStyle estilo = libro.createCellStyle();
		XSSFFont fuente = libro.createFont();
		fuente.setFontHeight(14);
		estilo.setFont(fuente);

		for (Venta venta : listarTodasLaVentas) {
			Row fila = hoja.createRow(numeroFilas++);

			Cell celda = fila.createCell(0);
			celda.setCellValue(venta.getId());
			hoja.autoSizeColumn(0);
			celda.setCellStyle(estilo);

			celda = fila.createCell(1);
			celda.setCellValue(venta.getVasos());
			hoja.autoSizeColumn(1);
			celda.setCellStyle(estilo);

			celda = fila.createCell(2);
			celda.setCellValue(venta.getCigarros());
			hoja.autoSizeColumn(2);
			celda.setCellStyle(estilo);

			celda = fila.createCell(3);
			celda.setCellValue(venta.getCaguamas());
			hoja.autoSizeColumn(3);
			celda.setCellStyle(estilo);

			celda = fila.createCell(4);
			celda.setCellValue(venta.getStock_caguamas());
			hoja.autoSizeColumn(4);
			celda.setCellStyle(estilo);

			celda = fila.createCell(5);
			celda.setCellValue(venta.getStock_cigarros());
			hoja.autoSizeColumn(5);
			celda.setCellStyle(estilo);

			celda = fila.createCell(6);
			celda.setCellValue(venta.getTotal());
			hoja.autoSizeColumn(6);
			celda.setCellStyle(estilo);

		}
	}

	public void exportar(HttpServletResponse response) throws IOException {
		escribirCabeceraDeTabla();
		escribirDatosDeLaTabla();

		ServletOutputStream outPutStream = response.getOutputStream();
		libro.write(outPutStream);

		libro.close();
		outPutStream.close();
	}

}
