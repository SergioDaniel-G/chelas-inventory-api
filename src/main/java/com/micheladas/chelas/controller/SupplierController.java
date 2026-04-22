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

/**
 * Controller managing the Customer lifecycle, providing standardized
 * CRUD operations and reporting tools.
 */
@Controller
public class SupplierController {

	@Autowired
	private SupplierService supplierService;

	/** View Detail **/
	@GetMapping("/verpro/{id}")
	public String viewDetailsSuppliers(@PathVariable(value = "id") Long id, Map<String, Object> model, RedirectAttributes flash) {
		Supplier supplier = supplierService.getProveedoresById(id);

		return ControllerGenericView.viewDetail(
				supplier,
				"proveedor",
				"see/verproveedores",
				"/proveedores",
				(supplier != null ? supplier.getName() : ""),
				model,
				flash
		);
	}

	/** List **/
	@GetMapping("/proveedores")
	public String listAllSuppliers(@RequestParam(name = "page", defaultValue = "0") int page,
								   Model model, String keyword) {

		return ControllerGenericView.processView("proveedores", page, keyword, model,
				supplierService::findAll, supplierService::findBykeyword);
	}

	/** Display New Form **/
	@GetMapping("/proveedores/nuevo")
	public String showDetailsForm(Model model) {

		return ControllerGenericView.displayForm(
				"proveedor",
				"proveedores",
				"nuevo/nuevo_proveedores",
				model,
				Supplier::new
		);
	}

	/** Save New **/
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
				"nuevo/nuevo_proveedores",
				() -> supplierService.saveProveedores(supplier),
				"redirect:/proveedores/nuevo"
		);
	}

	/** Edit Form **/
	@GetMapping("/proveedores/editar/{id}")
	public String showEditForm(@PathVariable Long id, Model model) {
		model.addAttribute("proveedor", supplierService.getProveedoresById(id));
		model.addAttribute("titulo", "Editar Proveedor");
		return "edit/editar_proveedores";
	}

	/** Update **/
	@PostMapping("/proveedores/{id}")
	public String actualizarProveedores(@PathVariable Long id,
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
				"edit/editar_proveedores",
				"/proveedores",
				"/proveedores/editar/" + id
		);
	}

	/** Delete Data **/
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/proveedores/{id}")
	public String deleteSuppliers(@PathVariable Long id, RedirectAttributes flash) {
		return ControllerGenericView.executeDelete(id, supplierService::deleteProveedores, "proveedores", flash);
	}

	/** PDF Export **/
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/exportarPDFpro")
	public void exportSuppliers(HttpServletResponse response) {
		ControllerGenericView.processPdfExport(response, "Proveedores",
				supplierService::findAllProveedores,
				(list, resp) -> {
					try {
						new SupplierPdfExporterpro(list).export(resp);
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				});
	}

	/** Export To Excel **/
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/exportarExcelpro")
	public void supplierListExportExcel(HttpServletResponse response) {
		List<Supplier> suppliers = supplierService.findAllProveedores();
		ControllerGenericView.exportToExcel(response, "Proveedores", res -> {
			new SupplierExcelExporterpro(suppliers).export(res);
		});
	}
}