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
     * Retrieves and displays the detailed view of a specific Customer by its ID.
     */

    @GetMapping("/vercli/{id}")
    public String showDetails(@PathVariable(value = "id") Long id, Map<String, Object> model, RedirectAttributes flash) {

        Customer customer = customerService.getCustomersById(id);

        return ControllerGenericView.viewDetail(
                customer,
                "cliente",
                "see/viewcustomers",
                "/clientes",
                (customer != null ? customer.getFullName() : ""),
                model,
                flash
        );
    }

    /**
     * Fetches a paginated list of all Customers and renders the list view.
     */

    @GetMapping("/clientes")
    public String list(@RequestParam(name = "page", defaultValue = "0") int page,
                       Model model, @RequestParam(required = false) String keyword) {
        return ControllerGenericView.processView("clientes", page, keyword, model,
                customerService::findAll, customerService::findBykeyword);
    }

    /**
     * Prepares the model and displays the form to register a new Customer.
     */

    @GetMapping("/clientes/nuevo")
    public String showForm(Model model) {
        return ControllerGenericView.displayForm(
                "cliente",
                "clientes",
                "nuevo/new_customers",
                model,
                Customer::new
        );
    }

    /**
     * Validates and persists a new Customer entry into the database.
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
                "nuevo/new_customers",
                () -> customerService.saveCustomers(customer),
                "redirect:/clientes/nuevo"
        );
    }

    /**
     * Retrieves an existing Customer data and displays the edit form.
     */

    @GetMapping("/clientes/editar/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("cliente", customerService.getCustomersById(id));
        model.addAttribute("titulo", "Editar Cliente");
        return "edit/edit_customers";
    }

    /**
     * Processes the update of an existing Customer after validating the input data.
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
     * Deletes a Customer record from the database. Restricted to ADMIN users.
     */

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/clientes/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes flash) {
        return ControllerGenericView.executeDelete(id, customerService::deleteCustomers, "clientes", flash);
    }

    /**
     * Generates and triggers the download of a PDF report containing all Customers.
     */

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/exportarPDFcli")
    public void pdfExport(HttpServletResponse response) {
        ControllerGenericView.processPdfExport(response, "Clientes",
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
     * Generates and triggers the download of an Excel spreadsheet with the Customer inventory.
     */

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/exportarExcelcli")
    public void exportToExcel(HttpServletResponse response) {
        List<Customer> customers = customerService.findAllCustomers();
        ControllerGenericView.exportToExcel(response, "Clientes", res -> {
            new CustomerExcelExporter(customers).export(res);
        });
    }
}