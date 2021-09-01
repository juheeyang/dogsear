package com.project.dogsear.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "search")
public class Search {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="search_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "hash_id")
    private HashTag hash;


    @Column
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd", timezone="Asia/Seoul")
    private LocalDate date;

    @Builder
    public Search(HashTag hash, LocalDate date, User user){
        this.hash=hash;
        this.date = date;
        this.user= user;
    }
}
