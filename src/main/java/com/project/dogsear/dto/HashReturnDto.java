package com.project.dogsear.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class HashReturnDto {
    private Long id;
    private String hash_text;
    private boolean bookmark;

    @Builder
    public HashReturnDto(Long id,String hash_text,boolean bookmark){
        this.id = id;
        this.hash_text = hash_text;
        this.bookmark = bookmark;
    }
}