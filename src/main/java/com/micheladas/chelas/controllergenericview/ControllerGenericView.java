package com.micheladas.chelas.controllergenericview;

import com.micheladas.chelas.pagination.PageRender;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.*;

/**
 * Standardized Utility class providing reusable logic for Controller operations,
 * including pagination, CRUD processing, and multi-format report exporting.
 */

public class ControllerGenericView {

    private static final Logger log = LoggerFactory.getLogger(ControllerGenericView.class);
    private static final int DEFAULT_PAGE_SIZE = 20;

    @FunctionalInterface
    public interface excelExporterAction {
        void execute(HttpServletResponse response) throws Exception;
    }

    /** * Orchestrates paginated listing and optional keyword-based searching.
     */

    public static String viewDetail(Object entity, String nameModel, String view, String redir, String tittleItem, Map<String, Object> model, RedirectAttributes flash) {

        if (entity == null) {
            flash.addFlashAttribute("error", "Los datos no existen en la base de datos");
            return "redirect:" + redir;
        }
        model.put(nameModel, entity);
        model.put("titulo", "Detalles de " + nameModel + ": " + tittleItem);
        return view;
    }

    /** * Orchestrates paginated listing and optional keyword-based searching.
     */

    public static <T> String processView(String name, int page, String keyword, Model model,
                                         Function<Pageable, Page<T>> searchPaginate,
                                         Function<String, Object> buscadorKeyword) {
        Pageable pageRequest = PageRequest.of(page, DEFAULT_PAGE_SIZE);
        Page<T> datosPaginados = searchPaginate.apply(pageRequest);
        model.addAttribute("page", new PageRender<>("/" + name, datosPaginados));
        if (keyword != null && !keyword.isEmpty()) {
            model.addAttribute(name, buscadorKeyword.apply(keyword));
        } else {
            model.addAttribute(name, datosPaginados);
        }
        return "main/" + name;
    }

    /** * Initializes the model for displaying a new entity creation form.
     */

    public static <T> String displayForm(String attibuteName, String tittle, String viewPath, Model model, Supplier<T> entityFactory) {
        model.addAttribute(attibuteName, entityFactory.get());
        model.addAttribute("tittle", "Registro de " + tittle);
        return viewPath;
    }

    /** * Handles validation and persistence for new entity records.
     */

    public static <T> String saveEntity(T entity, BindingResult result, RedirectAttributes flash, Model model, String tittle, String formPath, Runnable save, String redirect) {
        if (result.hasErrors()) {
            model.addAttribute("titulo", tittle);
            return formPath;
        }
        try {
            save.run();
            flash.addFlashAttribute("success", "Información guardada correctamente");
        } catch (Exception e) {
            log.error("Error al guardar la entidad: {}", e.getMessage());
            model.addAttribute("error", "Error al procesar la solicitud en la base de datos.");
            return formPath;
        }
        return redirect;
    }

    /**
     * Processes an update by validating input, checking for changes, and persisting.
     */

    public static <T> String updateEntity(
            Long id,
            T newData,
            BindingResult result,
            Model model,
            RedirectAttributes flash,
            Function<Long, T> searchById,
            BiFunction<T, T, Boolean> logicUpdate,
            Runnable saveAction,
            String tittle,
            String formPath,
            String redirSuccess,
            String redirOutChanges
    ) {

        if (result.hasErrors()) {
            model.addAttribute("titulo", tittle);
            return formPath;
        }

        T exist = searchById.apply(id);
        if (exist == null) {
            flash.addFlashAttribute("error", "El registro con ID " + id + " no existe.");
            return "redirect:" + redirSuccess;
        }

        boolean hasChanges = logicUpdate.apply(exist, newData);

        if (!hasChanges) {
            flash.addFlashAttribute("info", "No se detectaron cambios.");
            return "redirect:" + redirOutChanges;
        }

        try {
            saveAction.run();
            flash.addFlashAttribute("success", "Actualizado con éxito");
        } catch (Exception e) {
            log.error("Error crítico en actualización: {}", e.getMessage());
            model.addAttribute("error", "No se pudo actualizar debido a un error de integridad.");
            model.addAttribute("titulo", tittle);
            return formPath;
        }

        return "redirect:" + redirSuccess;
    }

    /** * Executes the deletion of a specific record by ID.
     */

    public static String executeDelete(Long id, Consumer<Long> deleteAction, String redirect, RedirectAttributes flash) {
        if (id != null && id > 0) {
            deleteAction.accept(id);
            flash.addFlashAttribute("success", "Información eliminada correctamente");
        }
        return "redirect:/" + redirect;
    }

    /** * Configures the HTTP response for PDF report generation.
     */

    public static <T> void processPdfExport(HttpServletResponse response, String prefix, Supplier<List<T>> supplierData, BiConsumer<List<T>, HttpServletResponse> pdfGenerator) {
        try {
            response.setContentType("application/pdf");
            String fileName = prefix + "_" + new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date()) + ".pdf";
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
            pdfGenerator.accept(supplierData.get(), response);
        } catch (Exception e) {
            log.error("Error PDF: {}", e.getMessage());
        }
    }

    /** * Configures the HTTP response for Excel report generation.
     */

    public static void exportToExcel(HttpServletResponse response, String namePrefix, excelExporterAction exporterAction) {
        try {
            response.setContentType("application/octet-stream");
            String fileName = String.format("%s_%s.xlsx", namePrefix, new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date()));
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
            exporterAction.execute(response);
        } catch (Exception e) {
            log.error("Error Excel: {}", e.getMessage());
        }
    }
}