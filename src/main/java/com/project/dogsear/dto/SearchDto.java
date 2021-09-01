package com.project.dogsear.dto;

import lombok.*;

import java.time.LocalDate;


@Getter
@Setter
@ToString
@NoArgsConstructor
public class SearchDto {
    private Long id;
    private String userEmail;
    private LocalDate date;
    private String searchText;

    @Builder
    public SearchDto(String searchText, LocalDate date, String userEmail){
        this.date = date;
        this.userEmail = userEmail;
        this.searchText = searchText;
    }
}

