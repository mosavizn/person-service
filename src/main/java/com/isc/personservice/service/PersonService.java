package com.isc.personservice.service;

import com.isc.personservice.web.dto.request.PersonRequest;
import com.isc.personservice.web.dto.response.GenericRestResponse;

public interface PersonService {
    GenericRestResponse addPerson(PersonRequest personRequest);

    GenericRestResponse findByBirthDate(String birthDate);

    GenericRestResponse deletePersonById(Long id);

}
