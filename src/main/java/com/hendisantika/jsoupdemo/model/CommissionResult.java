package com.hendisantika.jsoupdemo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by IntelliJ IDEA.
 * Project : jsoup-demo
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 15/06/21
 * Time: 07.46
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommissionResult {
    private String destinyCenter;

    private String jobCode;

    private String denomination;

    private String provision;

    private String group;

    private String level;
}
