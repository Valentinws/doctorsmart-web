package com.worksmart.doctorsmartweb.data;

import com.worksmart.doctorsmartweb.biz.model.Person;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

//@Component
//public class PersonDataLoader implements ApplicationRunner {
//    private PersonRepository personRepository;
//
//
//    public PersonDataLoader(PersonRepository personRepository) {
//        this.personRepository = personRepository;
//    }
//
//    @Override
//    public void run(ApplicationArguments args) throws Exception {
//
//        if (personRepository.count() == 0) {
//            List<Person> patient = List.of(
//                    new Person(null, "Gheorghe", "Ion", LocalDate.of(1960, 1, 1),"local@local.ro" ,new BigDecimal("2300")),
//                    new Person(null,"Ana", "Maria", LocalDate.of(1960, 1, 1),"local@local.ro" ,new BigDecimal("2300")),
//                    new Person(null, "Vali", "Val",LocalDate.of(1960, 1, 1),"local@local.ro" ,new BigDecimal("2300"))
//                    );
//
//            personRepository.saveAll(patient);
//        }
//
//    }
//}
