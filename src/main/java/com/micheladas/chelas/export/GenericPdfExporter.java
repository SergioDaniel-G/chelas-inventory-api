package com.micheladas.chelas.export;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import jakarta.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.BiConsumer;

public class GenericPdfExporter<T> {

    private final List<T> data;
    private final String reportTitle;
    private final String[] columns;
    private final float[] width;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private final Color CORPORATE_BLUE = new Color(0, 51, 102);
    private final Color ZEBRA_GRAY = new Color(245, 245, 245);
    private final Color BORDER_GRAY = new Color(230, 230, 230);

    public GenericPdfExporter(List<T> data, String reportTitle, String[] columns, float[] width) {
        this.data = data;
        this.reportTitle = reportTitle;
        this.columns = columns;
        this.width = width;
    }

    public void export(HttpServletResponse response, BiConsumer<PdfPTable, T> rowGenerator) throws DocumentException, IOException {
        Document document = new Document(PageSize.A4.rotate());
        PdfWriter.getInstance(document, response.getOutputStream());
        document.open();

        Font logoFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 22, CORPORATE_BLUE);
        Paragraph logo = new Paragraph("MC", logoFont);
        logo.setAlignment(Element.ALIGN_LEFT);
        document.add(logo);

        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, Color.BLACK);
        Paragraph title = new Paragraph("MICHELADAS \"LAS CHELAS\" S.A. de C.V.", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);

        Font infoFont = FontFactory.getFont(FontFactory.HELVETICA, 9, Color.DARK_GRAY);
        Paragraph info = new Paragraph("R.F.C: MCHE900101-ABC | Folio de Reporte: " + System.currentTimeMillis() +
                "\nAv. Principal #123, Col. Centro, Ciudad de México | Tel: (55) 1234-5678", infoFont);
        info.setAlignment(Element.ALIGN_CENTER);
        info.setSpacingAfter(10);
        document.add(info);

        Paragraph line = new Paragraph("__________________________________________________________________________________________________________________________________", infoFont);
        line.setSpacingAfter(15);
        document.add(line);

        Paragraph subtitle = new Paragraph(reportTitle, FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12));
        subtitle.setSpacingAfter(15);
        document.add(subtitle);

        PdfPTable table = new PdfPTable(columns.length);
        table.setWidthPercentage(100);
        table.setWidths(width);

        writeHeader(table);

        int i = 0;
        for (T item : data) {

            Color rowColor = (i++ % 2 == 0) ? Color.WHITE : ZEBRA_GRAY;
            rowGenerator.accept(table, item);

            for (int j = 0; j < columns.length; j++) {
                PdfPCell c = table.getRows().get(table.getRows().size() - 1).getCells()[j];
                if (c != null) {
                    c.setBackgroundColor(rowColor);
                    c.setBorderColor(BORDER_GRAY);
                    c.setPadding(6);
                    c.setVerticalAlignment(Element.ALIGN_MIDDLE);
                }
            }
        }
        document.add(table);

        Paragraph space = new Paragraph("\n\n");
        document.add(space);
        Font footerFont = FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 8, Color.GRAY);
        Paragraph footer = new Paragraph("Este reporte ha sido generado automáticamente por el sistema Micheladas Chelas.\n" +
                "Fecha de impresión: " + LocalDateTime.now().format(formatter), footerFont);
        footer.setAlignment(Element.ALIGN_RIGHT);
        document.add(footer);

        document.close();
    }

    private void writeHeader(PdfPTable tabla) {
        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, Color.WHITE);
        for (String column : columns) {
            PdfPCell cell = new PdfPCell(new Phrase(column, font));
            cell.setBackgroundColor(CORPORATE_BLUE);
            cell.setPadding(8);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorderColor(Color.WHITE);
            tabla.addCell(cell);
        }
    }

    public void addSimpleCell(PdfPTable table, String text, int alignment) {
        Font font = FontFactory.getFont(FontFactory.HELVETICA, 9);
        PdfPCell cell = new PdfPCell(new Phrase(text != null ? text : "", font));
        cell.setHorizontalAlignment(alignment);
        table.addCell(cell);
    }
}