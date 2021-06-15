package com.hendisantika.jsoupdemo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

/**
 * Created by IntelliJ IDEA.
 * Project : jsoup-demo
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 15/06/21
 * Time: 07.41
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommissionCriteria {
    @Pattern(regexp = "$|F|L", message = "invalid value ('F' oficials, 'L' work staff)")
    private String Users;

    private String counselling;

    @NotNull(message = "must be not null (pattern 'yyyy-MM-dd')")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @NotNull(message = "must be not null (pattern 'yyyy-MM-dd')")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    @Pattern(regexp = "$|A1|A2|C1|C2|AP|I|II|III|IV|V", message = "invalid value (A1, A2, C1, C2, AP, I, II, III, IV," +
            " V)")
    private String group;

    private String level;

    @Pattern(regexp = "$|15|27|28|32|36", message = "invalid value ('15' A Coruña, '27' Lugo, '28' Madrid, '32' " +
            "Ourense, '36' Pontevedra")
    private String provinceCode;

    private String district;

    private String description;

}
