package com.hendisantika.jsoupdemo.service;

import com.hendisantika.jsoupdemo.model.CommissionCriteria;
import com.hendisantika.jsoupdemo.model.CommissionResult;
import org.jsoup.Connection;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.thymeleaf.spring5.SpringTemplateEngine;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
                .data("aberto", toString(criteria.getFinalUsers()))
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
    }
