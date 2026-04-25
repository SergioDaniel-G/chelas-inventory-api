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
import com.micheladas.chelas.entity.Loan;
import com.micheladas.chelas.export.LoanExcelExporterpr;
import com.micheladas.chelas.export.LoanPdfExporterpr;
import com.micheladas.chelas.service.LoanService;

@Controller
public class LoansController {

    @Autowired
    private LoanService loanService;

    /**
     * Retrieves and displays the detailed view of a specific Loan by its ID.
     */

    @GetMapping("/verpr/{id}")
    public String showDetails(@PathVariable(value = "id") Long id, Map<String, Object> model, RedirectAttributes flash) {

        Loan loan = loanService.getLoansById(id);

        return ControllerGenericView.viewDetail(
                loan,
                "prestado",
                "see/viewloans",
                "/prestados",
                (loan != null ? String.valueOf(loan.getId()) : ""),
                model,
                flash
        );
    }

    /**
     * Fetches a paginated list of all Loans and renders the list view.
     */

    @GetMapping("/prestados")
    public String list(@RequestParam(name = "page", defaultValue = "0") int page,
                       Model model, String keyword) {

        return ControllerGenericView.processView("prestados", page, keyword, model,
                loanService::findAll, loanService::findBykeyword);
    }

    /**
     * Prepares the model and displays the form to register a new Loan.
     */

    @GetMapping("/prestados/nuevo")
    public String loansForm(Model model) {

        return ControllerGenericView.displayForm(
                "prestado",
                "prestados",
                "nuevo/new_loans",
                model,
                Loan::new
        );
    }

    /**
     * Validates and persists a new Loans entry into the database.
     */

    @PostMapping("/prestados/guardar")
    public String save(
            @Valid @ModelAttribute("prestado") Loan loan,
            BindingResult result,
            RedirectAttributes flash,
            Model model
    ) {
        return ControllerGenericView.saveEntity(
                loan,
                result,
                flash,
                model,
                "Registro de prestados",
                "nuevo/new_loans",
                () -> loanService.saveLoans(loan),
                "redirect:/prestados/nuevo"
        );
    }

    /**
     * Retrieves an existing Loan data and displays the edit form.
     */

    @GetMapping("/prestados/editar/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("prestado", loanService.getLoansById(id));
        model.addAttribute("titulo", "Editar Prestado");
        return "edit/edit_loans";
    }

    /**
     * Processes the update of an existing Loan after validating the input data.
     */

    @PostMapping("/prestados/{id}")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("prestado") Loan loan,
                         BindingResult result,
                         RedirectAttributes flash,
                         Model model) {

        return ControllerGenericView.updateEntity(
                id,
                loan,
                result,
                model,
                flash,
                loanService::getLoansById,
                Loan::updateFrom,
                () -> loanService.saveLoans(loan),
                "Editar Prestado",
                "edit/edit_loans", 
                "/prestados",           
                "/prestados/editar/" + id 
        );
    }

    /**
     * Deletes a Loans record from the database. Restricted to ADMIN users.
     */

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/prestados/eliminar/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes flash) {
        return ControllerGenericView.executeDelete(id, loanService::deleteLoans, "prestados", flash);
    }

    /**
     * Generates and triggers the download of a PDF report containing all Loans.
     */

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/exportarPDFpr")
    public void exportPdf(HttpServletResponse response) {
        ControllerGenericView.processPdfExport(response, "Prestado",
                loanService::findAllLoans,
                (list, resp) -> {
                    try { new LoanPdfExporterpr(list).export(resp); }
                    catch (Exception e) { throw new RuntimeException(e); }
                });
    }

    /**
     * Generates and triggers the download of an Excel spreadsheet with the Loan inventory.
     */

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/exportarExcelpr")
    public void exportToExcel(HttpServletResponse response) {
        List<Loan> loans = loanService.findAllLoans();
        ControllerGenericView.exportToExcel(response, "Prestados", res -> {
            new LoanExcelExporterpr(loans).export(res);
        });
    }
}