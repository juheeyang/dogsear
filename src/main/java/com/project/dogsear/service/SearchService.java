package com.project.dogsear.service;

import com.project.dogsear.domain.*;
import com.project.dogsear.dto.SearchDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@AllArgsConstructor
@Service
public class SearchService {
    private final UserRepository userRepository;
    private final SearchRepository searchRepository;
    private final HashTagRepository hashTagRepository;


    @Transactional
    public Search save(SearchDto searchDto,Long hash_id){
        Search search = Search.builder()
                .date(searchDto.getDate())
                .hash(hashTagRepository.findById(hash_id).get())
                .user(userRepository.findOneWithAuthoritiesByEmail(searchDto.getUserEmail()).get())
                .build();

        return searchRepository.save(search);
    }

    @Transactional
    public Search update(Long search_id, SearchDto searchDto){
        searchRepository.findById(search_id).get().setDate(searchDto.getDate());
        return searchRepository.findById(searchDto.getId()).get();
    }

    //
//    @Transactional
//    public Boolean delete(User user,String search_text){
//        HashTag hashTag=hashTagRepository.findOneByUserAndHashtext(user,search_text);
//        searchRepository.delete(searchRepository.findOneByHashid( hashTag ) );
//        return true;
//    }

}
