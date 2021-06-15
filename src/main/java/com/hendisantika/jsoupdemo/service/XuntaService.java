package com.hendisantika.jsoupdemo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.spring5.SpringTemplateEngine;

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
}
