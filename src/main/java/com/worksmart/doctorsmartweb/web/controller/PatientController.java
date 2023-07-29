package com.worksmart.doctorsmartweb.web.controller;

import com.worksmart.doctorsmartweb.biz.model.Person;
import com.worksmart.doctorsmartweb.biz.service.PersonService;
import com.worksmart.doctorsmartweb.data.FileStorageRepository;
import com.worksmart.doctorsmartweb.data.PersonRepository;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/patient")
@Log4j2
public class PatientController {
    private PersonRepository personRepository;
    private FileStorageRepository fileStorageRepository;
    private PersonService personService;

    public PatientController(PersonRepository personRepository,
                             FileStorageRepository fileStorageRepository,
                             PersonService personService) {
        this.personService = personService;
        this.personRepository = personRepository;
        this.fileStorageRepository= fileStorageRepository;
    }

    @ModelAttribute("patient")
    public Iterable<Person> getPatient(){
        return  personRepository.findAll();
    }

    @ModelAttribute
    public Person getPerson() {
        Person person = new Person();
//        person.setFirstName("Jhon");

        return person;
    }
    @GetMapping
    public String showPatientPage() {
        return "patient";
    }

    @GetMapping(path = "/images/{resource}")
    public ResponseEntity<Resource> getResource(@PathVariable String resource){
        String dispo = """
                 attachment; filename="%s"
                """;
        dispo = dispo.format(dispo, resource);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, dispo)
                .body(fileStorageRepository.findByName(resource));
    }

    @PostMapping
    public String savePatient(@Valid Person person, Errors errors, 
                              @RequestParam("photoFilename")
                              MultipartFile photoFile) throws IOException {
        log.info(person);

        log.info("Errors are: " + errors);

        if (!errors.hasErrors()) {
            personRepository.save(person);
            personService.save(person, photoFile.getInputStream());
            fileStorageRepository.save(photoFile.getOriginalFilename(),
                    photoFile.getInputStream());
//        redirect + name of the view we want to redirect
            return "redirect:patient";
        } else {
            return "patient";
        }
    }

    @PostMapping(params = "action")
    public String modifyPatient(@RequestParam Optional<List<Long>> selections,
    @RequestParam String action, Model model){
        if(action.equals("delete")) {
            log.info(action);
            if (selections.isPresent()) {
//                personRepository.deleteAllById(selections.get());
                personService.deleteAllById(selections.get());
            }
            return "redirect:patient";
        } else{
            if (selections.isPresent()) {
//                we retrieve only the first item in the list with 2 get. the first gets list of the optional
                Optional<Person> person = personRepository.findById(selections.get().get(0));
                model.addAttribute("person", person);

            }
            return "patient";
        }

    }

    @PostMapping(params = "import=true")
    public String importCSV(@RequestParam MultipartFile csvFile){
        log.info("dimensiunea fis: "+ csvFile.getSize());
        try {
            personService.importCSV(csvFile.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return "redirect:patient";
    }


}
