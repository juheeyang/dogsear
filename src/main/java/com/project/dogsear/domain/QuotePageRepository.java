package com.project.dogsear.domain;

import com.project.dogsear.dto.QuotePageDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface QuotePageRepository extends CrudRepository<QuoteEntity, Long> {
    Page<QuoteEntity> findByUser(User user, Pageable pageable);
    Page<QuoteEntity> findByUserAndDate(User user, LocalDate date,Pageable pageable);

    @Query("select new com.project.dogsear.dto.QuotePageDto(q.id,q.title,q.quote,q.writer,q.date,q.color) from QuoteEntity q where user_id =:user_id")
    Page<QuotePageDto> findByUser(@Param("user_id")Long user_id, Pageable pageable);

    @Query("select new com.project.dogsear.dto.QuotePageDto(q.id,q.title,q.quote,q.writer,q.date,q.color) from QuoteEntity q where user_id =:user_id and date =:date")
    Page<QuotePageDto> findByUserAndDate(@Param("user_id")Long user_id, @Param("date")LocalDate date,Pageable pageable);
}
