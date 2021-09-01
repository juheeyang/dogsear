package com.project.dogsear.domain;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;


import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "Quote")
public class QuoteEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="quote_id")
    private Long id;

    @Column(length = 50, nullable = false)
    private String title;

    @Column(length = 10, nullable = false)
    private String writer;

    @Column(length = 500, nullable = false)
    private String quote;

    @Column
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd", timezone="Asia/Seoul")
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(length = 10,nullable = false)
    private int color;

    @Builder
    public QuoteEntity(String title, String quote, String writer, LocalDate date, User user, int color){
        this.title = title;
        this.quote = quote;
        this.writer = writer;
        this.date = date;
        this.user= user;
        this.color = color;
    }


}
