package com.isc.personservice.repository;

import com.isc.personservice.entity.PersonEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<PersonEntity,Long> {

    @Query(value = "SELECT p.* FROM Person p WHERE EXTRACT (day FROM p.birth_Date) = ?1 AND EXTRACT (month FROM p.birth_Date) = ?2  AND p.birth_date_diff_from_leap_year =?3 ", nativeQuery = true)
    List<PersonEntity> findByBirthDate(@Param("dayOfMonth") Integer dayOfMonth,
                                       @Param("month") Integer month,
                                       @Param("diffFromLeapYear") int diffFromLeapYear);
}
