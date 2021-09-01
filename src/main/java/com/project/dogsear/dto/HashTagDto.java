package com.project.dogsear.dto;

import com.project.dogsear.domain.HashTag;
import lombok.*;


@Getter
@Setter
@ToString
@NoArgsConstructor
public class HashTagDto {
    private Long id;
    private String hash_text;
    private String userEmail;
    private boolean bookmark;
    private int hits;

    @Builder
    public HashTagDto(String hash_text, String userEmail, boolean bookmark, int hits){

        this.hash_text = hash_text;
        this.userEmail= userEmail;
        this.bookmark=bookmark;
        this.hits=hits;
    }

    public HashTag toEntity(){
        HashTag hashTag = HashTag.builder()
                .hash_text(hash_text)
                .bookmark(bookmark)
                .hits(hits)
                .build();
        return hashTag;
    }


}
