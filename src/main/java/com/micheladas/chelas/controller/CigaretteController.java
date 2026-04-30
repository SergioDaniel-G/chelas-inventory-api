package com.micheladas.chelas.controller;

import java.util.List;
import java.util.Map;
import com.micheladas.chelas.controllergenericview.ControllerGenericView;
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
import com.micheladas.chelas.entity.Cigarette;
import com.micheladas.chelas.export.CigaretteExcelExporter;
import com.micheladas.chelas.export.CigarettePdfExporterci;
import com.micheladas.chelas.service.CigaretteService;

@Controller
public class CigaretteController {

	@Autowired
	private CigaretteService cigaretteService;

	/**
	 * Retrieves and displays the detailed view of a specific Cigarette by its ID.
	 */

	@GetMapping("/verci/{id}")
	public String showDetails(@PathVariable(value = "id") Long id, Map<String, Object> model, RedirectAttributes flash) {
		Cigarette cigarette = cigaretteService.getCigarettesById(id);

		return ControllerGenericView.viewDetail(
				cigarette,
				"cigarro",
				"see/viewcigarettes",
				"/cigarettes",
				(cigarette != null ? cigarette.getBrand() : ""),
				model,
				flash
		);
	}

	/**
	 * Fetches a paginated list of all Cigarettes and renders the list view.
	 */

	@GetMapping("/cigarros")
	public String list(@RequestParam(name = "page", defaultValue = "0") int page,
					   Model model, String keyword) {
		return ControllerGenericView.processView("cigarettes", page, keyword, model,
				cigaretteService::findAll, cigaretteService::findBykeyword);
	}

	/**
	 * Prepares the model and displays the form to register a new Cigarette.
	 */

	@GetMapping("/cigarros/nuevo")
	public String showForm(Model model) {
		return ControllerGenericView.displayForm(
				"cigarro",
				"cigarettes",
				"newform/new_cigarettes",
				model,
				Cigarette::new
		);
	}

	/**
	 * Validates and persists a new Cigarette entry into the database.
	 */

	@PostMapping("/cigarros/guardar")
	public String save(
			@Valid @ModelAttribute("cigarro") Cigarette cigarette,
			BindingResult result,
			RedirectAttributes flash,
			Model model
	) {
		return ControllerGenericView.saveEntity(
				cigarette,
				result,
				flash,
				model,
				"Registro de cigarros",
				"newform/new_cigarettes",
				() -> cigaretteService.saveCigarettes(cigarette),
				"redirect:/cigarros/nuevo"
		);
	}

	/**
	 * Retrieves an existing Cigarette data and displays the edit form.
	 */

	@GetMapping("/cigarros/editar/{id}")
	public String showEditForm(@PathVariable Long id, Model model) {
		model.addAttribute("cigarro", cigaretteService.getCigarettesById(id));
		model.addAttribute("titulo", "Editar Cigarro");
		return "edit/edit_cigarettes";
	}

	/**
	 * Processes the update of an existing Cigarette after validating the input data.
	 */

	@PostMapping("/cigarros/{id}")
	public String update(@PathVariable Long id,
						 @Valid @ModelAttribute("cigarro") Cigarette cigarette,
						 BindingResult result,
						 RedirectAttributes flash,
						 Model model) {

		return ControllerGenericView.updateEntity(
				id,
				cigarette,
				result,
				model,
				flash,
				cigaretteService::getCigarettesById,
				Cigarette::updateFrom,
				() -> cigaretteService.saveCigarettes(cigarette),
				"Editar Cigarro",
				"edit/edit_cigarettes",
				"/cigarros",
				"/cigarros/editar/" + id
		);
	}

	/**
	 * Deletes a Cigarette record from the database. Restricted to ADMIN users.
	 */

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/cigarros/{id}")
	public String delete(@PathVariable Long id, RedirectAttributes flash) {
		return ControllerGenericView.executeDelete(id, cigaretteService::deleteCigarettes, "cigarros", flash);
	}

	/**
	 * Generates and triggers the download of a PDF report containing all BigBottles.
	 */


	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/exportarPDFci")
	public void exportPdf(HttpServletResponse response) {
		ControllerGenericView.processPdfExport(response, "cigarettes",
				cigaretteService::findAllCigarettes,
				(list, resp) -> {
					try {
						new CigarettePdfExporterci(list).export(resp);
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				});
	}

	/**
	 * Generates and triggers the download of an Excel spreadsheet with the Cigarette inventory.
	 */

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/exportarExcelci")
	public void exportToExcel(HttpServletResponse response) {
		List<Cigarette> cigarettes = cigaretteService.findAllCigarettes();
		ControllerGenericView.exportToExcel(response, "Cigarettes", res -> {
			new CigaretteExcelExporter(cigarettes).exportar(res);
		});
	}
}