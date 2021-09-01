package com.project.dogsear.service;


import com.project.dogsear.domain.*;
import com.project.dogsear.dto.QuoteDto;
import com.project.dogsear.dto.QuotePageDto;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@AllArgsConstructor
@Service
public class QuoteService {
    private final QuoteRepository quoteRepository;
    private final QuotePageRepository quotePageRepository;
    private final UserRepository userRepository;
    private static final int BLOCK_PAGE_NUM_COUNT = 5;
    private static final int PAGE_POST_COUNT = 10;

    @Transactional
    public Long saveQuote(QuoteDto quoteDto){

        QuoteEntity quoteEntity = QuoteEntity.builder()
                .quote(quoteDto.getQuote())
                .writer(quoteDto.getWriter())
                .title(quoteDto.getTitle())
                .date(quoteDto.getDate())
                .color(quoteDto.getColor())
                .user(userRepository.findOneWithAuthoritiesByEmail(quoteDto.getUserEmail()).get())
                .build();

        return quoteRepository.save(quoteEntity).getId();
    }

    @Transactional
    public QuoteEntity editQuote(Long quote_id,QuoteDto quoteDto){

        quoteRepository.findById(quote_id).get().setQuote(quoteDto.getQuote());
        quoteRepository.findById(quote_id).get().setColor(quoteDto.getColor());
        quoteRepository.findById(quote_id).get().setDate(quoteDto.getDate());
        quoteRepository.findById(quote_id).get().setTitle(quoteDto.getTitle());
        quoteRepository.findById(quote_id).get().setWriter(quoteDto.getWriter());

        return quoteRepository.findById(quote_id).get();
    }
//    @Transactional
//    public QuoteEntity editQuote(QuoteDto quoteDto){
//
//        quoteRepository.findById(quoteDto.getId()).get().setQuote(quoteDto.getQuote());
//        quoteRepository.findById(quoteDto.getId()).get().setColor(quoteDto.getColor());
//        quoteRepository.findById(quoteDto.getId()).get().setDate(quoteDto.getDate());
//        quoteRepository.findById(quoteDto.getId()).get().setTitle(quoteDto.getTitle());
//        quoteRepository.findById(quoteDto.getId()).get().setWriter(quoteDto.getWriter());
//
//        return quoteRepository.findById(quoteDto.getId()).get();
//    }

//    @Transactional
//    public List<QuoteEntity> quotePagingAll(User user, Integer pageNum, Integer order, String date){
//        PageRequest pageRequest;
//
//        switch (order){
//
//            case 1: //오래된순 오름차순 Asc
//                pageRequest = PageRequest.of(pageNum, PAGE_POST_COUNT, Sort.by(Sort.Direction.ASC,"date"));
//                break;
//            case 2: //최신순 내림차순 Desc
//                pageRequest = PageRequest.of(pageNum, PAGE_POST_COUNT, Sort.by(Sort.Direction.DESC,"date"));
//                break;
//            case 3: //이름순 오름차순 Asc
//                pageRequest = PageRequest.of(pageNum, PAGE_POST_COUNT, Sort.by(Sort.Direction.ASC,"title"));
//                break;
//            default: //no 정렬
//                pageRequest = PageRequest.of(pageNum, PAGE_POST_COUNT );
//                break;
//        }
//
//        if(date.equals("")||date.equals(null)){
//            Page<QuoteEntity> quoteEntities = quotePageRepository.findByUser(user,pageRequest);
//            List<QuoteEntity> result = quoteEntities.getContent();
//            return result;
//        }
//        Page<QuoteEntity> quoteEntities = quotePageRepository.findByUserAndDate(user,LocalDate.parse(date, DateTimeFormatter.ISO_DATE),pageRequest);
//        List<QuoteEntity> result = quoteEntities.getContent();
//        return result;
//    }

    @Transactional
    public List<QuotePageDto> quotePagingAll(Long user_id, Integer pageNum, Integer order, String date){
        PageRequest pageRequest;

        switch (order){

            case 1: //오래된순 오름차순 Asc
                pageRequest = PageRequest.of(pageNum, PAGE_POST_COUNT, Sort.by(Sort.Direction.ASC,"date"));
                break;
            case 2: //최신순 내림차순 Desc
                pageRequest = PageRequest.of(pageNum, PAGE_POST_COUNT, Sort.by(Sort.Direction.DESC,"date"));
                break;
            case 3: //이름순 오름차순 Asc
                pageRequest = PageRequest.of(pageNum, PAGE_POST_COUNT, Sort.by(Sort.Direction.ASC,"title"));
                break;
            default: //no 정렬
                pageRequest = PageRequest.of(pageNum, PAGE_POST_COUNT );
                break;
        }

        if(date.equals("")||date.equals(null)){
            Page<QuotePageDto> quotePageDtos = quotePageRepository.findByUser(user_id,pageRequest);
            List<QuotePageDto> result = quotePageDtos.getContent();
            return result;
        }
        Page<QuotePageDto> quoteEntities = quotePageRepository.findByUserAndDate(user_id,LocalDate.parse(date, DateTimeFormatter.ISO_DATE),pageRequest);
        List<QuotePageDto> result = quoteEntities.getContent();
        return result;
    }

}
