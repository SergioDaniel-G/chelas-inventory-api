package com.micheladas.chelas.controller;

import java.util.List;
import java.util.Map;
import com.micheladas.chelas.controllergenericview.ControllerGenericView;
import com.micheladas.chelas.entity.Price;
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
import com.micheladas.chelas.export.PriceExcelExporter;
import com.micheladas.chelas.export.PricePdfExporter;
import com.micheladas.chelas.service.PriceService;

@Controller
public class PriceController {

	@Autowired
	private PriceService priceService;

	/**
	 * RETRIEVES AND DISPLAYS THE DETAILED VIEW OF A SPECIFIC PRICE BY ITS ID.
	 */

	@GetMapping("/verpre/{id}")
	public String detailsPriceView(@PathVariable(value = "id") Long id, Map<String, Object> model, RedirectAttributes flash) {
		Price price = priceService.getPricesById(id);

		return ControllerGenericView.viewDetail(
				price,
				"precio",
				"see/viewprices",
				"/prices",
				(price != null ? String.valueOf(price.getId()) : ""),
				model,
				flash
		);
	}

	/**
	 * FETCHES A PAGINATED LIST OF ALL PRICE AND RENDERS THE LIST VIEW.
	 */

	@GetMapping("/precios")
	public String listAllPrices(@RequestParam(name = "page", defaultValue = "0") int page,
								Model model, String keyword) {

		return ControllerGenericView.processView("prices", page, keyword, model,
				priceService::findAll, priceService::findBykeyword);
	}

	/**
	 * PREPARES THE MODEL AND DISPLAYS THE FORM TO REGISTER A NEW PRICE.
	 */

	@GetMapping("/precios/nuevo")
	public String showPricesForm(Model model) {

		return ControllerGenericView.displayForm(
				"precio",
				"prices",
				"newform/new_prices",
				model,
				Price::new
		);
	}

	/**
	 * VALIDATES AND PERSISTS A NEW PRICE ENTRY INTO THE DATABASE.
	 */

	@PostMapping("/precios/guardar")
	public String savePrices(
			@Valid @ModelAttribute("precio") Price price,
			BindingResult result,
			RedirectAttributes flash,
			Model model
	) {
		return ControllerGenericView.saveEntity(
				price,
				result,
				flash,
				model,
				"Registro de precios",
				"newform/new_prices",
				() -> priceService.savePrices(price),
				"redirect:/precios/nuevo"
		);
	}

	/**
	 * RETRIEVES AN EXISTING PRICE DATA AND DISPLAYS THE EDIT FORM.
	 */

	@GetMapping("/precios/editar/{id}")
	public String showEditForm(@PathVariable Long id, Model modelo) {
		modelo.addAttribute("precio", priceService.getPricesById(id));
		modelo.addAttribute("titulo", "Editar Precio");
		return "edit/edit_prices";
	}

	/**
	 * PROCESSES THE UPDATE OF AN EXISTING PRICE AFTER VALIDATING THE INPUT DATA.
	 */

	@PostMapping("/precios/{id}")
	public String updatePrice(@PathVariable Long id,
							  @Valid @ModelAttribute("precio") Price price,
							  BindingResult result,
							  RedirectAttributes flash,
							  Model model) {

		return ControllerGenericView.updateEntity(
				id,
				price,
				result,
				model,
				flash,
				priceService::getPricesById,
				Price::updateFrom,
				() -> priceService.savePrices(price),
				"Editar Precio",
				"edit/edit_prices",
				"/precios",
				"/precios/editar/" + id
		);
	}

	/**
	 * DELETES A PRICE RECORD FROM THE DATABASE. RESTRICTED TO ADMIN USERS.
	 */

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/precios/{id}")
	public String deletePrice(@PathVariable Long id, RedirectAttributes flash) {
		return ControllerGenericView.executeDelete(id, priceService::deletePrices, "precios", flash);
	}

	/**
	 * GENERATES AND TRIGGERS THE DOWNLOAD OF A PDF REPORT CONTAINING ALL PRICES.
	 */

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/exportarPDFpre")
	public void listExportPricePdf(HttpServletResponse response) {
		ControllerGenericView.processPdfExport(response, "prices",
				priceService::findAllPrices,
				(list, resp) -> {
					try {
						new PricePdfExporter(list).export(resp);
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				});
	}

	/**
	 * GENERATES AND TRIGGERS THE DOWNLOAD OF AN EXCEL SPREADSHEET WITH THE PRICE INVENTORY.
	 */

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/exportarExcelpre")
	public void listExportPriceExcel(HttpServletResponse response) {
		List<Price> prices = priceService.findAllPrices();
		ControllerGenericView.exportToExcel(response, "prices", res -> {
			new PriceExcelExporter(prices).exportar(res);
		});
	}
}