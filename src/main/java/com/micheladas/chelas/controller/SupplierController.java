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
import com.micheladas.chelas.entity.Supplier;
import com.micheladas.chelas.export.SupplierExcelExporterpro;
import com.micheladas.chelas.export.SupplierPdfExporterpro;
import com.micheladas.chelas.service.SupplierService;

@Controller
public class SupplierController {

	@Autowired
	private SupplierService supplierService;

	/**
	 * Retrieves and displays the detailed view of a specific Supplier by its ID.
	 */

	@GetMapping("/verpro/{id}")
	public String viewDetailsSuppliers(@PathVariable(value = "id") Long id, Map<String, Object> model, RedirectAttributes flash) {
		Supplier supplier = supplierService.getProveedoresById(id);

		return ControllerGenericView.viewDetail(
				supplier,
				"proveedor",
				"see/viewsuppliers",
				"/suppliers",
				(supplier != null ? supplier.getName() : ""),
				model,
				flash
		);
	}

	/**
	 * Fetches a paginated list of all Supplier and renders the list view.
	 */

	@GetMapping("/proveedores")
	public String listAllSuppliers(@RequestParam(name = "page", defaultValue = "0") int page,
								   Model model, String keyword) {

		return ControllerGenericView.processView("suppliers", page, keyword, model,
				supplierService::findAll, supplierService::findBykeyword);
	}

	/**
	 * Prepares the model and displays the form to register a new Supplier.
	 */

	@GetMapping("/proveedores/nuevo")
	public String showDetailsForm(Model model) {

		return ControllerGenericView.displayForm(
				"proveedor",
				"suppliers",
				"newform/new_suppliers",
				model,
				Supplier::new
		);
	}

	/**
	 * Validates and persists a new Supplier entry into the database.
	 */

	@PostMapping("/proveedores/guardar")
	public String saveSuppliers(
			@Valid @ModelAttribute("proveedor") Supplier supplier,
			BindingResult result,
			RedirectAttributes flash,
			Model model
	) {
		return ControllerGenericView.saveEntity(
				supplier,
				result,
				flash,
				model,
				"Registro de proveedores",
				"newform/new_suppliers",
				() -> supplierService.saveProveedores(supplier),
				"redirect:/proveedores/nuevo"
		);
	}

	/**
	 * Retrieves an existing Supplier data and displays the edit form.
	 */

	@GetMapping("/proveedores/editar/{id}")
	public String showEditForm(@PathVariable Long id, Model model) {
		model.addAttribute("proveedor", supplierService.getProveedoresById(id));
		model.addAttribute("titulo", "Editar Proveedor");
		return "edit/edit_suppliers";
	}

	/**
	 * Processes the update of an existing Supplier after validating the input data.
	 */

	@PostMapping("/proveedores/{id}")
	public String updateSuppliers(@PathVariable Long id,
								  @Valid @ModelAttribute("proveedor") Supplier supplier,
								  BindingResult result,
								  RedirectAttributes flash,
								  Model model) {

		return ControllerGenericView.updateEntity(
				id,
				supplier,
				result,
				model,
				flash,
				supplierService::getProveedoresById,
				Supplier::updateFrom,
				() -> supplierService.saveProveedores(supplier),
				"Editar Proveedor",
				"edit/edit_suppliers",
				"/proveedores",
				"/proveedores/editar/" + id
		);
	}

	/**
	 * Deletes a Supplier record from the database. Restricted to ADMIN users.
	 */

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/proveedores/{id}")
	public String deleteSuppliers(@PathVariable Long id, RedirectAttributes flash) {
		return ControllerGenericView.executeDelete(id, supplierService::deleteProveedores, "proveedores", flash);
	}

	/**
	 * Generates and triggers the download of a PDF report containing all Suppliers.
	 */

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/exportarPDFpro")
	public void exportSuppliers(HttpServletResponse response) {
		ControllerGenericView.processPdfExport(response, "suppliers",
				supplierService::findAllProveedores,
				(list, resp) -> {
					try {
						new SupplierPdfExporterpro(list).export(resp);
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				});
	}

	/**
	 * Generates and triggers the download of an Excel spreadsheet with the Supplier inventory.
	 */

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/exportarExcelpro")
	public void supplierListExportExcel(HttpServletResponse response) {
		List<Supplier> suppliers = supplierService.findAllProveedores();
		ControllerGenericView.exportToExcel(response, "suppliers", res -> {
			new SupplierExcelExporterpro(suppliers).export(res);
		});
	}
}