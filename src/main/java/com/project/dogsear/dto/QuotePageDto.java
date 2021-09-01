package com.project.dogsear.dto;

import com.project.dogsear.domain.QuoteEntity;
import com.project.dogsear.domain.User;
import com.project.dogsear.domain.UserRepository;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class QuotePageDto {
    private Long id;
    private String title;
    private String quote;
    private String writer;
    private LocalDate date;
    private int color;

    @Builder
    public QuotePageDto(Long id,String title,String quote,String writer,LocalDate date,int color){
        this.id = id;
        this.title = title;
        this.quote = quote;
        this.writer = writer;
        this.date = date;
        this.color = color;
    }

}
