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
	 * RETRIEVES AND DISPLAYS THE DETAILED VIEW OF A SPECIFIC SUPPLIER BY ITS ID.
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
	 * FETCHES A PAGINATED LIST OF ALL SUPPLIER AND RENDERS THE LIST VIEW.
	 */

	@GetMapping("/proveedores")
	public String listAllSuppliers(@RequestParam(name = "page", defaultValue = "0") int page,
								   Model model, String keyword) {

		return ControllerGenericView.processView("suppliers", page, keyword, model,
				supplierService::findAll, supplierService::findBykeyword);
	}

	/**
	 * PREPARES THE MODEL AND DISPLAYS THE FORM TO REGISTER A NEW SUPPLIER.
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
	 * VALIDATES AND PERSISTS A NEW SUPPLIER ENTRY INTO THE DATABASE.
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
	 * RETRIEVES AN EXISTING SUPPLIER DATA AND DISPLAYS THE EDIT FORM.
	 */

	@GetMapping("/proveedores/editar/{id}")
	public String showEditForm(@PathVariable Long id, Model model) {
		model.addAttribute("proveedor", supplierService.getProveedoresById(id));
		model.addAttribute("titulo", "Editar Proveedor");
		return "edit/edit_suppliers";
	}

	/**
	 * PROCESSES THE UPDATE OF AN EXISTING SUPPLIER AFTER VALIDATING THE INPUT DATA.
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
	 * DELETES A SUPPLIER RECORD FROM THE DATABASE. RESTRICTED TO ADMIN USERS.
	 */

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/proveedores/{id}")
	public String deleteSuppliers(@PathVariable Long id, RedirectAttributes flash) {
		return ControllerGenericView.executeDelete(id, supplierService::deleteProveedores, "proveedores", flash);
	}

	/**
	 * GENERATES AND TRIGGERS THE DOWNLOAD OF A PDF REPORT CONTAINING ALL SUPPLIERS.
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
	 * GENERATES AND TRIGGERS THE DOWNLOAD OF AN EXCEL SPREADSHEET WITH THE SUPPLIER INVENTORY.
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