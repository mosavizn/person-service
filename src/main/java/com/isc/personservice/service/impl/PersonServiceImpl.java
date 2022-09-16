package com.isc.personservice.service.impl;


import com.isc.personservice.entity.PersonEntity;
import com.isc.personservice.repository.PersonRepository;
import com.isc.personservice.service.PersonService;
import com.isc.personservice.util.DateConverter;
import com.isc.personservice.web.dto.request.PersonRequest;
import com.isc.personservice.web.dto.response.GenericRestResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;



@AllArgsConstructor
@Service
public class PersonServiceImpl implements PersonService {

    public static final String LAST_MONTH = "12";
    public static final String LAST_DAY_OF_MONTH = "29";
    public static final String LAST_DAY_OF_MONTH_IN_LEAP_YEAR = "30";
    public static final int TWENTY = 20;
    public static final int MARCh = 3;

    PersonRepository personRepository;
    DateConverter dateConverter;

    @Override
    public GenericRestResponse addPerson(PersonRequest personRequest) {
        return addPersonEntity(personRequest);
    }

    @Override
    public GenericRestResponse findByBirthDate(String birthDate) {
        List<PersonEntity> persons = new ArrayList<>();
        List<String> personsName;
        if (isLastDayOfYearInLeapYear(birthDate) && !isJalaliLeapYear(birthDate) ) {
            return new GenericRestResponse(GenericRestResponse.STATUS.FAILURE,
                "this year is not a leap year!!!",
                null);
        }
        if (isLastDayOfYearInLeapYear(birthDate) && isJalaliLeapYear(birthDate) ){
            return new GenericRestResponse(GenericRestResponse.STATUS.SUCCESS,
                "Successfully",
                getBirthDateInLastDayOfLeapYear(birthDate, persons));
        }
        if (isLastDayOfYear(birthDate) && !isJalaliLeapYear(birthDate) ){
            return new GenericRestResponse(GenericRestResponse.STATUS.SUCCESS,
                "Successfully",
                getBirthDateInLastDayOfYear(birthDate, persons));
        }
        findPersonByGregorianBirthDate(calculateGregorianDateWithDiffFromLeapYear(birthDate), persons);
        personsName = persons.stream().map(PersonEntity::getName).collect(Collectors.toList());
        return new GenericRestResponse(GenericRestResponse.STATUS.SUCCESS,
            "Successfully",
            personsName);
    }

    @Override
    public GenericRestResponse deletePersonById(Long id) {
        Optional<PersonEntity> personEntity = personRepository.findById(id);
        if (personEntity.isPresent()){
            personRepository.delete(personEntity.get());
            return new GenericRestResponse(GenericRestResponse.STATUS.SUCCESS,
                "person data deleted successfully",
                null);
        }else {
            return new GenericRestResponse(GenericRestResponse.STATUS.FAILURE,
                "person data not found",
                null);
        }
    }

    private List<String> getBirthDateInLastDayOfYear(String birthDate, List<PersonEntity> persons) {
        List<String> personsName;
        findPersonByGregorianBirthDate(calculateGregorianDateWithDiffFromLeapYear(birthDate), persons);
        persons.addAll(personRepository.findByBirthDate(TWENTY, MARCh, 1));
        personsName = persons.stream().map(PersonEntity::getName).collect(Collectors.toList());
        return personsName;
    }

    private List<String> getBirthDateInLastDayOfLeapYear(String birthDate, List<PersonEntity> persons) {
        List<String> personsName;LocalDate gregorianDate = convertToGregorian(birthDate);
        persons.addAll(personRepository.findByBirthDate(gregorianDate.getDayOfMonth(),gregorianDate.getMonth().getValue(),1));
        personsName = persons.stream().map(PersonEntity::getName).collect(Collectors.toList());
        return personsName;
    }

    private void findPersonByGregorianBirthDate(Map<Integer,LocalDate> gregorianDatesMap, List<PersonEntity> persons) {
        persons.addAll(gregorianDatesMap.entrySet()
            .stream()
            .distinct()
            .flatMap(entry -> personRepository.findByBirthDate(entry.getValue().getDayOfMonth(), entry.getValue().getMonth().getValue(), entry.getKey()).stream())
            .collect(Collectors.toList()));
    }

    private Map<Integer,LocalDate> calculateGregorianDateWithDiffFromLeapYear(String birthDate) {
        Map<Integer,LocalDate> gregorianDateWithDiffFromLeapYear = new HashMap<>();
        int diffFromLeapYear = calculateDiffFromLeapYear(convertToGregorian(birthDate));
        int currentDiff = diffFromLeapYear;
        for (int i= diffFromLeapYear ; i >= (diffFromLeapYear - 3); i--){
            LocalDate gregorianBirthDate = convertToGregorian(birthDate);
            gregorianDateWithDiffFromLeapYear.put((currentDiff >= 0 ? currentDiff : (4 + currentDiff)), gregorianBirthDate);
            birthDate = minusJalaliYear(birthDate);
            currentDiff--;
        }
        return gregorianDateWithDiffFromLeapYear;
    }

    private String minusJalaliYear(String birthDate) {
        String[] dateParts = birthDate.split("-");
        return (Integer.parseInt(dateParts[0]) - 1) + "-" + dateParts[1] + "-" + dateParts[2];
    }


    private GenericRestResponse addPersonEntity(PersonRequest personRequest) {
        LocalDate birthDate = convertToGregorian(personRequest.getBirthDate());
        personRepository.save(generatePersonEntity(personRequest,birthDate));
        return new GenericRestResponse(GenericRestResponse.STATUS.SUCCESS,
            "person data added successfully",
            null);
    }

    private LocalDate convertToGregorian(String birthDate) {
        String[] dateParts = birthDate.split("-");
        return dateConverter.jalaliToGregorian(Integer.parseInt(dateParts[0]),Integer.parseInt(dateParts[1]), Integer.parseInt(dateParts[2]));
    }

    private boolean isJalaliLeapYear(String birthDate) {
        String[] birthDateParts = getBirthDateParts(birthDate);
        return dateConverter.jalaliToGregorian(Integer.parseInt(birthDateParts[0]),1, 1).isLeapYear();
    }

    private Boolean isLastDayOfYear(String birthDate){
        String[] birthDateParts = getBirthDateParts(birthDate);
        return birthDateParts[1].equals(LAST_MONTH) && birthDateParts[2].equals(LAST_DAY_OF_MONTH);
    }

    private Boolean isLastDayOfYearInLeapYear(String birthDate){
        String[] birthDateParts = getBirthDateParts(birthDate);
        return birthDateParts[1].equals(LAST_MONTH) && birthDateParts[2].equals(LAST_DAY_OF_MONTH_IN_LEAP_YEAR);
    }

    private String[] getBirthDateParts(String birthDate){
        return birthDate.split("-");
    }

    private PersonEntity generatePersonEntity(PersonRequest personRequest, LocalDate birthDate) {
        return new PersonEntity(personRequest.getName() ,birthDate, calculateDiffFromLeapYear(birthDate));
    }

    private Integer calculateDiffFromLeapYear(LocalDate birthDate) {
        int diffFromLeapYear = 0;
        if (birthDate.isLeapYear())
            return diffFromLeapYear;
        else {
            while (!birthDate.isLeapYear()){
                birthDate = birthDate.minusYears(1);
                diffFromLeapYear++;
            }
            return diffFromLeapYear;
        }
    }


}
