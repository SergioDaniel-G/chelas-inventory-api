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

import com.micheladas.chelas.entidad.Caguama;

public class CaguamaExportarExcel {
	
	private XSSFWorkbook libro;
	private XSSFSheet hoja;

	private List<Caguama> listarTodasLaVentas;

	public CaguamaExportarExcel( List<Caguama> listarTodasLaVentas) {
		super();
		this.listarTodasLaVentas = listarTodasLaVentas;
		libro = new XSSFWorkbook();
		hoja = libro.createSheet("Caguamas");
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
		celda.setCellValue("marca");
		celda.setCellStyle(estilo);
		
		celda = fila.createCell(2);
		celda.setCellValue("precio");
		celda.setCellStyle(estilo);
		
		celda = fila.createCell(3);
		celda.setCellValue("cantidad");
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
		
		for(Caguama caguama : listarTodasLaVentas) {
			Row fila = hoja.createRow(numeroFilas ++);
			
			Cell celda = fila.createCell(0);
			celda.setCellValue(caguama.getId());
			hoja.autoSizeColumn(0);
			celda.setCellStyle(estilo);
			
			celda = fila.createCell(1);
			celda.setCellValue(caguama.getMarca());
			hoja.autoSizeColumn(1);
			celda.setCellStyle(estilo);
			
			celda = fila.createCell(2);
			celda.setCellValue(caguama.getPrecio());
			hoja.autoSizeColumn(2);
			celda.setCellStyle(estilo);
			
			celda = fila.createCell(3);
			celda.setCellValue(caguama.getCantidad());
			hoja.autoSizeColumn(3);
			celda.setCellStyle(estilo);
			
			celda = fila.createCell(4);
			celda.setCellValue(caguama.getTotal());
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
