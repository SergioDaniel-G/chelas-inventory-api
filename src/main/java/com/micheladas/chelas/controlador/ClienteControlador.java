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
import com.micheladas.chelas.entidad.Cliente;
import com.micheladas.chelas.exportar.ClienteExportarExcel;
import com.micheladas.chelas.exportar.ClienteExportarPdf;
import com.micheladas.chelas.pagination.PageRender;
import com.micheladas.chelas.servicio.ClienteServicio;

@Controller
public class ClienteControlador {

	// we inject the CaguamasServicio interface//
	@Autowired
	private ClienteServicio servicio;

	// botn detalles num 4//
	@GetMapping("/vercli/{id}")
	public String verDetallesDeLosClientes(@PathVariable(value = "id") Long id, Map<String, Object> modelo,
			RedirectAttributes flash) {
		Cliente cliente = servicio.obtenerClientesPorId(id);
		if (cliente == null) {
			flash.addFlashAttribute("error", "los datos no existe en la base de datos");
			return "redirect:/clientes";
		}

		modelo.put("cliente", cliente);
		modelo.put("titulo", "Detalles del cliente " + cliente.getNombre_completo());
		return "verDetallesClientes";
	}

	// this is for findAll of caguamas 2//
	@GetMapping("/clientes")
	public String listarTodasLosClientes(@RequestParam(name = "page", defaultValue = "0") int page, Model modelo,
			String keyword) {
		Pageable pageRequest = PageRequest.of(page, 20);
		Page<Cliente> cliente = servicio.findAll(pageRequest);
		PageRender<Cliente> pageRender = new PageRender<>("/clientes", cliente);
		modelo.addAttribute("page", pageRender);
		modelo.addAttribute("clientes", cliente);

		if (keyword != null) {
			modelo.addAttribute("clientes", servicio.findBykeyword(keyword));
		} else {
			modelo.addAttribute("clientes", servicio.listarTodasLosClientes());
		}
		return "clientes";
	}

	// this is for list the new form call nuevo_caguamas//
	@GetMapping("/clientes/nuevo")
	public String mostrarFormularioDeClientes(Map<String, Object> modelo) {
		Cliente cliente = new Cliente(); // aqui es el error cuando aparece invalid marca bean
		modelo.put("titulo", "Registro de clientes");
		modelo.put("cliente", cliente);
		return "nuevo_clientes";
	}

	// boton guardar guardarCaguamas num 3//
	@PostMapping("/clientes/guardar")
	public String guardarClientes(@Valid Cliente cliente, BindingResult result, RedirectAttributes flash) {
		if (result.hasErrors()) {
			return "clientes";
		}

		servicio.guardarClientes(cliente);
		flash.addFlashAttribute("success", "información guardada con exito");
		return "redirect:/clientes/nuevo";
	}

	// we generate the button edit num 4//
	@GetMapping("/clientes/editar/{id}")
	public String mostrarFormularioDeEditar(@PathVariable Long id, Model modelo) {
		modelo.addAttribute("cliente", servicio.obtenerClientesPorId(id));
		return "editar_clientes";
	}

	// this is when you stay in the form edit and press the button guardar num 5//
	@PostMapping("/clientes/{id}")
	public String actualizarClientes(@PathVariable Long id, @ModelAttribute("cliente") Cliente cliente, Model modelo,
			RedirectAttributes flash) {
		Cliente clienteExistente = servicio.obtenerClientesPorId(id);
		clienteExistente.setId(id);
		clienteExistente.setId(cliente.getId());
		clienteExistente.setNum_cliente(cliente.getNum_cliente());
		clienteExistente.setNombre_completo(cliente.getNombre_completo());
		clienteExistente.setDireccion(cliente.getDireccion());
		clienteExistente.setCp(cliente.getCp());

		servicio.actualizarClientes(clienteExistente);
		flash.addFlashAttribute("success", "información actualizada con exito");
		return "redirect:/clientes/nuevo";

	}

	// this is for the button delete 6//
	@GetMapping("/clientes/{id}")
	public String eliminarVenta(@PathVariable Long id, RedirectAttributes flash) {
		if (id > 0) {
			servicio.eliminarClientes(id);
			flash.addFlashAttribute("success", "información de cliente eliminado correctamente");
		}
		return "redirect:/clientes";
	}

	// this is for the button export pdf //
	@GetMapping("/exportarPDFcli")
	public void exportarListadoDeClientesEnPDF(HttpServletResponse response) throws DocumentException, IOException {
		response.setContentType("application/pdf");

		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String fechaActual = dateFormatter.format(new Date());

		String cabecera = "Content-Disposition";
		String valor = "attachment; filename=Clientes_" + fechaActual + ".pdf";

		response.setHeader(cabecera, valor);

		List<Cliente> cliente = servicio.listarTodasLosClientes();

		ClienteExportarPdf exportar = new ClienteExportarPdf(cliente);
		exportar.exportar(response);
	}

	// this is for the button export excel//
	@GetMapping("/exportarExcelcli")
	public void exportarListadoDeClientesEnExcel(HttpServletResponse response) throws DocumentException, IOException {
		response.setContentType("application/octet-stream");

		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String fechaActual = dateFormatter.format(new Date());

		String cabecera = "Content-Disposition";
		String valor = "attachment; filename=Clientes_" + fechaActual + ".xlsx";

		response.setHeader(cabecera, valor);

		List<Cliente> cliente = servicio.listarTodasLosClientes();

		ClienteExportarExcel exportar = new ClienteExportarExcel(cliente);
		exportar.exportar(response);
	}
}
