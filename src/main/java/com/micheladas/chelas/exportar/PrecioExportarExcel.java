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

import com.micheladas.chelas.entidad.Precio;

public class PrecioExportarExcel {

	private XSSFWorkbook libro;
	private XSSFSheet hoja;

	private List<Precio> listarTodosLosPrecios;

	public PrecioExportarExcel(List<Precio> listarTodosLosPrecios) {
		super();
		this.listarTodosLosPrecios = listarTodosLosPrecios;
		libro = new XSSFWorkbook();
		hoja = libro.createSheet("Precios");
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
		celda.setCellValue("id_producto");
		celda.setCellStyle(estilo);

		celda = fila.createCell(2);
		celda.setCellValue("descripcion");
		celda.setCellStyle(estilo);

		celda = fila.createCell(3);
		celda.setCellValue("existencia");
		celda.setCellStyle(estilo);

		celda = fila.createCell(4);
		celda.setCellValue("precio");
		celda.setCellStyle(estilo);

	}

	private void escribirDatosDeLaTabla() {
		int numeroFilas = 1;

		CellStyle estilo = libro.createCellStyle();
		XSSFFont fuente = libro.createFont();
		fuente.setFontHeight(14);
		estilo.setFont(fuente);

		for (Precio precio : listarTodosLosPrecios) {
			Row fila = hoja.createRow(numeroFilas++);

			Cell celda = fila.createCell(0);
			celda.setCellValue(precio.getId());
			hoja.autoSizeColumn(0);
			celda.setCellStyle(estilo);

			celda = fila.createCell(1);
			celda.setCellValue(precio.getId_producto());
			hoja.autoSizeColumn(1);
			celda.setCellStyle(estilo);

			celda = fila.createCell(2);
			celda.setCellValue(precio.getDescripcion());
			hoja.autoSizeColumn(2);
			celda.setCellStyle(estilo);

			celda = fila.createCell(3);
			celda.setCellValue(precio.getExistencia());
			hoja.autoSizeColumn(3);
			celda.setCellStyle(estilo);

			celda = fila.createCell(4);
			celda.setCellValue(precio.getPrecio_producto());
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
