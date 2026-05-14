package com.micheladas.chelas.export;

import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.micheladas.chelas.entity.UserIp;
import com.micheladas.chelas.genericcontrollerexporter.GenericPdfExporter;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class IpsPdfExporter {

    private List<UserIp> userIpList;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yy HH:mm");

    public IpsPdfExporter(List<UserIp> userIpList) {
        this.userIpList = userIpList;
    }

    public void export(HttpServletResponse response) throws DocumentException, IOException {

        String[] columns = {"ID", "FECHA", "EMAIL", "IP", "DISP", "ESTADO", "RIESGO", "BOT", "INFO"};

        float[] width = { 0.6f, 1.6f, 2.5f, 1.4f, 0.8f, 1.4f, 1.2f, 0.6f, 5f };

        GenericPdfExporter<UserIp> exporter = new GenericPdfExporter<>(
                userIpList,
                "AUDITORÍA DE ACCESOS E IPS",
                columns,
                width
        );

        exporter.export(response, (table, userIp) -> {

            exporter.addSimpleCell(table, String.valueOf(userIp.getId()), Element.ALIGN_CENTER);

            String fecha = userIp.getLoginTime() != null ? userIp.getLoginTime().format(formatter) : "N/A";
            exporter.addSimpleCell(table, fecha, Element.ALIGN_CENTER);

            exporter.addSimpleCell(table, userIp.getEmail(), Element.ALIGN_LEFT);

            exporter.addSimpleCell(table, userIp.getIpAddress(), Element.ALIGN_CENTER);

            exporter.addSimpleCell(table, userIp.getDevice(), Element.ALIGN_CENTER);

            exporter.addSimpleCell(table, userIp.getStatus(), Element.ALIGN_CENTER);

            exporter.addSimpleCell(table, userIp.getRiskLevel(), Element.ALIGN_CENTER);

            Boolean botValue = userIp.getIsBot();
            String botText = (botValue != null && botValue) ? "SÍ" : "NO";
            exporter.addSimpleCell(table, botText, Element.ALIGN_CENTER);

            String info = userIp.getUserAgent() != null ? userIp.getUserAgent() : "-";
            exporter.addSimpleCell(table, info, Element.ALIGN_LEFT);
        });
    }
}