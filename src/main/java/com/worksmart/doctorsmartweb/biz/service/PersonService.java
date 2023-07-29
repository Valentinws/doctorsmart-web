package com.worksmart.doctorsmartweb.biz.service;

import com.worksmart.doctorsmartweb.biz.model.Person;
import com.worksmart.doctorsmartweb.data.FileStorageRepository;
import com.worksmart.doctorsmartweb.data.PersonRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import java.util.zip.ZipInputStream;

@Service
public class PersonService {
    private final PersonRepository personRepository;
    private final FileStorageRepository storageRepository;

    public PersonService(PersonRepository personRepository, FileStorageRepository storageRepository) {
        this.personRepository = personRepository;
        this.storageRepository = storageRepository;
    }


    public Person save(Person person, InputStream photoStream) {
        Person savedPerson = personRepository.save(person);
        storageRepository.save(person.getPhotoFilename(),photoStream);
        return savedPerson;
    }

    public Optional<Person> findById(Long aLong) {
        return personRepository.findById(aLong);
    }

    public Iterable<Person> findAll() {
        return personRepository.findAll();
    }

    public void deleteAllById(Iterable<Long> ids) {
        Iterable<Person> patientToDelete = personRepository.findAllById(ids);
        Stream<Person> patientStream = StreamSupport.stream(patientToDelete.spliterator(), false);
        Set<String> filenames = patientStream
                .map(Person::getPhotoFilename)
                .collect(Collectors.toSet());
        personRepository.deleteAllById(ids);
        storageRepository.deleteAllByName(filenames);
    }

    public void importCSV(InputStream csvFileStream) {
        try {
            ZipInputStream zipInputStream = new ZipInputStream(csvFileStream);
            zipInputStream.getNextEntry();
            InputStreamReader inputStreamReader = new InputStreamReader(zipInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            bufferedReader.lines()
                    .skip(1)
                    .limit(3)
                    .map(Person::parse)
//                    .forEach(System.out::println);
                    .forEach(personRepository::save);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }
}
