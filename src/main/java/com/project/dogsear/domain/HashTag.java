package com.project.dogsear.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "hashTag")
public class HashTag{

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="hash_id")
    private Long id;


    @Column(nullable = false,name="hash_text")
    private String hashtext;

    @Column(nullable = false, name="bookmark", columnDefinition = "boolean default false")
    private boolean bookmark;

    @Column(nullable = false, name="hits", columnDefinition = "integer default 0")
    private int hits;

    @Column
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd", timezone="Asia/Seoul")
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    @Builder
    public HashTag(String hash_text, User user,boolean bookmark, int hits){
        this.hashtext= hash_text;
        this.user=user;
        this.bookmark=bookmark;
        this.hits=hits;
        this.date = LocalDate.now();
    }
}
