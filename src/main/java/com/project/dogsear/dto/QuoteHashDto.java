package com.project.dogsear.dto;
import lombok.*;


import javax.persistence.*;
import java.time.LocalDateTime;


@Getter
@Setter
@ToString
@NoArgsConstructor
public class QuoteHashDto {
    private Long hash_id;
    private Long quote_id;

    @Builder
    public QuoteHashDto(Long hash_id, Long quote_id){

        this.hash_id = hash_id;
        this.quote_id = quote_id;

    }

}
