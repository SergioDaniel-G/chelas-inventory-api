package com.micheladas.chelas.controller;

import java.util.List;
import java.util.Map;

import com.micheladas.chelas.controllergenericview.ControllerGenericView;
import com.micheladas.chelas.export.SaleExcelExporter;
import com.micheladas.chelas.export.SalePdfExporter;
import com.micheladas.chelas.service.SaleService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.micheladas.chelas.entity.Sale;


@Controller
public class SaleController {

	@Autowired
	private SaleService saleService;

	/**
	 * Retrieves and displays the detailed view of a specific Sale by its ID.
	 */

	@GetMapping("/verve/{id}")
	public String viewBigBottleDetails(@PathVariable(value = "id") Long id, Map<String, Object> model, RedirectAttributes flash) {
		Sale sale = saleService.getSalesById(id);

		return ControllerGenericView.viewDetail(
				sale,
				"venta",
				"see/viewsales",
				"/sales",
				(sale != null ? String.valueOf(sale.getId()) : ""),
				model,
				flash
		);
	}

	/**
	 * Fetches a paginated list of all Sales and renders the list view.
	 */

	@GetMapping("/ventas")
	public String listAllSales(@RequestParam(name = "page", defaultValue = "0") int page,
							   Model model, String keyword) {

		return ControllerGenericView.processView("sales", page, keyword, model,
				saleService::findAll, saleService::findBykeyword);
	}

	/**
	 * Prepares the model and displays the form to register a new Sale.
	 */

	@GetMapping("/ventas/nuevo")
	public String showSalesForm(Model model) {
		return ControllerGenericView.displayForm(
				"venta",
				"sales",
				"newform/new_sales",
				model,
				Sale::new
		);
	}

	/**
	 * Validates and persists a new Sale entry into the database.
	 */

	@PostMapping("/ventas/guardar")
	public String saveSales(
			@Valid @ModelAttribute("venta") Sale sale,
			BindingResult result,
			RedirectAttributes flash,
			Model model
	) {
		return ControllerGenericView.saveEntity(
				sale,
				result,
				flash,
				model,
				"Registro de ventas",
				"newform/new_sales",
				() -> saleService.saveSales(sale),
				"redirect:/ventas/nuevo"
		);
	}

	/**
	 * Retrieves an existing Sale data and displays the edit form.
	 */

	@GetMapping("/ventas/editar/{id}")
	public String showEditForm(@PathVariable Long id, Model model) {
		model.addAttribute("venta", saleService.getSalesById(id));
		model.addAttribute("titulo", "Editar Venta");
		return "edit/edit_sales";
	}

	/**
	 * Processes the update of an existing Sale after validating the input data.
	 */
	@PostMapping("/ventas/{id}")
	public String updteSales(@PathVariable Long id,
							 @Valid @ModelAttribute("venta") Sale sale,
							 BindingResult result,
							 RedirectAttributes flash,
							 Model model) {

		return ControllerGenericView.updateEntity(
				id,
				sale,
				result,
				model,
				flash,
				saleService::getSalesById,
				Sale::updateFrom,
				() -> saleService.saveSales(sale),
				"Editar Venta",
				"edit/edit_sales",
				"/ventas",
				"/ventas/editar/" + id
		);
	}

	/**
	 * Deletes a Sale record from the database. Restricted to ADMIN users.
	 */

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/ventas/{id}")
	public String deleteSales(@PathVariable Long id, RedirectAttributes flash) {
		return ControllerGenericView.executeDelete(id, saleService::deleteSales, "ventas", flash);
	}

	/**
	 * Generates and triggers the download of a PDF report containing all Sales.
	 */

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/exportarPDFve")
	public void salesExport(HttpServletResponse response) {
		ControllerGenericView.processPdfExport(response, "sales",
				saleService::findAllSales,
				(lista, resp) -> {
					try {
						new SalePdfExporter(lista).export(resp);
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				});
	}

	/**
	 * Generates and triggers the download of an Excel spreadsheet with the Sales inventory.
	 */

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/exportarExcelven")
	public void listSalesExcelExport(HttpServletResponse response) {
		List<Sale> sales = saleService.findAllSales();
		ControllerGenericView.exportToExcel(response, "sales", res -> {
			new SaleExcelExporter(sales).export(res);
		});
	}
}