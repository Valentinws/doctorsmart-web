package com.worksmart.doctorsmartweb.biz.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@AllArgsConstructor
@Data
@Entity
@NoArgsConstructor
public class Person {
    @Id
    @GeneratedValue
    private Long id;

    @NotEmpty(message = "First name can not be empty")
    private String firstName;
    @NotEmpty(message = "Last name can not be empty")
    private String lastName;
    @Past(message = "Date of birth must be in the past!")
    @NotNull(message = "Must not be empty!")
    private LocalDate dob;
    @Email(message = "Email must be valid")
    @NotEmpty(message = "Email can not be empty")
    private String email;
//    @DecimalMin(value = "222", message = "Must be at least 222")
//    @NotNull(message = "Must not be empty!")
//    private Optional<BigDecimal> salary;

    private String photoFilename;

    public static Person parse(String csvLine) {
        String[] fields = csvLine.split(";\\s*");
        LocalDate dob = LocalDate.parse(fields[4], DateTimeFormatter.ofPattern("M/d/yyyy"));
        return new Person(null, fields[1], fields[2], dob, fields[3], null);
    }


    // DON't DO this, not ok to change data displaying in the business logic. do it in the presentation layer(web)
//    public String getFormattedDOB(){
//        return DateTimeFormatter.ofPattern("dd MMMM, yyyy").format(dob);
//
//    }


}
