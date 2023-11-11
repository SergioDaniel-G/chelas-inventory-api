package com.micheladas.chelas.controlador;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.lowagie.text.DocumentException;
import com.micheladas.chelas.entidad.Prestado;
import com.micheladas.chelas.exportar.PrestadoExportarExcelpr;
import com.micheladas.chelas.exportar.PrestadoExportarPdfpr;
import com.micheladas.chelas.pagination.PageRender;
import com.micheladas.chelas.servicio.PrestadoServicio;

@Controller
public class PrestadoControlador {

	@Autowired
	private PrestadoServicio servicio;

	// botn detalles num 4//
	@GetMapping("/verpr/{id}")
	public String verDetallesDeLoPrestado(@PathVariable(value = "id") Long id, Map<String, Object> modelo,
			RedirectAttributes flash) {
		Prestado prestado = servicio.obtenerPrestadosPorId(id);
		if (prestado == null) {
			flash.addFlashAttribute("error", "los datos no existen en la base de datos");
			return "redirect:/prestados";
		}

		modelo.put("prestado", prestado);
		modelo.put("titulo", "Detalles de lo prestado " + prestado.getId());
		return "verDetallesPrestados";
	}

	// this is for findAll of caguamas 2//
	@GetMapping("/prestados")
	public String listarLosPrestados(@RequestParam(name = "page", defaultValue = "0") int page, Model modelo,
			String keyword) {
		Pageable pageRequest = PageRequest.of(page, 10);
		Page<Prestado> prestado = servicio.findAll(pageRequest);
		PageRender<Prestado> pageRender = new PageRender<>("/prestados", prestado);
		modelo.addAttribute("prestados", prestado);
		modelo.addAttribute("page", pageRender);

		if (keyword != null) {
			modelo.addAttribute("prestados", servicio.findBykeyword(keyword));
		} else {
			modelo.addAttribute("prestados", servicio.listarTodasLasPrestados());
		}
		return "prestados";
	}

	@GetMapping("/prestados/nuevo")
	public String mostrarFormularioDePrestados(Map<String, Object> modelo) {
		Prestado prestado = new Prestado(); // aqui es el error cuando aparece invalid marca bean
		modelo.put("titulo", "Registro de prestados");
		modelo.put("prestado", prestado);
		return "nuevo_prestados";
	}

	// boton guardar guardarCaguamas num 3//
	@PostMapping("/prestados/guardar")
	public String guardarPrestados(@Valid Prestado prestado, BindingResult result, RedirectAttributes flash) {
		if (result.hasErrors()) {
			return "prestados";
		}

		servicio.guardarPrestados(prestado);
		flash.addFlashAttribute("success", "información guardada con exito");
		return "redirect:/prestados/nuevo";
	}

	// we generate the button edit num 4//
	@GetMapping("/prestados/editar/{id}")
	public String mostrarFormularioDeEditar(@PathVariable Long id, Model modelo) {
		modelo.addAttribute("prestado", servicio.obtenerPrestadosPorId(id));
		return "editar_prestados";
	}

	@PostMapping("/prestados/{id}")
	public String actualizarPrestados(@PathVariable Long id, @ModelAttribute("prestado") Prestado prestado,
			RedirectAttributes flash) {
		Prestado prestadoExistente = servicio.obtenerPrestadosPorId(id);
		prestadoExistente.setId(id);
		prestadoExistente.setId(prestado.getId());
		prestadoExistente.setNombre(prestado.getNombre());
		prestadoExistente.setCantidad(prestado.getCantidad());
		prestadoExistente.setTotal(prestado.getTotal());
		prestadoExistente.setDescripcionDelPedido(prestado.getDescripcionDelPedido());

		servicio.actualizarPrestados(prestadoExistente);
		flash.addFlashAttribute("success", "información actualizada con exito");
		return "redirect:/prestados/nuevo";

	}

	// this is for the button delete 6//
	@GetMapping("/prestados/{id}")
	public String eliminarPrestados(@PathVariable Long id, RedirectAttributes flash) {
		if (id > 0) {
			servicio.eliminarPrestados(id);
			flash.addFlashAttribute("success", "informacion de prestados eliminada correctamente");
		}
		return "redirect:/prestados";
	}

	// this is for the button export pdf//
	@GetMapping("/exportarPDFpr")
	public void exportarListadoDePrestadosEnPDF(HttpServletResponse response) throws DocumentException, IOException {
		response.setContentType("application/pdf");

		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String fechaActual = dateFormatter.format(new Date());

		String cabecera = "Content-Disposition";
		String valor = "attachment; filename=Prestado_" + fechaActual + ".pdf";

		response.setHeader(cabecera, valor);

		List<Prestado> prestado = servicio.listarTodasLasPrestados();

		PrestadoExportarPdfpr exportar = new PrestadoExportarPdfpr(prestado);
		exportar.exportar(response);
	}

	// this is for the button export excel//
	@GetMapping("/exportarExcelpr")
	public void exportarListadoDePrestadosEnExcel(HttpServletResponse response) throws DocumentException, IOException {
		response.setContentType("application/octet-stream");

		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String fechaActual = dateFormatter.format(new Date());

		String cabecera = "Content-Disposition";
		String valor = "attachment; filename=Prestados_" + fechaActual + ".xlsx";

		response.setHeader(cabecera, valor);

		List<Prestado> prestado = servicio.listarTodasLasPrestados();

		PrestadoExportarExcelpr exportar = new PrestadoExportarExcelpr(prestado);
		exportar.exportar(response);
	}
}
