package com.micheladas.chelas.controlador;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.micheladas.chelas.entidad.Precio;
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
import com.micheladas.chelas.exportar.PrecioExportarExcel;
import com.micheladas.chelas.exportar.PrecioExportarPdf;
import com.micheladas.chelas.pagination.PageRender;
import com.micheladas.chelas.servicio.PrecioServicio;

@Controller
public class PrecioControlador {

	// we inject the CaguamasServicio interface//
	@Autowired
	private PrecioServicio servicio;

	// botn detalles num 4//1
	@GetMapping("/verpre/{id}")
	public String verDetallesDeLosPrecios(@PathVariable(value = "id") Long id, Map<String, Object> modelo,
			RedirectAttributes flash) {
		Precio precio = servicio.obtenerPreciosPorId(id);
		if (precio == null) {
			flash.addFlashAttribute("error", "los datos no existen en la base de datos");
			return "redirect:/precios";
		}

		modelo.put("precio", precio);
		modelo.put("titulo", "Detalles de los precios " + precio.getId());
		return "verprecios";
	}

	// this is for findAll of caguamas 2//2
	@GetMapping("/precios")
	public String listarTodosLosPrecios(@RequestParam(name = "page", defaultValue = "0") int page, Model modelo,
			String keyword) {
		Pageable pageRequest = PageRequest.of(page, 20);
		Page<Precio> precio = servicio.findAll(pageRequest);
		PageRender<Precio> pageRender = new PageRender<>("/precios", precio);
		modelo.addAttribute("precios", precio);
		modelo.addAttribute("page", pageRender);

		if (keyword != null) {
			modelo.addAttribute("precios", servicio.findBykeyword(keyword));
		} else {
			modelo.addAttribute("precios", servicio.listarTodosLosPrecios());
		}
		return "precios";
	}

	// this is for list the new form call nuevo_caguamas//3
	@GetMapping("/precios/nuevo")
	public String mostrarFormularioDePrecios(Map<String, Object> modelo) {
		Precio precio = new Precio(); // aqui es el error cuando aparece invalid marca bean
		modelo.put("titulo", "Registro de precios");
		modelo.put("precio", precio);
		return "nuevo_precios";
	}

	// boton guardar guardarCaguamas num 3//4
	@PostMapping("/precios/guardar")
	public String guardarPrecios(@Valid @ModelAttribute("precio") Precio precio,
								 BindingResult result,
								 RedirectAttributes flash,
								 Model modelo) {
		if (result.hasErrors()) {
			modelo.addAttribute("precios", precio);
			modelo.addAttribute("titulo", "Registro de precios");
			return "nuevo_precios";
		}

		servicio.guardarPrecios(precio);
		flash.addFlashAttribute("success", "informacion guardada correctamente");
		return "redirect:/precios/nuevo";
	}

	// we generate the button edit num 4//5
	@GetMapping("/precios/editar/{id}")
	public String mostrarFormularioDeEditar(@PathVariable Long id, Model modelo) {
		modelo.addAttribute("precio", servicio.obtenerPreciosPorId(id));
		return "editar_precios";
	}

	// this is when you stay in the form edit and press the button guardar num 5//6
	@PostMapping("/precios/{id}")
	public String actualizarPrecios(@PathVariable Long id, @ModelAttribute("precio") Precio precio, Model modelo,
			RedirectAttributes flash) {
		Precio precioExistente = servicio.obtenerPreciosPorId(id);

		// Comprobar si hubo algún cambio en los campos editables
		boolean hayCambio = false;

		if (!precioExistente.getId_producto().equals(precio.getId_producto())) {
			hayCambio = true;
		}
		if (!precioExistente.getDescripcion().equals(precio.getDescripcion())) {
			hayCambio = true;
		}

		if (!precioExistente.getExistencia().equals(precio.getExistencia())) {
			hayCambio = true;
		}
		if (!precioExistente.getPrecio_producto().equals(precio.getPrecio_producto())) {
			hayCambio = true;
		}

		if (!hayCambio) {
			// No hubo cambios
			flash.addFlashAttribute("info", "No has realizado ningún cambio.");
			return "redirect:/precios/editar/" + id;
		}

		// Actualizar los campos
		precioExistente.setId_producto(precio.getId_producto());
		precioExistente.setDescripcion(precio.getDescripcion());
		precioExistente.setExistencia(precio.getExistencia());
		precio.setPrecio_producto(precio.getPrecio_producto());

		servicio.actualizarPrecios(precioExistente);
		flash.addFlashAttribute("success", "información de precios actualizada correctamente");
		return "redirect:/precios/nuevo";

	}

	// this is for the button delete//7
	@GetMapping("/precios/{id}")
	public String eliminarVenta(@PathVariable Long id, RedirectAttributes flash) {
		if (id > 0) {
			servicio.eliminarPrecios(id);
			flash.addFlashAttribute("success", "información de precios eliminados correctamente");

		}
		return "redirect:/precios";
	}

	// this is for the button export pdf num 6//8
	@GetMapping("/exportarPDFpre")
	public void exportarListadoDePreciosEnPDF(HttpServletResponse response) throws DocumentException, IOException {
		response.setContentType("application/pdf");

		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String fechaActual = dateFormatter.format(new Date());

		String cabecera = "Content-Disposition";
		String valor = "attachment; filename=Precios_" + fechaActual + ".pdf";

		response.setHeader(cabecera, valor);

		List<Precio> precio = servicio.listarTodosLosPrecios();

		PrecioExportarPdf exportar = new PrecioExportarPdf(precio);
		exportar.exportar(response);
	}

	// this is for the button export excel//9
	@GetMapping("/exportarExcelpre")
	public void exportarListadoDePreciosEnExcel(HttpServletResponse response) throws DocumentException, IOException {
		response.setContentType("application/octet-stream");

		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String fechaActual = dateFormatter.format(new Date());

		String cabecera = "Content-Disposition";
		String valor = "attachment; filename=Caguamas_" + fechaActual + ".xlsx";

		response.setHeader(cabecera, valor);

		List<Precio> precio = servicio.listarTodosLosPrecios();

		PrecioExportarExcel exportar = new PrecioExportarExcel(precio);
		exportar.exportar(response);
	}

}
