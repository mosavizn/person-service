package com.isc.personservice.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "PERSON")
@Data
@AllArgsConstructor
@RequiredArgsConstructor
@NoArgsConstructor
public class PersonEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "NAME", nullable=false, length = 30)
    @NonNull
    private String name;

    @Column(name = "BIRTH_DATE", nullable=false)
    @NonNull
    private LocalDate birthDate;

    @Column(name = "BIRTH_DATE_DIFF_FROM_LEAP_YEAR")
    @NonNull
    private int birthDateDiffFromLeapYear;


}
