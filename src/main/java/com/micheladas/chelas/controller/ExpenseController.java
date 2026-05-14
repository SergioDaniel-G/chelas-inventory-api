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
	 * RETRIEVES AND DISPLAYS THE DETAILED VIEW OF A SPECIFIC EXPENSE BY ITS ID.
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
	 * FETCHES A PAGINATED LIST OF ALL EXPENSES AND RENDERS THE LIST VIEW.
	 */

	@GetMapping("/gastos")
	public String list(@RequestParam(name = "page", defaultValue = "0") int page,
					   Model model, String keyword) {
		return ControllerGenericView.processView("expenses", page, keyword, model,
				expenseService::findAll, expenseService::findBykeyword);
	}

	/**
	 * PREPARES THE MODEL AND DISPLAYS THE FORM TO REGISTER A NEW EXPENSE.
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
	 * VALIDATES AND PERSISTS A NEW EXPENSE ENTRY INTO THE DATABASE.
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
	 * RETRIEVES AN EXISTING EXPENSE DATA AND DISPLAYS THE EDIT FORM.
	 */

	@GetMapping("/gastos/editar/{id}")
	public String editForm(@PathVariable Long id, Model model) {
		model.addAttribute("gasto", expenseService.getExpensesById(id));
		model.addAttribute("titulo", "Editar Gasto");
		return "edit/edit_expenses";
	}

	/**
	 * PROCESSES THE UPDATE OF AN EXISTING EXPENSE AFTER VALIDATING THE INPUT DATA.
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
	 * DELETES A EXPENSE RECORD FROM THE DATABASE. RESTRICTED TO ADMIN USERS.
	 */

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/gastos/{id}")
	public String delete(@PathVariable Long id, RedirectAttributes flash) {
		return ControllerGenericView.executeDelete(id, expenseService::deleteExpenses, "gastos", flash);
	}

	/**
	 * GENERATES AND TRIGGERS THE DOWNLOAD OF A PDF REPORT CONTAINING ALL EXPENSES.
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
	 * GENERATES AND TRIGGERS THE DOWNLOAD OF AN EXCEL SPREADSHEET WITH THE EXPENSE INVENTORY.
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