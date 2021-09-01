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
public class QuoteDto {
    private Long id;
    private String title;
    private String quote;
    private String writer;
    private String userEmail;
    private LocalDate date;
    private List<String> hashList;
    private int color;

    @Builder
    public QuoteDto(String title,String quote,String writer,LocalDate date,String userEmail,int color,List<String>hashList){
        this.title = title;
        this.quote = quote;
        this.writer = writer;
        this.date = date;
        this.userEmail = userEmail;
        this.color = color;
        this.hashList = hashList;
    }

    public QuoteEntity toEntity(){
        QuoteEntity quoteEntity = QuoteEntity.builder()
                .date(date)
                .quote(quote)
                .title(title)
                .writer(writer)
                .color(color)
                .build();
        return quoteEntity;
    }


}
