package com.project.dogsear.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "Quote_Hash")
public class QuoteHash {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="quote_hash_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name="hash_id")
    private HashTag hashid;

    @ManyToOne
    @JoinColumn(name="quote_id")
    private QuoteEntity quoteid;

    @Builder
    public QuoteHash(HashTag hash_id, QuoteEntity quote_id){
        this.hashid = hash_id;
        this.quoteid = quote_id;
    }


}
