package com.hendisantika.jsoupdemo.service;

import com.hendisantika.jsoupdemo.model.CommissionCriteria;
import com.hendisantika.jsoupdemo.model.CommissionResult;
import org.jsoup.Connection;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by IntelliJ IDEA.
 * Project : jsoup-demo
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 15/06/21
 * Time: 07.49
 */
@Service
public class XuntaService {
    private static final Logger LOG = LoggerFactory.getLogger(XuntaService.class);

    /**
     * URL of the website for consulting the Xunta's publications.
     */
    private static final String URL_COMMISIONS_WEB = "http://www.xunta.es/comisions";

    /**
     * URL of the Xunta publications consultation service.
     */
    private static final String URL_COMMISIONS_SERVICE = "http://www.xunta.es/comisions/ConsultarAnuncios.do";

    /**
     * Search engine and template processing
     */
    @Autowired
    private SpringTemplateEngine templateEngine;

    private static String toString(Object filter) {
        return Objects.toString(filter, "");
    }

    public String createCommissionEmailContent(CommissionCriteria criteria, Collection<CommissionResult> commissions) {
        // Template context
        final Context ctx = new Context();
        ctx.setVariable("criteria", criteria);
        ctx.setVariable("results", commissions);

        // Generation of the email body.
        return templateEngine.process("commissions.html", ctx);
    }

    private static String formatDate(LocalDate date) {
        return date != null ? date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "";
    }

    public List<CommissionResult> getCommissions(CommissionCriteria criteria)
            throws IOException {

        List<CommissionResult> commissions = new ArrayList<CommissionResult>();

        // We make a request to the page of the xunta to obtain the cookies
        Connection.Response response = Jsoup
                .connect(URL_COMMISIONS_WEB)
                .method(Connection.Method.GET)
                .execute();

        // We make a request to the ad consultation service
        Document serviceResponse = Jsoup
                .connect(URL_COMMISIONS_SERVICE)
                .userAgent("Mozilla/5.0")
                .timeout(10 * 1000)
                .cookies(response.cookies())
                .data("aberto", toString(criteria.getUsers()))
                .data("conselleria", toString(criteria.getCounselling()))
                .data("desde", toString(formatDate(criteria.getStartDate())))
                .data("ata", toString(formatDate(criteria.getEndDate())))
                .data("grupo", toString(criteria.getGroup()))
                .data("nivel", toString(criteria.getLevel()))
                .data("provincia", toString(criteria.getProvinceCode()))
                .data("concello", toString(criteria.getDistrict()))
                .data("descricion", toString(criteria.getDescription()))
                .post();

        // Validación de errores.
        LOG.debug(serviceResponse.outerHtml());
        for (Element script : serviceResponse.getElementsByTag("script")) {
            Optional<DataNode> errorDataNode =
                    script.dataNodes().stream().filter(node -> node.getWholeData().contains(".error")).findFirst();
            if (errorDataNode.isPresent()) {
                Pattern p = Pattern.compile("toastr.error\\('(.*?)'\\)");
                Matcher m = p.matcher(errorDataNode.get().getWholeData());
                if (m.find()) {
                    throw new HttpStatusException(m.group(1), HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            URL_COMMISIONS_SERVICE);
                }
            }
        }

        // Procesamos la tabla de resultados: formulario + resultados.
        Elements tables = serviceResponse.select("table");
        if (tables.size() > 1) {
            Elements rows = tables.get(1).select("tr");

            // Eliminamos la última columna de la tabla (uris relativas).
            for (int i = 1; i < rows.size(); i++) {
                Elements values = rows.get(i).select("td");
                String destinyCenter = values.get(0).text();
                String jobCode = values.get(1).text();
                String denomination = values.get(2).text();
                String provision = values.get(3).text();
                String group = values.get(4).text();
                String level = values.get(5).text();

                commissions.add(new CommissionResult(destinyCenter, jobCode, denomination, provision, group, level));
            }
        }

        return commissions;
    }
}
