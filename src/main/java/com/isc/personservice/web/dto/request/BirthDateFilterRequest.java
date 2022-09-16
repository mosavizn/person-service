package com.isc.personservice.web.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class BirthDateFilterRequest {

    @ApiModelProperty(notes = "Person birthDate", example = "1399-01-01", required = true)
    @NotNull
    @Pattern(regexp = "^[1-4]\\d{3}\\-((0[1-6]\\-((3[0-1])|([1-2][0-9])|(0[1-9])))|((1[0-2]|(0[7-9]))\\-(30|31|([1-2][0-9])|(0[1-9]))))$")
    private String birthDate;
}
