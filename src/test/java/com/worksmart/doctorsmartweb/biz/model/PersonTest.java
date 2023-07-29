package com.worksmart.doctorsmartweb.biz.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class PersonTest {

    @Test
    public void canParse(){
        String csvLine = "1,Ioana,   Maria,ioanam@io.io,  10/10/2002";
        Person person = Person.parse(csvLine);
        assertThat(person.getDob()).isEqualTo(LocalDate.of(2002,10,10));

    }

}