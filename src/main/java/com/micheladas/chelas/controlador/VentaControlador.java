package com.micheladas.chelas.controlador;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.micheladas.chelas.entidad.Gasto;
import com.micheladas.chelas.exportar.VentaExportarExcel;
import com.micheladas.chelas.exportar.VentaExportarPdf;
import com.micheladas.chelas.servicio.VentaServicio;
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
import com.micheladas.chelas.entidad.Venta;
import com.micheladas.chelas.pagination.PageRender;


@Controller
public class VentaControlador {

	@Autowired
	private VentaServicio servicio;

	// vista de verDetalles//
	@GetMapping("/verve/{id}")
	public String verDetallesDeLasVentas(@PathVariable(value = "id") Long id, Map<String, Object> modelo,
			RedirectAttributes flash) {
		Venta venta = servicio.obtenerVentasPorId(id);
		if (venta == null) {
			flash.addFlashAttribute("error", "los datos no existe en la base de datos");
			return "redirect:/ventas";
		}

		modelo.put("venta", venta);
		modelo.put("titulo", "Detalles de las ventas " + venta.getId());
		return "verventas";
	}

	// this is for findAll of caguamas 2//
	@GetMapping("/ventas")
	public String listarTodasLasVentas(@RequestParam(name = "page", defaultValue = "0") int page, Model modelo,
			String keyword) {
		Pageable pageRequest = PageRequest.of(page, 31);
		Page<Venta> venta = servicio.findAll(pageRequest);
		PageRender<Venta> pageRender = new PageRender<>("/ventas", venta);
		modelo.addAttribute("ventas", venta);
		modelo.addAttribute("page", pageRender);

		if (keyword != null) {
			modelo.addAttribute("ventas", servicio.findBykeyword(keyword));
		} else {
			modelo.addAttribute("ventas", servicio.listarTodasLasVentas());
		}

		return "ventas";
	}

	// this is for list the new form call nuevo_caguamas//
	@GetMapping("/ventas/nuevo")
	public String mostrarFormularioDeVentas(Map<String, Object> modelo) {
		Venta venta = new Venta(); // aqui es el error cuando aparece invalid marca bean
		modelo.put("titulo", "Registro de ventas");
		modelo.put("venta", venta);
		return "nuevo_ventas";
	}

	// boton guardar guardarCaguamas num 3//
	@PostMapping("/ventas/guardar")
	public String guardarVentas(@Valid @ModelAttribute("venta") Venta venta,
								BindingResult result,
								RedirectAttributes flash,
								Model modelo) {
		if (result.hasErrors()) {
			modelo.addAttribute("ventas", venta);
			modelo.addAttribute("titulo", "Registro de ventas");
			return "nuevo_ventas";
		}

		servicio.guardarVentas(venta);
		flash.addFlashAttribute("success", "informacion guardada correctamente");
		return "redirect:/ventas/nuevo";
	}

	// we generate the button edit num 4//
	@GetMapping("/ventas/editar/{id}")
	public String mostrarFormularioDeEditar(@PathVariable Long id, Model modelo) {
		modelo.addAttribute("venta", servicio.obtenerVentasPorId(id));
		return "editar_ventas";
	}

	// this is when you stay in the form edit and press the button guardar num 5//6
	@PostMapping("/ventas/{id}")
	public String actualizarVentas(@PathVariable Long id, @ModelAttribute("venta") Venta venta, Model modelo,
			RedirectAttributes flash) {
		Venta ventaExistente = servicio.obtenerVentasPorId(id);

		// Comprobar si hubo algún cambio en los campos editables
		boolean hayCambio = false;

		if (!ventaExistente.getVasos().equals(venta.getVasos())) {
			hayCambio = true;
		}
		if (!ventaExistente.getCigarros().equals(venta.getCigarros())) {
			hayCambio = true;
		}
		if (!ventaExistente.getCaguamas().equals(venta.getCaguamas())) {
			hayCambio = true;
		}
		if (!ventaExistente.getTotal().equals(venta.getTotal())) {
			hayCambio = true;
		}

		if (!ventaExistente.getStock_caguamas().equals(venta.getStock_caguamas())) {
			hayCambio = true;
		}
		if (!ventaExistente.getStock_cigarros().equals(venta.getStock_cigarros())) {
			hayCambio = true;
		}

		if (!hayCambio) {
			// No hubo cambios
			flash.addFlashAttribute("info", "No has realizado ningún cambio.");
			return "redirect:/ventas/editar/" + id;
		}

		// Actualizar los campos
		ventaExistente.setVasos(venta.getVasos());
		ventaExistente.setCigarros(venta.getCigarros());
		ventaExistente.setCaguamas(venta.getCaguamas());
		ventaExistente.setTotal(venta.getTotal());
		ventaExistente.setStock_caguamas(venta.getStock_caguamas());
		ventaExistente.setStock_cigarros(venta.getStock_cigarros());

		servicio.actualizarVentas(ventaExistente);
		flash.addFlashAttribute("success", "información de venta actualizada correctamente");
		return "redirect:/ventas/nuevo";

	}

	// this is for the button delete//
	@GetMapping("/ventas/{id}")
	public String eliminarVenta(@PathVariable Long id, RedirectAttributes flash) {
		if (id > 0) {
			servicio.eliminarVentas(id);
			flash.addFlashAttribute("success", "información de ventas eliminada correctamente");

		}
		return "redirect:/ventas";
	}

	// this is for the button export pdf num 6//
	@GetMapping("/exportarPDFve")
	public void exportarListadoDeVentasEnPDF(HttpServletResponse response) throws DocumentException, IOException {
		response.setContentType("application/pdf");

		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String fechaActual = dateFormatter.format(new Date());

		String cabecera = "Content-Disposition";
		String valor = "attachment; filename=Ventas_" + fechaActual + ".pdf";

		response.setHeader(cabecera, valor);

		List<Venta> venta = servicio.listarTodasLasVentas();

		VentaExportarPdf exportar = new VentaExportarPdf(venta);
		exportar.exportar(response);
	}

	// this is for the button export excel//
	@GetMapping("/exportarExcelven")
	public void exportarListadoDeVentasEnExcel(HttpServletResponse response) throws DocumentException, IOException {
		response.setContentType("application/octet-stream");

		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String fechaActual = dateFormatter.format(new Date());

		String cabecera = "Content-Disposition";
		String valor = "attachment; filename=Ventas_" + fechaActual + ".xlsx";

		response.setHeader(cabecera, valor);

		List<Venta> venta = servicio.listarTodasLasVentas();

		VentaExportarExcel exportar = new VentaExportarExcel(venta);
		exportar.exportar(response);
	}

}
