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
import com.micheladas.chelas.entidad.Cigarro;
import com.micheladas.chelas.exportar.CigarroExportarExcelci;
import com.micheladas.chelas.exportar.CigarroExportarPdfci;
import com.micheladas.chelas.pagination.PageRender;
import com.micheladas.chelas.servicio.CigarroServicio;

@Controller
public class CigarroControlador {

	@Autowired
	private CigarroServicio servicio;

	// botn detalles num 4//
	@GetMapping("/verci/{id}")
	public String verDetallesDeLosCigarros(@PathVariable(value = "id") Long id, Map<String, Object> modelo,
			RedirectAttributes flash) {
		Cigarro cigarro = servicio.obtenerCigarrosPorId(id);
		if (cigarro == null) {
			flash.addFlashAttribute("error", "los datos no existen en la base de datos");
			return "redirect:/cigarros";
		}

		modelo.put("cigarro", cigarro);
		modelo.put("titulo", "Detalles del empleado " + cigarro.getMarca());
		return "verDetallesCigarros";
	}

	// this is for findAll of caguamas 2//
	@GetMapping("/cigarros")
	public String listarLosCigarros(@RequestParam(name = "page", defaultValue = "0") int page, Model modelo,
			String keyword) {
		Pageable pageRequest = PageRequest.of(page, 20);
		Page<Cigarro> cigarro = servicio.findAll(pageRequest);
		PageRender<Cigarro> pageRender = new PageRender<>("/cigarros", cigarro);
		modelo.addAttribute("cigarros", cigarro);
		modelo.addAttribute("page", pageRender);

		if (keyword != null) {
			modelo.addAttribute("cigarros", servicio.findBykeyword(keyword));
		} else {
			modelo.addAttribute("cigarros", servicio.listarTodasLosCigarros());
		}
		return "cigarros";
	}

	@GetMapping("/cigarros/nuevo")
	public String mostrarFormularioDeCigarros(Map<String, Object> modelo) {
		Cigarro cigarro = new Cigarro(); // aqui es el error cuando aparece invalid marca bean
		modelo.put("titulo", "Registro de cigarros");
		modelo.put("cigarro", cigarro);
		return "nuevo_cigarros";
	}

	// boton guardar guardarCaguamas num 3//
	@PostMapping("/cigarros/guardar")
	public String guardarCigarros(@Valid Cigarro cigarro, BindingResult result, RedirectAttributes flash) {
		if (result.hasErrors()) {
			return "cigarros";
		}

		servicio.guardarCigarros(cigarro);
		flash.addFlashAttribute("success", "información guardada con exito");
		return "redirect:/cigarros/nuevo";
	}

	// we generate the button edit num 4//
	@GetMapping("/cigarros/editar/{id}")
	public String mostrarFormularioDeEditar(@PathVariable Long id, Model modelo) {
		modelo.addAttribute("cigarro", servicio.obtenerCigarrosPorId(id));
		return "editar_cigarros";
	}

	@PostMapping("/cigarros/{id}")
	public String actualizarCigarros(@PathVariable Long id, @ModelAttribute("cigarro") Cigarro cigarro,
			RedirectAttributes flash) {
		Cigarro cigarroExistente = servicio.obtenerCigarrosPorId(id);
		cigarroExistente.setId(id);
		cigarroExistente.setId(cigarro.getId());
		cigarroExistente.setTotal(cigarro.getTotal());
		cigarroExistente.setCantidad(cigarro.getCantidad());
		cigarroExistente.setPrecio(cigarro.getPrecio());
		cigarroExistente.setMarca(cigarro.getMarca());

		servicio.actualizarCigarros(cigarroExistente);
		flash.addFlashAttribute("success", "información actualizada con exito");
		return "redirect:/cigarros/nuevo";

	}

	// this is for the button delete 6//
	@GetMapping("/cigarros/{id}")
	public String eliminarCigarros(@PathVariable Long id, RedirectAttributes flash) {
		if (id > 0) {
			servicio.eliminarCigarros(id);
			flash.addFlashAttribute("success", "informacion de cigarros eliminada correctamente");
		}
		return "redirect:/cigarros";
	}

	// this is for the button export pdf//
	@GetMapping("/exportarPDFci")
	public void exportarListadoDeCigarrosEnPDF(HttpServletResponse response) throws DocumentException, IOException {
		response.setContentType("application/pdf");

		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String fechaActual = dateFormatter.format(new Date());

		String cabecera = "Content-Disposition";
		String valor = "attachment; filename=Cigarros_" + fechaActual + ".pdf";

		response.setHeader(cabecera, valor);

		List<Cigarro> cigarro = servicio.listarTodasLosCigarros();

		CigarroExportarPdfci exportar = new CigarroExportarPdfci(cigarro);
		exportar.exportar(response);
	}

	// this is for the button export excel//
	@GetMapping("/exportarExcelci")
	public void exportarListadoDeCigarrosEnExcel(HttpServletResponse response) throws DocumentException, IOException {
		response.setContentType("application/octet-stream");

		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String fechaActual = dateFormatter.format(new Date());

		String cabecera = "Content-Disposition";
		String valor = "attachment; filename=Cigarros_" + fechaActual + ".xlsx";

		response.setHeader(cabecera, valor);

		List<Cigarro> cigarro = servicio.listarTodasLosCigarros();

		CigarroExportarExcelci exportar = new CigarroExportarExcelci(cigarro);
		exportar.exportar(response);
	}

	/*
	 * @GetMapping("/index") public String home() { return "index"; }
	 * 
	 * 
	 * // this is for list all pages of caguamas//
	 * 
	 * @GetMapping("/cigarros") public String listarTodasLosCigarros(Model modelo) {
	 * modelo.addAttribute("cigarros", servicio.listarTodasLosCigarros()); return
	 * "cigarros"; }
	 * 
	 * // this is for list the new form call nuevo_caguamas//
	 * 
	 * @GetMapping("/cigarros/nuevo") public String mostrarFormularioDeVentas(Model
	 * modelo) { Cigarros cigarro = new Cigarros(); // aqui es el error cuando
	 * aparece invalid marca bean modelo.addAttribute("cigarro", cigarro); return
	 * "nuevo_cigarros"; }
	 * 
	 * // we generate the button edit//
	 * 
	 * @GetMapping("/cigarros/editar/{id}") public String
	 * mostrarFormularioDeEditar(@PathVariable Long id, Model modelo) {
	 * modelo.addAttribute("cigarro", servicio.obtenerCigarrosPorId(id)); return
	 * "editar_cigarros"; }
	 * 
	 * //falta el boton guardar//
	 * 
	 * // this is when you stay in the form edit and press the button guardar//
	 * 
	 * @PostMapping("/cigarros/{id}") public String actualizarCigarros(@PathVariable
	 * Long id, @ModelAttribute("cigarro") Cigarros cigarro, Model modelo) {
	 * Cigarros cigarroExistente = servicio.obtenerCigarrosPorId(id);
	 * cigarroExistente.setId(id); cigarroExistente.setId(cigarro.getId());
	 * cigarroExistente.setTotal(cigarro.getTotal());
	 * cigarroExistente.setCantidad(cigarro.getCantidad());
	 * cigarroExistente.setPrecio(cigarro.getPrecio());
	 * cigarroExistente.setMarca(cigarro.getMarca());
	 * 
	 * servicio.actualizarCigarros(cigarroExistente); return "redirect:/cigarros";
	 * 
	 * }
	 * 
	 * // this is for the button delete// now is an example
	 * 
	 * @GetMapping("/cigarros/{id}") public String eliminarCigarros(@PathVariable
	 * (value = "id")Long id,RedirectAttributes flash) { if(id>0) {
	 * servicio.eliminarCigarros(id); flash.addFlashAttribute("exito",
	 * "cliente eliminado correctamente"); } return "redirect:/cigarros"; }
	 * 
	 * // this is for the button export pdf//
	 * 
	 * @GetMapping("/exportar2PDF") public void
	 * exportarListadoDeCigarrosEnPDF(HttpServletResponse response) throws
	 * DocumentException, IOException { response.setContentType("application/pdf");
	 * 
	 * DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
	 * String fechaActual = dateFormatter.format(new Date());
	 * 
	 * String cabecera = "Content-Disposition"; String valor =
	 * "attachment; filename=Cigarros_" + fechaActual + ".pdf";
	 * 
	 * response.setHeader(cabecera, valor);
	 * 
	 * List<Cigarros> cigarro = servicio.listarTodasLosCigarros();
	 * 
	 * CigarrosExportarPdfci exportar = new CigarrosExportarPdfci(cigarro);
	 * exportar.exportar(response); }
	 * 
	 * // this is for the button export excel//
	 * 
	 * @GetMapping("/exportar2Excel") public void
	 * exportarListadoDeCigarrosEnExcel(HttpServletResponse response) throws
	 * DocumentException, IOException {
	 * response.setContentType("application/octet-stream");
	 * 
	 * DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
	 * String fechaActual = dateFormatter.format(new Date());
	 * 
	 * String cabecera = "Content-Disposition"; String valor =
	 * "attachment; filename=Cigarros_" + fechaActual + ".xlsx";
	 * 
	 * response.setHeader(cabecera, valor);
	 * 
	 * List<Cigarros> cigarro = servicio.listarTodasLosCigarros();
	 * 
	 * CigarrosExportarExcelci exportar = new CigarrosExportarExcelci(cigarro);
	 * exportar.exportar(response); }
	 */

}
