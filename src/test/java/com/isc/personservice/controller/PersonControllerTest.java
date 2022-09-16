package com.isc.personservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.isc.personservice.repository.PersonRepository;
import com.isc.personservice.web.dto.request.BirthDateFilterRequest;
import com.isc.personservice.web.dto.request.PersonRequest;
import com.isc.personservice.web.dto.response.GenericRestResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;

import java.net.URI;

import static org.hamcrest.core.StringContains.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class PersonControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    PersonRepository personRepository;

    private static final URI ADD_PERSON_URI = URI.create("/api/v1/person-service/addPerson");
    private static final URI DELETE_PERSON_URI = URI.create("/api/v1/person-service/deletePerson");
    private static final URI GET_PERSONS_BY_BIRTH_DATE_URI = URI.create("/api/v1/person-service/getPersonsByBirthDate");

    @Test
    public void deleteAvailablePerson() throws Exception {
        RequestBuilder req = post(DELETE_PERSON_URI)
            .contentType(MediaType.APPLICATION_JSON)
            .content("1");

        MvcResult mvcResult = this.mockMvc.perform(req)
            .andExpect(content().string(containsString("person data deleted successfully")))
            .andExpect(content().string(containsString("\"status\":".concat(String.valueOf(GenericRestResponse.STATUS.SUCCESS)))))
            .andExpect(status().isOk())
            .andDo(print())
            .andReturn();
    }

    @Test
    public void deleteInValidPerson() throws Exception {
        RequestBuilder req = post(DELETE_PERSON_URI)
            .contentType(MediaType.APPLICATION_JSON)
            .content("-1");

        MvcResult mvcResult = this.mockMvc.perform(req)
            .andExpect(content().string(containsString("person data not found")))
            .andExpect(content().string(containsString("\"status\":".concat(String.valueOf(GenericRestResponse.STATUS.FAILURE)))))
            .andExpect(status().isOk())
            .andDo(print())
            .andReturn();
    }

    @Test
    public void addValidPerson() throws Exception {
        PersonRequest personDto = new PersonRequest("Ali Rezayi", "1399-01-01");
        String addPersonStr = mapToJson(personDto);

        RequestBuilder req = post(ADD_PERSON_URI)
            .contentType(MediaType.APPLICATION_JSON)
            .content(addPersonStr);

        MvcResult mvcResult = this.mockMvc.perform(req)
            .andExpect(content().string(containsString("person data added successfully")))
            .andExpect(content().string(containsString("\"status\":".concat(String.valueOf(GenericRestResponse.STATUS.SUCCESS)))))
            .andExpect(status().isOk())
            .andDo(print())
            .andReturn();
    }

    @Test
    public void addPersonWithInValidBirthDate() throws Exception {
        PersonRequest personDto = new PersonRequest("Ali Rezayi", "1399-13-01");
        String addPersonStr = mapToJson(personDto);

        RequestBuilder req = post(ADD_PERSON_URI)
            .contentType(MediaType.APPLICATION_JSON)
            .content(addPersonStr);

        MvcResult mvcResult = this.mockMvc.perform(req)
            .andExpect(content().string(containsString("request data is not valid!!!")))
            .andExpect(content().string(containsString("\"status\":".concat(String.valueOf(GenericRestResponse.STATUS.FAILURE)))))
            .andExpect(status().isOk())
            .andDo(print())
            .andReturn();
    }

    @Test
    public void getPersonsByBirthDateForFarvardinFirstDay() throws Exception {
        BirthDateFilterRequest personDto = new BirthDateFilterRequest("1399-01-01");
        String searchBirthDateStr = mapToJson(personDto);

        RequestBuilder req = post(GET_PERSONS_BY_BIRTH_DATE_URI)
            .contentType(MediaType.APPLICATION_JSON)
            .content(searchBirthDateStr);

        MvcResult mvcResult = this.mockMvc.perform(req)
            .andExpect(content().string(containsString("Successfully")))
            .andExpect(content().string(containsString("\"status\":".concat(String.valueOf(GenericRestResponse.STATUS.SUCCESS)))))
            .andExpect(content().string(containsString("\"data\":".concat(String.valueOf("[\"Hoda Salehi\",\"Alireza Ghanbari\"]")))))
            .andExpect(status().isOk())
            .andDo(print())
            .andReturn();
    }


    @Test
    public void getPersonsByBirthDateForEsfandTwentyNineDay() throws Exception {
        BirthDateFilterRequest personDto = new BirthDateFilterRequest("1400-12-29");
        String searchBirthDateStr = mapToJson(personDto);

        RequestBuilder req = post(GET_PERSONS_BY_BIRTH_DATE_URI)
            .contentType(MediaType.APPLICATION_JSON)
            .content(searchBirthDateStr);

        MvcResult mvcResult = this.mockMvc.perform(req)
            .andExpect(content().string(containsString("Successfully")))
            .andExpect(content().string(containsString("\"status\":".concat(String.valueOf(GenericRestResponse.STATUS.SUCCESS)))))
            .andExpect(content().string(containsString("\"data\":".concat(String.valueOf("[\"Hamid Aghaee\",\"Ali Ramezani\",\"Shahin Moradi\",\"Reza Moradi\"]")))))
            .andExpect(status().isOk())
            .andDo(print())
            .andReturn();
    }

    @Test
    public void getPersonsByBirthDateForEsfandThirtyDay() throws Exception {
        BirthDateFilterRequest personDto = new BirthDateFilterRequest("1399-12-30");
        String searchBirthDateStr = mapToJson(personDto);

        RequestBuilder req = post(GET_PERSONS_BY_BIRTH_DATE_URI)
            .contentType(MediaType.APPLICATION_JSON)
            .content(searchBirthDateStr);

        MvcResult mvcResult = this.mockMvc.perform(req)
            .andExpect(content().string(containsString("Successfully")))
            .andExpect(content().string(containsString("\"status\":".concat(String.valueOf(GenericRestResponse.STATUS.SUCCESS)))))
            .andExpect(content().string(containsString("\"data\":".concat(String.valueOf("[\"Reza Moradi\"]")))))
            .andExpect(status().isOk())
            .andDo(print())
            .andReturn();
    }

    @Test
    public void getPersonsByBirthDateForEsfandTwentyNineDayInLeapYear() throws Exception {
        BirthDateFilterRequest personDto = new BirthDateFilterRequest("1399-12-29");
        String searchBirthDateStr = mapToJson(personDto);

        RequestBuilder req = post(GET_PERSONS_BY_BIRTH_DATE_URI)
            .contentType(MediaType.APPLICATION_JSON)
            .content(searchBirthDateStr);

        MvcResult mvcResult = this.mockMvc.perform(req)
            .andExpect(content().string(containsString("Successfully")))
            .andExpect(content().string(containsString("\"status\":".concat(String.valueOf(GenericRestResponse.STATUS.SUCCESS)))))
            .andExpect(content().string(containsString("\"data\":".concat(String.valueOf("[\"Hamid Aghaee\",\"Ali Ramezani\",\"Shahin Moradi\"]")))))
            .andExpect(status().isOk())
            .andDo(print())
            .andReturn();
    }

    @Test
    public void getPersonsByBirthDateForInValidBirthDateFormat() throws Exception {
        BirthDateFilterRequest personDto = new BirthDateFilterRequest("1399-13-29");
        String searchBirthDateStr = mapToJson(personDto);

        RequestBuilder req = post(GET_PERSONS_BY_BIRTH_DATE_URI)
            .contentType(MediaType.APPLICATION_JSON)
            .content(searchBirthDateStr);

        MvcResult mvcResult = this.mockMvc.perform(req)
            .andExpect(content().string(containsString("request data is not valid!!!")))
            .andExpect(content().string(containsString("\"status\":".concat(String.valueOf(GenericRestResponse.STATUS.FAILURE)))))
            .andExpect(status().isOk())
            .andDo(print())
            .andReturn();
    }

    protected static String mapToJson(Object obj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(obj);
    }
}
