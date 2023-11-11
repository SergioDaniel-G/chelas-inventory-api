package com.micheladas.chelas.exportar;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.micheladas.chelas.entidad.Gasto;

public class GastoExportarExcel {

	private XSSFWorkbook libro;
	private XSSFSheet hoja;

	private List<Gasto> listarTodosLosGastos;

	public GastoExportarExcel(List<Gasto> listarTodosLosGastos) {
		super();
		this.listarTodosLosGastos = listarTodosLosGastos;
		libro = new XSSFWorkbook();
		hoja = libro.createSheet("Gastos");
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
		celda.setCellValue("articulo");
		celda.setCellStyle(estilo);

		celda = fila.createCell(2);
		celda.setCellValue("cantidad");
		celda.setCellStyle(estilo);

		celda = fila.createCell(3);
		celda.setCellValue("precio");
		celda.setCellStyle(estilo);

		celda = fila.createCell(4);
		celda.setCellValue("total");
		celda.setCellStyle(estilo);

	}

	private void escribirDatosDeLaTabla() {
		int numeroFilas = 1;

		CellStyle estilo = libro.createCellStyle();
		XSSFFont fuente = libro.createFont();
		fuente.setFontHeight(14);
		estilo.setFont(fuente);

		for (Gasto gasto : listarTodosLosGastos) {
			Row fila = hoja.createRow(numeroFilas++);

			Cell celda = fila.createCell(0);
			celda.setCellValue(gasto.getId());
			hoja.autoSizeColumn(0);
			celda.setCellStyle(estilo);

			celda = fila.createCell(1);
			celda.setCellValue(gasto.getArticulo());
			hoja.autoSizeColumn(1);
			celda.setCellStyle(estilo);

			celda = fila.createCell(2);
			celda.setCellValue(gasto.getCantidad());
			hoja.autoSizeColumn(2);
			celda.setCellStyle(estilo);

			celda = fila.createCell(3);
			celda.setCellValue(gasto.getPrecio());
			hoja.autoSizeColumn(3);
			celda.setCellStyle(estilo);

			celda = fila.createCell(4);
			celda.setCellValue(gasto.getTotal());
			hoja.autoSizeColumn(4);
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
