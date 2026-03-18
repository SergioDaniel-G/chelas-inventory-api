
package com.micheladas.chelas.controlador;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
import com.micheladas.chelas.entidad.Caguama;
import com.micheladas.chelas.exportar.CaguamaExportarExcel;
import com.micheladas.chelas.exportar.CaguamaExportarPdf;
import com.micheladas.chelas.pagination.PageRender;
import com.micheladas.chelas.servicio.CaguamaServicio;

@Controller
public class CaguamaControlador {

	@Autowired
	private CaguamaServicio servicio;


	@GetMapping("/ver/{id}")
	public String verDetallesDeLasCaguamas(@PathVariable(value = "id") Long id, Map<String, Object> modelo,
			RedirectAttributes flash) {
		Caguama caguama = servicio.obtenerCaguamasPorId(id);
		if (caguama == null) {
			flash.addFlashAttribute("error", "los datos no existe en la base de datos");
			return "redirect:/caguamas";
		}

		modelo.put("caguama", caguama);
		modelo.put("titulo", "Detalles del empleado " + caguama.getMarca());
		return "vercaguamas";
	}


	@GetMapping("/caguamas")
	public String listarTodasLasCaguamas(@RequestParam(name = "page", defaultValue = "0") int page, Model modelo) {
		Pageable pageRequest = PageRequest.of(page, 20);
		Page<Caguama> caguama = servicio.findAll(pageRequest);
		PageRender<Caguama> pageRender = new PageRender<>("/caguamas", caguama);
		modelo.addAttribute("caguamas", caguama);
		modelo.addAttribute("page", pageRender);

		// modelo.addAttribute("caguamas", servicio.listarTodasLasCaguamas());
		return "caguamas";
	}

	// this is for list the new form call nuevo_caguamas//
	@GetMapping("/caguamas/nuevo")
	public String mostrarFormularioDeCaguamas(Model modelo) {
		Caguama caguama = new Caguama(); // aqui es el error cuando aparece invalid marca bean
		modelo.addAttribute("caguama", new Caguama()); // Thymeleaf reconoce este objeto
		modelo.addAttribute("titulo", "Registro de clientes");
		return "nuevo_caguamas";
	}


	@PostMapping("/caguamas/guardar")
	public String guardarCaguamas(
			@Valid @ModelAttribute("caguama") Caguama caguama,
			BindingResult result,
			RedirectAttributes flash,
			Model modelo) {
		if (result.hasErrors()) {
			modelo.addAttribute("caguama", caguama);
			modelo.addAttribute("titulo", "Registro de clientes");
			return "nuevo_caguamas";
		}

		// Calcular el total
		if (caguama.getPrecio() != null && caguama.getCantidad() != null) {
			caguama.setTotal(caguama.getPrecio() * caguama.getCantidad());
		}

		servicio.guardarCaguamas(caguama);
		flash.addFlashAttribute("success", "información de caguama guardada correctamente");
		return "redirect:/caguamas/nuevo";
	}

	// we generate the button edit num 4//
	@GetMapping("/caguamas/editar/{id}")
	public String mostrarFormularioDeEditar(@PathVariable Long id, Model modelo) {
		modelo.addAttribute("caguama", servicio.obtenerCaguamasPorId(id));
		return "editar_caguamas";
	}

	@PostMapping("/caguamas/{id}")
	public String actualizarCaguamas(@PathVariable Long id,
									 @ModelAttribute("caguama") Caguama caguama,
									 RedirectAttributes flash) {

		// Obtener el registro existente
		Caguama caguamaExistente = servicio.obtenerCaguamasPorId(id);

		// Comprobar si hubo algún cambio en los campos editables
		boolean hayCambio = false;

		if (!caguamaExistente.getMarca().equals(caguama.getMarca())) {
			hayCambio = true;
		}
		if (!caguamaExistente.getPrecio().equals(caguama.getPrecio())) {
			hayCambio = true;
		}
		if (!caguamaExistente.getCantidad().equals(caguama.getCantidad())) {
			hayCambio = true;
		}

		if (!hayCambio) {
			// No hubo cambios
			flash.addFlashAttribute("info", "No has realizado ningún cambio.");
			return "redirect:/caguamas/editar/" + id;
		}

		// Actualizar los campos
		caguamaExistente.setMarca(caguama.getMarca());
		caguamaExistente.setPrecio(caguama.getPrecio());
		caguamaExistente.setCantidad(caguama.getCantidad());

		// Recalcular total
		caguamaExistente.setTotal(caguama.getPrecio() * caguama.getCantidad());

		// Guardar cambios
		servicio.actualizarCaguamas(caguamaExistente);

		flash.addFlashAttribute("success", "Información de caguama actualizada correctamente");
		return "redirect:/caguamas";
	}
	// this is for the button delete//
	@GetMapping("/caguamas/eliminar/{id}")
	public String eliminarVenta(@PathVariable Long id, RedirectAttributes flash) {
		if (id > 0) {
			servicio.eliminarCaguamas(id);
			flash.addFlashAttribute("success", "información de caguama eliminada correctamente");
		}
		return "redirect:/caguamas";
	}

	// this is for the button export pdf num 6//
	@GetMapping("/exportarPDF")
	public void exportarListadoDeCaguamasEnPDF(HttpServletResponse response) throws DocumentException, IOException {
		response.setContentType("application/pdf");

		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String fechaActual = dateFormatter.format(new Date());

		String cabecera = "Content-Disposition";
		String valor = "attachment; filename=Caguamas_" + fechaActual + ".pdf";

		response.setHeader(cabecera, valor);

		List<Caguama> caguama = servicio.listarTodasLasCaguamas();

		CaguamaExportarPdf exportar = new CaguamaExportarPdf(caguama);
		exportar.exportar(response);
	}

	@GetMapping("/exportarExcel")
	public void exportarListadoDeCaguamasEnExcel(HttpServletResponse response) throws DocumentException, IOException {
		response.setContentType("application/octet-stream");

		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String fechaActual = dateFormatter.format(new Date());

		String cabecera = "Content-Disposition";
		String valor = "attachment; filename=Caguamas_" + fechaActual + ".xlsx";

		response.setHeader(cabecera, valor);

		List<Caguama> caguama = servicio.listarTodasLasCaguamas();

		CaguamaExportarExcel exportar = new CaguamaExportarExcel(caguama);
		exportar.exportar(response);
	}

}