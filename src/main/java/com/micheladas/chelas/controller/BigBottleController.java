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

/**
 * Controller handling the lifecycle and reporting of BigBottle resources.
 * Leverages a generic view handler to standardize CRUD operations and data export.
 */
@Controller
public class BigBottleController {

    private static final Logger log = LoggerFactory.getLogger(BigBottleController.class);

    @Autowired
    private BigBottleService bigBottleService;

    /** View Detail **/
    @GetMapping("/ver/{id}")
    public String showDetails(@PathVariable(value = "id") Long id, Map<String, Object> model, RedirectAttributes flash) {
        BigBottle bigBottle = bigBottleService.getBigBottleById(id);

        return ControllerGenericView.viewDetail(
                bigBottle,
                "bigBottle",
                "see/viewbigbottle",
                "/caguamas",
                (bigBottle != null ? bigBottle.getBrand() : ""),
                model,
                flash
        );
    }

    /** List **/
    @GetMapping("/caguamas")
    public String list(@RequestParam(name = "page", defaultValue = "0") int page,
                       Model model) {
        return ControllerGenericView.processView("caguamas", page, null, model,
                bigBottleService::findAll, null);
    }

    /** Display New Form **/
    @GetMapping("/caguamas/nuevo")
    public String showForm(Model model) {
        return ControllerGenericView.displayForm(
                "caguama",
                "caguamas",
                "nuevo/newbigbottle",
                model,
                BigBottle::new
        );
    }

    /** Save New **/
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
                "nuevo/newbigbottle",
                () -> bigBottleService.saveBigBottle(bigBottle),
                "redirect:/caguamas/nuevo"
        );
    }

    /** Edit Form **/
    @GetMapping("/caguamas/editar/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("caguama", bigBottleService.getBigBottleById(id));
        return "edit/edit_bigbottle";
    }

    /** 6. Update**/
    @PostMapping("/caguamas/{id}")
    public String updatBigBottle(@PathVariable Long id,
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

    /** Delete Data **/
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/caguamas/eliminar/{id}")
    public String deleteBigBottle(@PathVariable Long id, RedirectAttributes flash) {
        return ControllerGenericView.executeDelete(id, bigBottleService::deleteBigBottle, "caguamas", flash);
    }

    /** PDF Export **/
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/exportPDF")
    public void exportPdf(HttpServletResponse response) {
        ControllerGenericView.processPdfExport(response, "Caguamas",
                bigBottleService::findAllBigBottle,
                (list, resp) -> {
                    try {
                        new BigBottlePdfExporter(list).export(resp);
                    } catch (Exception e) {
                        log.error("Error al exportar PDF: {}", e.getMessage());
                    }
                });
    }

    /** Export To Excel **/
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/excelExport")
    public void exportToExcel(HttpServletResponse response) {
        List<BigBottle> caguamas = bigBottleService.findAllBigBottle();
        ControllerGenericView.exportToExcel(response, "Caguamas", res -> {
            new BigBottleExcelExporter(caguamas).export(res);
        });
    }

}