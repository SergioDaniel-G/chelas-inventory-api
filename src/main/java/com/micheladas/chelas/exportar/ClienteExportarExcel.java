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

import com.micheladas.chelas.entidad.Cliente;

public class ClienteExportarExcel {
	
	private XSSFWorkbook libro;
	private XSSFSheet hoja;

	private List<Cliente> listarTodasLosClientes;

		public ClienteExportarExcel( List<Cliente> listarTodasLosClientes) {
		super();
		this.listarTodasLosClientes = listarTodasLosClientes;
		libro = new XSSFWorkbook();
		hoja = libro.createSheet("Clientes");
	}

		private void escribirCabeceraDeTabla() {
		Row fila = hoja.createRow(0);
		
		CellStyle estilo = libro.createCellStyle();
		XSSFFont fuente = libro.createFont();
		fuente.setBold(true);
		fuente.setFontHeight(16);
		estilo.setFont(fuente);
		
		Cell celda = fila.createCell(0);
		celda.setCellValue("Id");
		celda.setCellStyle(estilo);
		
		celda = fila.createCell(1);
		celda.setCellValue("num_cliente");
		celda.setCellStyle(estilo);
		
		celda = fila.createCell(2);
		celda.setCellValue("nombre_completo");
		celda.setCellStyle(estilo);
		
		celda = fila.createCell(3);
		celda.setCellValue("direccion");
		celda.setCellStyle(estilo);
		
		celda = fila.createCell(4);
		celda.setCellValue("cp");
		celda.setCellStyle(estilo);
		
	}
	
	private void escribirDatosDeLaTabla() {
		int numeroFilas = 1;
		
		CellStyle estilo = libro.createCellStyle();
		XSSFFont fuente = libro.createFont();
		fuente.setFontHeight(14);
		estilo.setFont(fuente);
		
		for(Cliente cliente : listarTodasLosClientes) {
			Row fila = hoja.createRow(numeroFilas ++);
			
			Cell celda = fila.createCell(0);
			celda.setCellValue(cliente.getId());
			hoja.autoSizeColumn(0);
			celda.setCellStyle(estilo);
			
			celda = fila.createCell(1);
			celda.setCellValue(cliente.getNum_cliente());
			hoja.autoSizeColumn(1);
			celda.setCellStyle(estilo);
			
			celda = fila.createCell(2);
			celda.setCellValue(cliente.getNombre_completo());
			hoja.autoSizeColumn(2);
			celda.setCellStyle(estilo);
			
			celda = fila.createCell(3);
			celda.setCellValue(cliente.getDireccion());
			hoja.autoSizeColumn(3);
			celda.setCellStyle(estilo);
			
			celda = fila.createCell(4);
			celda.setCellValue(cliente.getCp());
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
