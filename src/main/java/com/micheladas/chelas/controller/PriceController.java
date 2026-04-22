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

/**
 * Controller managing the Customer lifecycle, providing standardized
 * CRUD operations and reporting tools.
 */
@Controller
public class PriceController {

	@Autowired
	private PriceService priceService;

	/** View Detail **/
	@GetMapping("/verpre/{id}")
	public String detailsPriceView(@PathVariable(value = "id") Long id, Map<String, Object> model, RedirectAttributes flash) {
		Price price = priceService.getPricesById(id);

		return ControllerGenericView.viewDetail(
				price,
				"precio",
				"see/verprecios",
				"/precios",
				(price != null ? String.valueOf(price.getId()) : ""),
				model,
				flash
		);
	}

	/** List **/
	@GetMapping("/precios")
	public String listAllPrices(@RequestParam(name = "page", defaultValue = "0") int page,
								Model model, String keyword) {

		return ControllerGenericView.processView("precios", page, keyword, model,
				priceService::findAll, priceService::findBykeyword);
	}

	/** Display New Form **/
	@GetMapping("/precios/nuevo")
	public String showPricesForm(Model model) {

		return ControllerGenericView.displayForm(
				"precio",
				"precios",
				"nuevo/nuevo_precios",
				model,
				Price::new
		);
	}

	/** Save New **/
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
				"nuevo/nuevo_precios",
				() -> priceService.savePrices(price),
				"redirect:/precios/nuevo"
		);
	}

	/** Edit Form **/
	@GetMapping("/precios/editar/{id}")
	public String showEditForm(@PathVariable Long id, Model modelo) {
		modelo.addAttribute("precio", priceService.getPricesById(id));
		modelo.addAttribute("titulo", "Editar Precio");
		return "edit/editar_precios";
	}

	/** Update **/
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
				"edit/editar_precios",
				"/precios",
				"/precios/editar/" + id
		);
	}

	/** Delete Data **/
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/precios/{id}")
	public String deletePrice(@PathVariable Long id, RedirectAttributes flash) {
		return ControllerGenericView.executeDelete(id, priceService::deletePrices, "precios", flash);
	}

	/** PDF Export **/
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/exportarPDFpre")
	public void listExportPricePdf(HttpServletResponse response) {
		ControllerGenericView.processPdfExport(response, "Precios",
				priceService::findAllPrices,
				(list, resp) -> {
					try {
						new PricePdfExporter(list).export(resp);
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				});
	}

	/** Export To Excel **/
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/exportarExcelpre")
	public void listExportPriceExcel(HttpServletResponse response) {
		List<Price> prices = priceService.findAllPrices();
		ControllerGenericView.exportToExcel(response, "Precios", res -> {
			new PriceExcelExporter(prices).exportar(res);
		});
	}
}