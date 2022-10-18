package com.isc.personservice.web.controller;

import com.isc.personservice.service.PersonService;
import com.isc.personservice.web.dto.request.BirthDateFilterRequest;
import com.isc.personservice.web.dto.request.PersonRequest;
import com.isc.personservice.web.dto.response.GenericRestResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/person-service")
@AllArgsConstructor
@Api(value = "Person Service API")
public class PersonController {
    private final Logger logger = LoggerFactory.getLogger(PersonController.class);

    private PersonService PersonService;

    @PostMapping(value = "/addPerson")
    @ApiOperation(value = "REST request to Add Person",
            produces = "Application/JSON", response = GenericRestResponse.class, httpMethod = "POST")
    public GenericRestResponse addPerson(
            @ApiParam(value = "PersonRequest", required = true)
            @RequestBody @Valid PersonRequest personRequest) {
        logger.debug("REST request to save person : {}", personRequest);
        return PersonService.addPerson(personRequest);
    }


    @PostMapping(value = "/getPersonsByBirthDate")
    @ApiOperation(value = "REST request to get Persons by birthDate",
            produces = "Application/JSON", response = GenericRestResponse.class, httpMethod = "POST")
    public GenericRestResponse getPersonsByBirthDate(
        @Valid
        @RequestBody
        @ApiParam(value = "BirthDateFilterRequest" ,required = true)
        BirthDateFilterRequest birthDate) {
            logger.debug("REST request to get persons by Birth date : {}", birthDate.getBirthDate());
            return PersonService.findByBirthDate(birthDate.getBirthDate());
    }

    @PostMapping(value = "/deletePerson")
    @ApiOperation(value = "REST request to delete a Person",
        produces = "Application/JSON", response = GenericRestResponse.class, httpMethod = "POST")
    public GenericRestResponse deletePerson(
        @RequestBody @ApiParam(value = "person Id", example = "1" ,required = true) @Valid Long id) {
        logger.debug("REST request to delete a Person by id : {}", id);
        return PersonService.deletePersonById(id);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public GenericRestResponse handleValidationException(){
            return new GenericRestResponse(GenericRestResponse.STATUS.FAILURE,
                "request data is not valid!!!",
                null);

    }


}
