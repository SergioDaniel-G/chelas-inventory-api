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

/**
 * Controller managing the Customer lifecycle, providing standardized
 * CRUD operations and reporting tools.
 */
@Controller
public class SaleController {

	@Autowired
	private SaleService saleService;

	/** View Detail **/
	@GetMapping("/verve/{id}")
	public String viewBigBottleDetails(@PathVariable(value = "id") Long id, Map<String, Object> model, RedirectAttributes flash) {
		Sale sale = saleService.getSalesById(id);

		return ControllerGenericView.viewDetail(
				sale,
				"venta",
				"see/verventas",
				"/ventas",
				(sale != null ? String.valueOf(sale.getId()) : ""),
				model,
				flash
		);
	}

	/** List **/
	@GetMapping("/ventas")
	public String listAllSales(@RequestParam(name = "page", defaultValue = "0") int page,
							   Model model, String keyword) {

		return ControllerGenericView.processView("ventas", page, keyword, model,
				saleService::findAll, saleService::findBykeyword);
	}

	/** Display New Form **/
	@GetMapping("/ventas/nuevo")
	public String showSalesForm(Model model) {
		return ControllerGenericView.displayForm(
				"venta",
				"ventas",
				"nuevo/nuevo_ventas",
				model,
				Sale::new
		);
	}

	/** Save New**/
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
				"nuevo/nuevo_ventas",
				() -> saleService.saveSales(sale),
				"redirect:/ventas/nuevo"
		);
	}

	/** Edit Form **/
	@GetMapping("/ventas/editar/{id}")
	public String showEditForm(@PathVariable Long id, Model model) {
		model.addAttribute("venta", saleService.getSalesById(id));
		model.addAttribute("titulo", "Editar Venta");
		return "edit/editar_ventas";
	}

	/** Update **/
	@PostMapping("/ventas/{id}")
	public String actualizarVentas(@PathVariable Long id,
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
				"edit/editar_ventas",
				"/ventas",
				"/ventas/editar/" + id
		);
	}

	/** Delete Data **/
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/ventas/{id}")
	public String deleteSales(@PathVariable Long id, RedirectAttributes flash) {
		return ControllerGenericView.executeDelete(id, saleService::deleteSales, "ventas", flash);
	}

	/** PDF Export **/
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/exportarPDFve")
	public void salesExport(HttpServletResponse response) {
		ControllerGenericView.processPdfExport(response, "Ventas",
				saleService::findAllSales,
				(lista, resp) -> {
					try {
						new SalePdfExporter(lista).export(resp);
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				});
	}

	/** Export To Excel **/
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/exportarExcelven")
	public void listSalesExcelExport(HttpServletResponse response) {
		List<Sale> sales = saleService.findAllSales();
		ControllerGenericView.exportToExcel(response, "Ventas", res -> {
			new SaleExcelExporter(sales).export(res);
		});
	}
}