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
import com.micheladas.chelas.entity.Customer;
import com.micheladas.chelas.export.CustomerExcelExporter;
import com.micheladas.chelas.export.CustomerPdfExporter;
import com.micheladas.chelas.service.CustomerService;


@Controller
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    /**
     * RETRIEVES AND DISPLAYS THE DETAILED VIEW OF A SPECIFIC CUSTOMER BY ITS ID.
     */

    @GetMapping("/vercli/{id}")
    public String showDetails(@PathVariable(value = "id") Long id, Map<String, Object> model, RedirectAttributes flash) {

        Customer customer = customerService.getCustomersById(id);

        return ControllerGenericView.viewDetail(
                customer,
                "cliente",
                "see/viewcustomers",
                "/customers",
                (customer != null ? customer.getFullName() : ""),
                model,
                flash
        );
    }

    /**
     * FETCHES A PAGINATED LIST OF ALL CUSTOMERS AND RENDERS THE LIST VIEW.
     */

    @GetMapping("/clientes")
    public String list(@RequestParam(name = "page", defaultValue = "0") int page,
                       Model model, @RequestParam(required = false) String keyword) {
        return ControllerGenericView.processView("customers", page, keyword, model,
                customerService::findAll, customerService::findBykeyword);
    }

    /**
     * PREPARES THE MODEL AND DISPLAYS THE FORM TO REGISTER A NEW CUSTOMER.
     */

    @GetMapping("/clientes/nuevo")
    public String showForm(Model model) {
        return ControllerGenericView.displayForm(
                "cliente",
                "customers",
                "newform/new_customers",
                model,
                Customer::new
        );
    }

    /**
     * VALIDATES AND PERSISTS A NEW CUSTOMER ENTRY INTO THE DATABASE.
     */


    @PostMapping("/clientes/guardar")
    public String save(
            @Valid @ModelAttribute("cliente") Customer customer,
            BindingResult result,
            RedirectAttributes flash,
            Model model
    ) {
        return ControllerGenericView.saveEntity(
                customer,
                result,
                flash,
                model,
                "Registro de clientes",
                "newform/new_customers",
                () -> customerService.saveCustomers(customer),
                "redirect:/clientes/nuevo"
        );
    }

    /**
     * RETRIEVES AN EXISTING CUSTOMER DATA AND DISPLAYS THE EDIT FORM.
     */

    @GetMapping("/clientes/editar/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("cliente", customerService.getCustomersById(id));
        model.addAttribute("titulo", "Editar Cliente");
        return "edit/edit_customers";
    }

    /**
     * PROCESSES THE UPDATE OF AN EXISTING CUSTOMER AFTER VALIDATING THE INPUT DATA.
     */

    @PostMapping("/clientes/{id}")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("cliente") Customer customer,
                         BindingResult result,
                         RedirectAttributes flash,
                         Model model) {

        return ControllerGenericView.updateEntity(
                id,
                customer,
                result,
                model,
                flash,
                customerService::getCustomersById,
                Customer::updateFrom,
                () -> customerService.saveCustomers(customer),
                "Editar Cliente",
                "edit/edit_customers",
                "/clientes",
                "/clientes/editar/" + id
        );
    }

    /**
     * DELETES A CUSTOMER RECORD FROM THE DATABASE. RESTRICTED TO ADMIN USERS.
     */

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/clientes/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes flash) {
        return ControllerGenericView.executeDelete(id, customerService::deleteCustomers, "clientes", flash);
    }

    /**
     * GENERATES AND TRIGGERS THE DOWNLOAD OF A PDF REPORT CONTAINING ALL CUSTOMERS.
     */

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/exportarPDFcli")
    public void pdfExport(HttpServletResponse response) {
        ControllerGenericView.processPdfExport(response, "customers",
                customerService::findAllCustomers,
                (list, resp) -> {
                    try {
                        new CustomerPdfExporter(list).export(resp);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    /**
     * GENERATES AND TRIGGERS THE DOWNLOAD OF AN EXCEL SPREADSHEET WITH THE CUSTOMER INVENTORY.
     */

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/exportarExcelcli")
    public void exportToExcel(HttpServletResponse response) {
        List<Customer> customers = customerService.findAllCustomers();
        ControllerGenericView.exportToExcel(response, "customers", res -> {
            new CustomerExcelExporter(customers).export(res);
        });
    }
}