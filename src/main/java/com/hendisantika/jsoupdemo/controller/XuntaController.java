package com.hendisantika.jsoupdemo.controller;

import com.hendisantika.jsoupdemo.service.XuntaService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by IntelliJ IDEA.
 * Project : jsoup-demo
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 15/06/21
 * Time: 08.02
 */
@RestController
@RequestMapping("xunta")
@Validated
@Log4j2
public class XuntaController {
    @Autowired
    private XuntaService xuntaService;

    @Autowired
    private JavaMailSender javaMailSender;
}
