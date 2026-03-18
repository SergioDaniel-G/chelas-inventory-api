package com.micheladas.chelas.controlador;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.micheladas.chelas.entidad.Gasto;
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
import com.micheladas.chelas.entidad.Proveedor;
import com.micheladas.chelas.exportar.ProveedorExportarExcelpro;
import com.micheladas.chelas.exportar.ProveedorExportarPdfpro;
import com.micheladas.chelas.pagination.PageRender;
import com.micheladas.chelas.servicio.ProveedorServicio;

@Controller
public class ProveedorControlador {

	@Autowired
	private ProveedorServicio servicio;

	// verDetalles//
	@GetMapping("/verpro/{id}")
	public String verDetallesDeLasProveedores(@PathVariable(value = "id") Long id, Map<String, Object> modelo,
			RedirectAttributes flash) {
		Proveedor proveedor = servicio.obtenerProveedoresPorId(id);
		if (proveedor == null) {
			flash.addFlashAttribute("error", "los datos no existe en la base de datos");
			return "redirect:/proveedores";
		}

		modelo.put("proveedor", proveedor);
		modelo.put("titulo", "Detalles del proveedor" + proveedor.getNombre());
		return "verproveedores";
	}

	// this is for findAll of caguamas 2//
	@GetMapping("/proveedores")
	public String listarTodasLasProveedores(@RequestParam(name = "page", defaultValue = "0") int page, Model modelo,
			String keyword) {
		Pageable pageRequest = PageRequest.of(page, 10);
		Page<Proveedor> proveedor = servicio.findAll(pageRequest);
		PageRender<Proveedor> pageRender = new PageRender<>("/proveedor", proveedor);
		modelo.addAttribute("proveedores", proveedor);
		modelo.addAttribute("page", pageRender);

		if (keyword != null) {
			modelo.addAttribute("proveedores", servicio.findBykeyword(keyword));
		} else {
			modelo.addAttribute("proveedores", servicio.listarTodasLasProveedores());
		}
		return "proveedores";
	}

	// this is for list the new form call nuevo_caguamas//
	@GetMapping("/proveedores/nuevo")
	public String mostrarFormularioDeProveedores(Map<String, Object> modelo) {
		Proveedor proveedor = new Proveedor(); // aqui es el error cuando aparece invalid marca bean
		modelo.put("titulo", "Registro de proveedores");
		modelo.put("proveedor", proveedor);
		return "nuevo_proveedores";
	}

	@PostMapping("/proveedores/guardar")
	public String guardarProveedores(@Valid @ModelAttribute("proveedor") Proveedor proveedor,
									 BindingResult result,
									 RedirectAttributes flash,
									 Model modelo) {
		if (result.hasErrors()) {
			modelo.addAttribute("proveedores", proveedor);
			modelo.addAttribute("titulo", "Registro de proveedores");
			return "nuevo_proveedores";
		}

		servicio.guardarProveedores(proveedor);
		flash.addFlashAttribute("success", "información de proveedores guardada correctamente");
		return "redirect:/proveedores/nuevo";
	}

	// we generate the button edit num 4//
	@GetMapping("/proveedores/editar/{id}")
	public String mostrarFormularioDeEditar(@PathVariable Long id, Model modelo) {
		modelo.addAttribute("proveedor", servicio.obtenerProveedoresPorId(id));
		return "editar_proveedores";
	}

	// this is when you stay in the form edit and press the button guardar num 5//6
	@PostMapping("/proveedores/{id}")
	public String actualizarProveedores(@PathVariable Long id, @ModelAttribute("proveedor") Proveedor proveedor,
			RedirectAttributes flash) {
		Proveedor proveedorExistente = servicio.obtenerProveedoresPorId(id);
		boolean hayCambio = false;

		if (!proveedorExistente.getNombre().equals(proveedor.getNombre())) {
			hayCambio = true;
		}
		if (!proveedorExistente.getTelefono().equals(proveedor.getTelefono())) {
			hayCambio = true;
		}

		if (!proveedorExistente.getUbicacion().equals(proveedor.getUbicacion())) {
			hayCambio = true;
		}

		if (!hayCambio) {
			// No hubo cambios
			flash.addFlashAttribute("info", "No has realizado ningún cambio.");
			return "redirect:/proveedores/editar/" + id;
		}

		// Actualizar los campos
		proveedorExistente.setNombre(proveedor.getNombre());
		proveedorExistente.setTelefono(proveedor.getTelefono());
		proveedorExistente.setUbicacion(proveedor.getUbicacion());

		servicio.actualizarProveedores(proveedorExistente);
		flash.addFlashAttribute("success", "información de proveedores actualizada correctamente");
		return "redirect:/proveedores/nuevo";

	}

	// this is for the button delete//
	@GetMapping("/proveedores/{id}")
	public String eliminarProveedores(@PathVariable Long id, RedirectAttributes flash) {
		if (id > 0) {
			servicio.eliminarProveedores(id);
			flash.addFlashAttribute("success", "información de proveedores eliminada correctamente");
		}
		return "redirect:/proveedores";
	}

	// this is for the button export pdf num 6//
	@GetMapping("/exportarPDFpro")
	public void exportarListadoDeProveedoresEnPDF(HttpServletResponse response) throws DocumentException, IOException {
		response.setContentType("application/pdf");

		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String fechaActual = dateFormatter.format(new Date());

		String cabecera = "Content-Disposition";
		String valor = "attachment; filename=Proveedores_" + fechaActual + ".pdf";

		response.setHeader(cabecera, valor);

		List<Proveedor> proveedor = servicio.listarTodasLasProveedores();

		ProveedorExportarPdfpro exportar = new ProveedorExportarPdfpro(proveedor);
		exportar.exportar(response);
	}

	// this is for the button export excel//
	@GetMapping("/exportarExcelpro")
	public void exportarListadoDeProveedoresEnExcel(HttpServletResponse response)
			throws DocumentException, IOException {
		response.setContentType("application/octet-stream");

		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String fechaActual = dateFormatter.format(new Date());

		String cabecera = "Content-Disposition";
		String valor = "attachment; filename=Proveedores_" + fechaActual + ".xlsx";

		response.setHeader(cabecera, valor);

		List<Proveedor> proveedor = servicio.listarTodasLasProveedores();

		ProveedorExportarExcelpro exportar = new ProveedorExportarExcelpro(proveedor);
		exportar.exportar(response);
	}

}
