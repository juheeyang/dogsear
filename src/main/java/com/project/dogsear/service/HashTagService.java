package com.project.dogsear.service;

import com.project.dogsear.domain.*;
import com.project.dogsear.dto.HashTagDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@AllArgsConstructor
@Service
public class HashTagService  {
    private final HashTagRepository hashTagRepository;
    private final UserRepository userRepository;
    private final QuoteHashService quoteHashService;

    @Transactional
    public Long save(HashTagDto hashTagDto){
        HashTag hashTag = HashTag.builder()
                .hash_text(hashTagDto.getHash_text())
                .user(userRepository.findOneWithAuthoritiesByEmail(hashTagDto.getUserEmail()).get())
                .build();

        return hashTagRepository.save(hashTag).getId();
    }

    @Transactional
    public void findHashByname(String hashName,Long quote_id){
        List<HashTag> hashTags = hashTagRepository.findAll();
        for(int i=0;i<hashTags.size();i++){
            if(hashName.equals(hashTags.get(i).getHashtext())){
                quoteHashService.save(quote_id,hashTags.get(i).getId());
            }
        }
    }

    @Transactional
    public Long findOneByHashText(String hash_text){
        Long result = 0l;
        List<HashTag> hashTags = hashTagRepository.findAll();
        for(int i=0;i<hashTags.size();i++){
            if(hash_text.equals(hashTags.get(i).getHashtext())){
                result = hashTags.get(i).getId();
            }
        }
        return result;
    }

    @Transactional
    public void hashBookmarkSet(Long hash_id){
        HashTag hashTag = hashTagRepository.findById(hash_id).get();
        boolean bookmark = hashTag.isBookmark();
        hashTag.setBookmark(!bookmark);
    }

    @Transactional
    public String hashTextEdit(User user,Long hash_id,String hash_text){
        if(hashTagRepository.findOneByUserAndHashtext(user,hash_text)==null){
            HashTag hashTag = hashTagRepository.findById(hash_id).get();
            hashTag.setHashtext(hash_text);
            return "변경 성공";
        }else {  // 변경하려는 해시태그 이름이 이미 존재할 경우
            return "이름 중복";
        }

    }

    @Transactional
    public void hitsUp(Long hash_id){
        HashTag hashTag = hashTagRepository.findById(hash_id).get();
        int hits = hashTag.getHits();
        hits += 1;
        hashTag.setHits(hits);
    }

}
