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
     * RETRIEVES AND DISPLAYS THE DETAILED VIEW OF A SPECIFIC LOAN BY ITS ID.
     */

    @GetMapping("/verpr/{id}")
    public String showDetails(@PathVariable(value = "id") Long id, Map<String, Object> model, RedirectAttributes flash) {

        Loan loan = loanService.getLoansById(id);

        return ControllerGenericView.viewDetail(
                loan,
                "prestado",
                "see/viewloans",
                "/loans",
                (loan != null ? String.valueOf(loan.getId()) : ""),
                model,
                flash
        );
    }

    /**
     * FETCHES A PAGINATED LIST OF ALL LOANS AND RENDERS THE LIST VIEW.
     */

    @GetMapping("/prestados")
    public String list(@RequestParam(name = "page", defaultValue = "0") int page,
                       Model model, String keyword) {

        return ControllerGenericView.processView("loans", page, keyword, model,
                loanService::findAll, loanService::findBykeyword);
    }

    /**
     * PREPARES THE MODEL AND DISPLAYS THE FORM TO REGISTER A NEW LOAN.
     */

    @GetMapping("/prestados/nuevo")
    public String loansForm(Model model) {

        return ControllerGenericView.displayForm(
                "prestado",
                "loans",
                "newform/new_loans",
                model,
                Loan::new
        );
    }

    /**
     * VALIDATES AND PERSISTS A NEW LOANS ENTRY INTO THE DATABASE.
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
                "newform/new_loans",
                () -> loanService.saveLoans(loan),
                "redirect:/prestados/nuevo"
        );
    }

    /**
     * RETRIEVES AN EXISTING LOAN DATA AND DISPLAYS THE EDIT FORM.
     */

    @GetMapping("/prestados/editar/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("prestado", loanService.getLoansById(id));
        model.addAttribute("titulo", "Editar Prestado");
        return "edit/edit_loans";
    }

    /**
     * PROCESSES THE UPDATE OF AN EXISTING LOAN AFTER VALIDATING THE INPUT DATA.
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
     * DELETES A LOANS RECORD FROM THE DATABASE. RESTRICTED TO ADMIN USERS.
     */

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/prestados/eliminar/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes flash) {
        return ControllerGenericView.executeDelete(id, loanService::deleteLoans, "loans", flash);
    }

    /**
     * GENERATES AND TRIGGERS THE DOWNLOAD OF A PDF REPORT CONTAINING ALL LOANS.
     */

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/exportarPDFpr")
    public void exportPdf(HttpServletResponse response) {
        ControllerGenericView.processPdfExport(response, "loans",
                loanService::findAllLoans,
                (list, resp) -> {
                    try { new LoanPdfExporterpr(list).export(resp); }
                    catch (Exception e) { throw new RuntimeException(e); }
                });
    }

    /**
     * GENERATES AND TRIGGERS THE DOWNLOAD OF AN EXCEL SPREADSHEET WITH THE LOAN INVENTORY.
     */

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/exportarExcelpr")
    public void exportToExcel(HttpServletResponse response) {
        List<Loan> loans = loanService.findAllLoans();
        ControllerGenericView.exportToExcel(response, "loans", res -> {
            new LoanExcelExporterpr(loans).export(res);
        });
    }
}