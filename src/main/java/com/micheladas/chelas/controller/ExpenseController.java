package com.micheladas.chelas.controller;

import java.util.List;
import java.util.Map;
import com.micheladas.chelas.controllergenericview.ControllerGenericView;
import com.micheladas.chelas.export.ExpenseExcelExporter;
import com.micheladas.chelas.export.ExpensePdfExporter;
import com.micheladas.chelas.service.ExpenseService;
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
import com.micheladas.chelas.entity.Expense;

@Controller
public class ExpenseController {

	@Autowired
	private ExpenseService expenseService;

	/**
	 * Retrieves and displays the detailed view of a specific Expense by its ID.
	 */


	@GetMapping("/verg/{id}")
	public String showDetails(@PathVariable(value = "id") Long id, Map<String, Object> model, RedirectAttributes flash) {
		Expense expense = expenseService.getExpensesById(id);

		return ControllerGenericView.viewDetail(
				expense,
				"gasto",
				"see/viewexpenses",
				"/expenses",
				(expense != null ? expense.getItemName() : ""),
				model,
				flash
		);
	}

	/**
	 * Fetches a paginated list of all Expenses and renders the list view.
	 */

	@GetMapping("/gastos")
	public String list(@RequestParam(name = "page", defaultValue = "0") int page,
					   Model model, String keyword) {
		return ControllerGenericView.processView("expenses", page, keyword, model,
				expenseService::findAll, expenseService::findBykeyword);
	}

	/**
	 * Prepares the model and displays the form to register a new Expense.
	 */

	@GetMapping("/gastos/nuevo")
	public String expensesForm(Model model) {
		return ControllerGenericView.displayForm(
				"gasto",
				"expenses",
				"newform/new_expenses",
				model,
				Expense::new
		);
	}

	/**
	 * Validates and persists a new Expense entry into the database.
	 */
	@PostMapping("/gastos/guardar")
	public String save(
			@Valid @ModelAttribute("gasto") Expense expense,
			BindingResult result,
			RedirectAttributes flash,
			Model model
	) {
		return ControllerGenericView.saveEntity(
				expense,
				result,
				flash,
				model,
				"Registro de gastos",
				"newform/new_expenses",
				() -> expenseService.saveExpenses(expense),
				"redirect:/gastos/nuevo"
		);
	}

	/**
	 * Retrieves an existing Expense data and displays the edit form.
	 */

	@GetMapping("/gastos/editar/{id}")
	public String editForm(@PathVariable Long id, Model model) {
		model.addAttribute("gasto", expenseService.getExpensesById(id));
		model.addAttribute("titulo", "Editar Gasto");
		return "edit/edit_expenses";
	}

	/**
	 * Processes the update of an existing Expense after validating the input data.
	 */

	@PostMapping("/gastos/{id}")
	public String update(@PathVariable Long id,
						 @Valid @ModelAttribute("gasto") Expense expense,
						 BindingResult result,
						 RedirectAttributes flash,
						 Model model) {

		return ControllerGenericView.updateEntity(
				id,
				expense,
				result,
				model,
				flash,
				expenseService::getExpensesById,
				Expense::updateFrom,
				() -> expenseService.saveExpenses(expense),
				"Editar Gasto",
				"edit/edit_expenses",
				"/gastos",
				"/gastos/editar/" + id
		);
	}

	/**
	 * Deletes a Expense record from the database. Restricted to ADMIN users.
	 */

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/gastos/{id}")
	public String delete(@PathVariable Long id, RedirectAttributes flash) {
		return ControllerGenericView.executeDelete(id, expenseService::deleteExpenses, "gastos", flash);
	}

	/**
	 * Generates and triggers the download of a PDF report containing all Expenses.
	 */

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/exportarPDFga")
	public void exportPdf(HttpServletResponse response) {
		ControllerGenericView.processPdfExport(response, "expenses",
				expenseService::findAllExpenses,
				(lista, resp) -> {
					try {
						new ExpensePdfExporter(lista).export(resp);
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				});
	}

	/**
	 * Generates and triggers the download of an Excel spreadsheet with the Expense inventory.
	 */

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/exportarExcelga")
	public void exportToExcel(HttpServletResponse response) {
		List<Expense> expenses = expenseService.findAllExpenses();
		ControllerGenericView.exportToExcel(response, "expenses", res -> {
			new ExpenseExcelExporter(expenses).export(res);
		});
	}
}