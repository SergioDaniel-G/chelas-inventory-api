package com.micheladas.chelas.controller;

import java.util.List;
import java.util.Map;
import com.micheladas.chelas.controllergenericview.ControllerGenericView;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.micheladas.chelas.entity.BigBottle;
import com.micheladas.chelas.export.BigBottleExcelExporter;
import com.micheladas.chelas.export.BigBottlePdfExporter;
import com.micheladas.chelas.service.BigBottleService;

@Controller
public class BigBottleController {

    private static final Logger log = LoggerFactory.getLogger(BigBottleController.class);

    @Autowired
    private BigBottleService bigBottleService;

    /**
     * Retrieves and displays the detailed view of a specific BigBottle by its ID.
     */

    @GetMapping("/ver/{id}")
    public String showDetails(@PathVariable(value = "id") Long id, Map<String, Object> model, RedirectAttributes flash) {
        BigBottle bigBottle = bigBottleService.getBigBottleById(id);

        return ControllerGenericView.viewDetail(
                bigBottle,
                "caguama",
                "see/viewbigbottle",
                "/bigbottles",
                (bigBottle != null ? bigBottle.getBrand() : ""),
                model,
                flash
        );
    }

    /**
     * Fetches a paginated list of all BigBottles and renders the list view.
     */

    @GetMapping("/caguamas")
    public String list(@RequestParam(name = "page", defaultValue = "0") int page,
                       Model model) {
        return ControllerGenericView.processView("bigbottles", page, null, model,
                bigBottleService::findAll, null);
    }

    /**
     * Prepares the model and displays the form to register a new BigBottle.
     */

    @GetMapping("/caguamas/nuevo")
    public String showForm(Model model) {
        return ControllerGenericView.displayForm(
                "caguama",
                "bigbottles",
                "newform/newbigbottle",
                model,
                BigBottle::new
        );
    }

    /**
     * Validates and persists a new BigBottle entry into the database.
     */

    @PostMapping("/caguamas/guardar")
    public String save(
            @Valid @ModelAttribute("caguama") BigBottle bigBottle,
            BindingResult result,
            RedirectAttributes flash,
            Model model
    ) {
        return ControllerGenericView.saveEntity(
                bigBottle,
                result,
                flash,
                model,
                "Registro de caguamas",
                "newform/newbigbottle",
                () -> bigBottleService.saveBigBottle(bigBottle),
                "redirect:/caguamas/nuevo"
        );
    }

    /**
     * Retrieves an existing BigBottle's data and displays the edit form.
     */

    @GetMapping("/caguamas/editar/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("caguama", bigBottleService.getBigBottleById(id));
        return "edit/edit_bigbottle";
    }

    /**
     * Processes the update of an existing BigBottle after validating the input data.
     */

    @PostMapping("/caguamas/{id}")
    public String updateBigBottle(@PathVariable Long id,
                                  @Valid @ModelAttribute("caguama") BigBottle bigBottle,
                                  BindingResult result,
                                  RedirectAttributes flash,
                                  Model model) {

        return ControllerGenericView.updateEntity(
                id,
                bigBottle,
                result,
                model,
                flash,
                bigBottleService::getBigBottleById,
                BigBottle::updateFrom,
                () -> bigBottleService.saveBigBottle(bigBottle),
                "Editar Caguama",
                "edit/edit_bigbottle",
                "/caguamas",
                "/caguamas/editar/" + id
        );
    }

    /**
     * Deletes a BigBottle record from the database. Restricted to ADMIN users.
     */

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/caguamas/eliminar/{id}")
    public String deleteBigBottle(@PathVariable Long id, RedirectAttributes flash) {
        return ControllerGenericView.executeDelete(id, bigBottleService::deleteBigBottle, "caguamas", flash);
    }

    /**
     * Generates and triggers the download of a PDF report containing all BigBottles.
     */

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/exportPDF")
    public void exportPdf(HttpServletResponse response) {
        ControllerGenericView.processPdfExport(response, "bigbottles",
                bigBottleService::findAllBigBottle,
                (list, resp) -> {
                    try {
                        new BigBottlePdfExporter(list).export(resp);
                    } catch (Exception e) {
                        log.error("Error al exportar PDF: {}", e.getMessage());
                    }
                });
    }

    /**
     * Generates and triggers the download of an Excel spreadsheet with the BigBottle inventory.
     */

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/excelExport")
    public void exportToExcel(HttpServletResponse response) {
        List<BigBottle> caguamas = bigBottleService.findAllBigBottle();
        ControllerGenericView.exportToExcel(response, "bigbottles", res -> {
            new BigBottleExcelExporter(caguamas).export(res);
        });
    }

}