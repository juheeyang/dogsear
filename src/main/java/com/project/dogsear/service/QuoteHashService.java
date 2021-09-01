package com.project.dogsear.service;

import com.project.dogsear.domain.*;
import com.project.dogsear.dto.QuoteDto;
import com.project.dogsear.dto.QuoteHashDto;
import com.project.dogsear.dto.QuotePageDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Service
public class QuoteHashService {
    private final QuoteHashRepository quoteHashRepository;
    private final QuoteRepository quoteRepository;
    private final HashTagRepository hashTagRepository;

    @Transactional
    public Long save(Long quote_id,Long hash_id){

        QuoteHash quoteHash = QuoteHash.builder()
                .quote_id(quoteRepository.findById(quote_id).get())
                .hash_id(hashTagRepository.findById(hash_id).get())
                .build();

        return quoteHashRepository.save(quoteHash).getId();
    }

    @Transactional
    public List<Long> findTodeleteHashByQuoteId(Long quote_id){
        List<Long> result = new ArrayList<>();
        List<QuoteHash> quoteHashes = quoteHashRepository.findAll();
        for(int i=0;i<quoteHashes.size();i++){
            if(!quote_id.equals(quoteHashes.get(i).getQuoteid())){   //이미 등록된 모든 아이 찾아옴
                result.add(quoteHashes.get(i).getId());
            }
        }
        return result;
    }

    @Transactional
    public List<String> findHashByQuote(QuoteEntity quoteEntity){
        List<String> result = new ArrayList<>();
        List<QuoteHash> quoteHashes = quoteHashRepository.findAll();
        for(int i=0;i<quoteHashes.size();i++){
            if(quoteEntity.getId().equals(quoteHashes.get(i).getQuoteid().getId())){
                result.add(quoteHashes.get(i).getHashid().getHashtext());
            }
        }
        return result;
    }

    @Transactional
    public String quoteHashSave(String hash_text,User user){

        if(hash_text.equals("")||hash_text==null){
            return "nullTextError";
        }else if(hashTagRepository.findOneByUserAndHashtext(user,hash_text)==null){
            HashTag hashTag = HashTag.builder()
                    .hash_text(hash_text)
                    .user(user)
                    .build();
            hashTagRepository.save(hashTag);
        }

        return "success";

    }

    @Transactional
    public void quoteHashEdit(QuoteDto quoteDto,User user){
        List<String> Edited = quoteDto.getHashList();
        List<String> Origin = findHashByQuote(quoteRepository.findById(quoteDto.getId()).get());

        for(int i=0;i<Edited.size();i++){  // 없는 해시태그 save
            if(hashTagRepository.findOneByUserAndHashtext(user,Edited.get(i))==null){
                HashTag hashTag = HashTag.builder()
                        .hash_text(Edited.get(i))
                        .user(user)
                        .build();
                hashTagRepository.save(hashTag);
            }
        }

        for(int i=0;i<Edited.size();i++){  // 새로 추가된 해시태그
            hashTagRepository.findOneByUserAndHashtext(user,Edited.get(i)).setDate(LocalDate.now());
            if(!Origin.contains(Edited.get(i))){
                save(quoteDto.getId(),hashTagRepository.findOneByUserAndHashtext(user,Edited.get(i)).getId());
            }
        }
        for(int i=0;i<Origin.size();i++){ // 없어진 해시태그
            if(!Edited.contains(Origin.get(i))){
                HashTag hashTag = hashTagRepository.findOneByUserAndHashtext(user,Origin.get(i));
                QuoteEntity quoteEntity = quoteRepository.findById(quoteDto.getId()).get();
                quoteHashRepository.delete(quoteHashRepository.findOneByHashidAndQuoteid(hashTag,quoteEntity));
            }
        }

    }

    @Transactional
    public void quoteDelete(QuoteEntity quoteEntity){
        List<QuoteHash> quoteHashes= quoteHashRepository.findByQuoteid(quoteRepository.findById(quoteEntity.getId()).get());
        for(int i=0;i<quoteHashes.size();i++){
            quoteHashRepository.delete(quoteHashes.get(i));
        }
    }

    @Transactional
    public void hashDelete(HashTag hashTag){
        List<QuoteHash> quoteHashes= quoteHashRepository.findByHashid(hashTagRepository.findById(hashTag.getId()).get());
        for(int i=0;i<quoteHashes.size();i++){
            quoteHashRepository.delete(quoteHashes.get(i));
        }
    }

    @Transactional
    public void hashEdit(HashTag origin,HashTag edited){
        List<QuoteHash> quoteHashes= quoteHashRepository.findByHashid(origin);
        for(int i=0;i<quoteHashes.size();i++){
            quoteHashes.get(i).setHashid(edited);
        }
    }

    @Transactional
    public List<QuoteEntity> findByHashReturnQuoteList(HashTag hashTag){

       List<QuoteHash> quoteHashes = quoteHashRepository.findByHashid(hashTag);
        List<QuoteEntity> quoteEntities = new ArrayList<QuoteEntity>();
        for(int i=0;i<quoteHashes.size();i++){
            quoteEntities.add(quoteHashes.get(i).getQuoteid());
        }
        return quoteEntities;
    }

    public Long CountByHashid(HashTag hashTag){
        Long quoteHashes = quoteHashRepository.countByHashid(hashTag);
        return quoteHashes;
    }

}
