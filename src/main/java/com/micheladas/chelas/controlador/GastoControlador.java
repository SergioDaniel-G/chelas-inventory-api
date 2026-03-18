package com.micheladas.chelas.controlador;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.micheladas.chelas.exportar.GastoExportarExcel;
import com.micheladas.chelas.exportar.GastoExportarPdf;
import com.micheladas.chelas.servicio.GastoServicio;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

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
import com.micheladas.chelas.entidad.Gasto;
import com.micheladas.chelas.pagination.PageRender;

@Controller
public class GastoControlador {

	@Autowired
	private GastoServicio servicio;

	// this is for findAll of caguamas 2 y 1//
	@GetMapping("/gastos")
	public String listarTodosLosGastos(@RequestParam(name = "page", defaultValue = "0") int page, Model modelo,
			String keyword) {
		Pageable pageRequest = PageRequest.of(page, 10);
		Page<Gasto> gasto = servicio.findAll(pageRequest);
		PageRender<Gasto> pageRender = new PageRender<>("/gastos", gasto);
		modelo.addAttribute("gastos", gasto);
		modelo.addAttribute("page", pageRender);

		if (keyword != null) {
			modelo.addAttribute("gastos", servicio.findBykeyword(keyword));
		} else {
			modelo.addAttribute("gastos", gasto);
		}
		return "gastos";
	}

	// botn detalles num 4//
	@GetMapping("/verg/{id}")
	public String verDetallesDeLosGastos(@PathVariable(value = "id") Long id, Map<String, Object> modelo,
			RedirectAttributes flash) {
		Gasto gasto = servicio.obtenerGastosPorId(id);
		if (gasto == null) {
			flash.addFlashAttribute("error", "los datos no existe en la base de datos");
			return "redirect:/gastos";
		}

		modelo.put("gasto", gasto);
		modelo.put("titulo", "Detalles de los gastos " + gasto.getArticulo());
		return "vergastos";
	}

	@GetMapping("/gastos/nuevo")
	public String mostrarFormularioDeGastos(Map<String, Object> modelo) {
		Gasto gasto = new Gasto(); // aqui es el error cuando aparece invalid marca bean
		modelo.put("titulo", "Registro de gastos");
		modelo.put("gasto", gasto);
		return "nuevo_gastos";
	}

	@PostMapping("/gastos/guardar")
	public String guardarGastos(
			@Valid @ModelAttribute("gasto") Gasto gasto,
			BindingResult result,
			RedirectAttributes flash,
			Model modelo) {
		if (result.hasErrors()) {
			modelo.addAttribute("gastos", gasto);
			modelo.addAttribute("titulo", "Registro de gastos");
			return "nuevo_gastos";
		}


		if (gasto.getPrecio() != null && gasto.getCantidad() != null) {
			gasto.setTotal(gasto.getPrecio() * gasto.getCantidad());
		}

		servicio.guardarGastos(gasto);
		flash.addFlashAttribute("success", "información de gastos guardados correctamente");
		return "redirect:/gastos/nuevo";
	}

	// we generate the button edit num 4 este es para actualizar datos//
	@GetMapping("/gastos/editar/{id}")
	public String mostrarFormularioDeEditar(@PathVariable Long id, Model modelo) {
		modelo.addAttribute("gasto", servicio.obtenerGastosPorId(id));
		return "editar_gastos";
	}

	// num 5 este es para actualizar datos tambien
	@PostMapping("/gastos/{id}")
	public String actualizarGastos(@PathVariable Long id, @ModelAttribute("gasto") Gasto gasto, Model modelo,
			RedirectAttributes flash) {
		Gasto gastoExistente = servicio.obtenerGastosPorId(id);

		// Comprobar si hubo algún cambio en los campos editables
		boolean hayCambio = false;

		if (!gastoExistente.getArticulo().equals(gasto.getArticulo())) {
			hayCambio = true;
		}
		if (!gastoExistente.getPrecio().equals(gasto.getPrecio())) {
			hayCambio = true;
		}
		if (!gastoExistente.getCantidad().equals(gasto.getCantidad())) {
			hayCambio = true;
		}
		if (!gastoExistente.getTotal().equals(gasto.getTotal())) {
			hayCambio = true;
		}

		if (!hayCambio) {
			// No hubo cambios
			flash.addFlashAttribute("info", "No has realizado ningún cambio.");
			return "redirect:/cigarros/editar/" + id;
		}

		// Actualizar los campos
		gastoExistente.setArticulo(gasto.getArticulo());
		gastoExistente.setPrecio(gasto.getPrecio());
		gastoExistente.setCantidad(gasto.getCantidad());
		gastoExistente.setTotal(gasto.getTotal());

		// Recalcular total
		gastoExistente.setTotal(gasto.getPrecio() * gasto.getCantidad());

		servicio.actualizarGastos(gastoExistente);
		flash.addFlashAttribute("success", "información de otros gastos actualizada correctamente");
		return "redirect:/gastos/nuevo";

	}

	// this is for the button delete//
	@GetMapping("/gastos/{id}")
	public String eliminarVenta(@PathVariable Long id, RedirectAttributes flash) {
		if (id > 0) {
			servicio.eliminarGastos(id);
			flash.addFlashAttribute("success", "información de otros gastos eliminados correctamente");
		}
		return "redirect:/gastos";
	}

	// this is for the button export pdf num 6//
	@GetMapping("/exportarPDFga")
	public void exportarListadoDeGastosEnPDF(HttpServletResponse response) throws DocumentException, IOException {
		response.setContentType("application/pdf");

		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String fechaActual = dateFormatter.format(new Date());

		String cabecera = "Content-Disposition";
		String valor = "attachment; filename=OtrosGastos_" + fechaActual + ".pdf";

		response.setHeader(cabecera, valor);

		List<Gasto> gasto = servicio.listarTodosLosGastos();

		GastoExportarPdf exportar = new GastoExportarPdf(gasto);
		exportar.exportar(response);
	}

	// this is for the button export excel//
	@GetMapping("/exportarExcelga")
	public void exportarListadoDeGastosEnExcel(HttpServletResponse response) throws DocumentException, IOException {
		response.setContentType("application/octet-stream");

		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String fechaActual = dateFormatter.format(new Date());

		String cabecera = "Content-Disposition";
		String valor = "attachment; filename=OtrosGastos_" + fechaActual + ".xlsx";

		response.setHeader(cabecera, valor);

		List<Gasto> gasto = servicio.listarTodosLosGastos();

		GastoExportarExcel exportar = new GastoExportarExcel(gasto);
		exportar.exportar(response);
	}

}
