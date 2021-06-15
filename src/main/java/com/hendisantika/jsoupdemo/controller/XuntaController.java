package com.hendisantika.jsoupdemo.controller;

import com.hendisantika.jsoupdemo.model.CommissionCriteria;
import com.hendisantika.jsoupdemo.model.CommissionResult;
import com.hendisantika.jsoupdemo.service.XuntaService;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import java.io.IOException;
import java.util.List;

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

    @GetMapping("commission")
    public List<CommissionResult> getCommissions(@Email String recipient, @Valid CommissionCriteria filters)
            throws IOException, MessagingException {

        log.info("request started -> '/xunta/commission' -> recipient ({}), filters ({})", recipient, filters);
        final List<CommissionResult> commissions = xuntaService.getCommissions(filters);
        if (StringUtils.isNotEmpty(recipient) && commissions.size() > 0) {
            MimeMessage msg = javaMailSender.createMimeMessage();
            msg.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
            msg.setSubject("Xunta's Commission Results");
            msg.setContent(xuntaService.createCommissionEmailContent(filters, commissions), "text/html;charset=UTF-8");
            javaMailSender.send(msg);
        }
        log.info("request finished -> results ({})", commissions.size());
        return commissions;
    }
}
