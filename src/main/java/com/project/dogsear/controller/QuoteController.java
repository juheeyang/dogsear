package com.project.dogsear.controller;

import com.project.dogsear.domain.*;
import com.project.dogsear.dto.QuoteDto;
import com.project.dogsear.dto.QuotePageDto;
import com.project.dogsear.service.HashTagService;
import com.project.dogsear.service.QuoteHashService;
import com.project.dogsear.service.QuoteService;
import com.project.dogsear.service.UserService;
import com.project.dogsear.status.DefaultRes;
import com.project.dogsear.status.DefaultResNoResult;
import com.project.dogsear.status.ResponseMessage;
import com.project.dogsear.status.StatusCode;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;

@AllArgsConstructor
@RestController
@RequestMapping("/quote")
public class QuoteController {
    private final QuoteService quoteService;
    private final UserService userService;
    private final HashTagRepository hashTagRepository;
    private final QuoteHashService quoteHashService;
    private final HashTagService hashTagService;
    private final QuoteRepository quoteRepository;
    private final QuoteHashRepository quoteHashRepository;


    @PostMapping("/save")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity save(@RequestBody QuoteDto quoteDto){
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", "application/json;charset=UTF-8");

        if(quoteDto.getQuote().equals("")||quoteDto.getQuote().equals(null)){
            return new ResponseEntity(DefaultResNoResult.res(StatusCode.FAIL_CREATE_QUOTE_QUOTE, ResponseMessage.FAIL_CREATE_QUOTE_QUOTE),httpHeaders,  HttpStatus.OK);
        }
        if(quoteDto.getTitle().equals("")||quoteDto.getTitle().equals(null)){
            return new ResponseEntity(DefaultResNoResult.res(StatusCode.FAIL_CREATE_QUOTE_TITLE, ResponseMessage.FAIL_CREATE_QUOTE_TITLE),httpHeaders,  HttpStatus.OK);
        }
        if(quoteDto.getWriter().equals("")||quoteDto.getWriter().equals(null)){
            return new ResponseEntity(DefaultResNoResult.res(StatusCode.FAIL_CREATE_QUOTE_WRITER, ResponseMessage.FAIL_CREATE_QUOTE_WRITER),httpHeaders,  HttpStatus.OK);
        }

        User user = userService.getMyUserWithAuthorities().get();
        List<String> hash = quoteDto.getHashList();
        for(String h : hash){
            String result = quoteHashService.quoteHashSave(h , user );
            if(result.equals("nullTextError")){
                return new ResponseEntity(DefaultResNoResult.res(StatusCode.FAIL_HASH_TEXT_NULL,ResponseMessage.FAIL_HASH_TEXT_NULL),httpHeaders,HttpStatus.OK);
            }
        }
         //해시태그 데이터베이스에 있는지 없는지 확인해서 없으면 생성

        quoteDto.setUserEmail(user.getEmail());
        Long quote_id = quoteService.saveQuote(quoteDto);

        for(int i=0;i<quoteDto.getHashList().size();i++){
            Long hash_id = hashTagRepository.findOneByUserAndHashtext(userService.getMyUserWithAuthorities().get(),quoteDto.getHashList().get(i)).getId();
            quoteHashService.save(quote_id,hash_id);
        }

        List<Long> list = hashTagRepository.findNullHashid(userService.getMyUserWithAuthorities().get().getUserId());
        for(Long l : list){
            hashTagRepository.delete(hashTagRepository.findById(l).get());
        }
        return new ResponseEntity(DefaultRes.res(StatusCode.OK, ResponseMessage.CREATED_QUOTE, quote_id),httpHeaders,  HttpStatus.OK);

    }


    @GetMapping("/info/{quote_id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity info(@PathVariable Long quote_id){
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", "application/json;charset=UTF-8");

        User user = userService.getMyUserWithAuthorities().get();

        QuoteEntity quoteEntity = quoteRepository.findById(quote_id).get();
        QuoteDto quoteDto = new QuoteDto();
        quoteDto.setUserEmail(user.getEmail());
        List<String> hashList = quoteHashService.findHashByQuote(quoteEntity);
        BeanUtils.copyProperties(quoteEntity,quoteDto);
        quoteDto.setUserEmail(quoteEntity.getUser().getEmail());
        quoteDto.setHashList(hashList);

        if(!user.getEmail().equals(quoteEntity.getUser().getEmail())){
            return new ResponseEntity(DefaultResNoResult.res(StatusCode.UNAUTHORIZED, ResponseMessage.FAIL_READ_QUOTE_UNAUTHORIZED), httpHeaders, HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity(DefaultRes.res(StatusCode.OK, ResponseMessage.READ_QUOTE, quoteDto), httpHeaders,  HttpStatus.OK);
    }


    //인용구 수정 메소드
    @PutMapping("/edit/{quote_id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity edit(@PathVariable  Long quote_id, @RequestBody QuoteDto quoteDto) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", "application/json;charset=UTF-8");

        User user = userService.getMyUserWithAuthorities().get();
        if(!user.getEmail().equals( quoteRepository.findById(quote_id).get().getUser().getEmail()  )){
            return new ResponseEntity(DefaultResNoResult.res(StatusCode.UNAUTHORIZED, ResponseMessage.FAIL_EDIT_QUOTE_UNAUTHORIZED), httpHeaders, HttpStatus.UNAUTHORIZED);
        }

        quoteDto.setId(quote_id);
        if(quoteDto.getQuote().equals("")||quoteDto.getQuote().equals(null)){
            return new ResponseEntity(DefaultResNoResult.res(StatusCode.FAIL_CREATE_QUOTE_QUOTE, ResponseMessage.FAIL_CREATE_QUOTE_QUOTE),httpHeaders,  HttpStatus.OK);
        }
        if(quoteDto.getTitle().equals("")||quoteDto.getTitle().equals(null)){
            return new ResponseEntity(DefaultResNoResult.res(StatusCode.FAIL_CREATE_QUOTE_TITLE, ResponseMessage.FAIL_CREATE_QUOTE_TITLE),httpHeaders,  HttpStatus.OK);
        }
        if(quoteDto.getWriter().equals("")||quoteDto.getWriter().equals(null)){
            return new ResponseEntity(DefaultResNoResult.res(StatusCode.FAIL_CREATE_QUOTE_WRITER, ResponseMessage.FAIL_CREATE_QUOTE_WRITER),httpHeaders,  HttpStatus.OK);
        }

        quoteHashService.quoteHashEdit(quoteDto,user);
        QuoteEntity edited = quoteService.editQuote(quote_id, quoteDto);
        List<Long> list = hashTagRepository.findNullHashid(userService.getMyUserWithAuthorities().get().getUserId());
        for(Long l : list){
            hashTagRepository.delete(hashTagRepository.findById(l).get());
        }
        return new ResponseEntity(DefaultRes.res(StatusCode.OK, ResponseMessage.CORRECT_QUOTE, quoteDto.getId()),httpHeaders,  HttpStatus.OK);
    }

    //인용구 삭제 메소드
    @DeleteMapping("/delete/{quote_id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity delete(@PathVariable  Long quote_id) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", "application/json;charset=UTF-8");

        Map resultMap = new HashMap<String, Object>();

        QuoteEntity quote =quoteRepository.findById(quote_id).get();
        User user = userService.getMyUserWithAuthorities().get();

        if(quote==null){ //해당하는 인용구가 존재하지 않으면
            return new ResponseEntity(DefaultResNoResult.res(StatusCode.NOT_EXIST_QUOTE, ResponseMessage.NOT_EXIST_QUOTE), httpHeaders, HttpStatus.NOT_FOUND);
        }
        if(!quote.getUser().getEmail().equals(user.getEmail())){ //꺼내온 유저정보와 삭제할 인용구의 유저정보와 일치하지 않으면
            return new ResponseEntity(DefaultResNoResult.res(StatusCode.UNAUTHORIZED, ResponseMessage.FAIL_READ_QUOTE_UNAUTHORIZED), httpHeaders, HttpStatus.UNAUTHORIZED);
        }

        quoteHashService.quoteDelete(quote);
        quoteRepository.delete(quote);
        List<Long> list = hashTagRepository.findNullHashid(userService.getMyUserWithAuthorities().get().getUserId());
        for(Long l : list){
            hashTagRepository.delete(hashTagRepository.findById(l).get());
        }
        return new ResponseEntity(DefaultResNoResult.res(StatusCode.OK, ResponseMessage.QUOTE_DELETE_SUCCESS),httpHeaders,  HttpStatus.OK);
    }


    //위에거 수정********************
    @GetMapping("/info/all")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity findallPaging(@RequestParam(value = "page") Integer page_num,@RequestParam(value = "order") Integer order,@RequestParam(value = "date") String date) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", "application/json;charset=UTF-8");

        User user = userService.getMyUserWithAuthorities().get();

        List<QuotePageDto> result = quoteService.quotePagingAll(user.getUserId(),page_num,order,date);
        return new ResponseEntity(DefaultRes.res(StatusCode.OK, ResponseMessage.READ_QUOTE,result),httpHeaders,  HttpStatus.OK);
    }

    @GetMapping("/select/date")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity returnDate() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", "application/json;charset=UTF-8");

        List<QuoteEntity> quoteEntities = quoteRepository.findByUser(userService.getMyUserWithAuthorities().get());

        List<LocalDate> result = new ArrayList<>();
        List<Integer> numList = new ArrayList<>();
        List<List> colorList = new ArrayList<>();

        for(int i=0;i<quoteEntities.size();i++){
            if(!result.contains(quoteEntities.get(i).getDate())){
                result.add(quoteEntities.get(i).getDate());
                numList.add(1);
                List<Integer> list = new ArrayList<>();
                list.add(quoteEntities.get(i).getColor());
                colorList.add(list);
                continue;
            }

            for(int j=0;j<result.size();j++){
                if(result.get(j).equals(quoteEntities.get(i).getDate())){
                    if(numList.get(j)<3){
                        colorList.get(j).add(quoteEntities.get(i).getColor());
                    }
                    int count = numList.get(j) + 1;
                    numList.set(j,count);
                }

            }
        }

        List<Object> resultMap = new ArrayList<>();

        for(int i=0;i<result.size();i++){
            Map map = new HashMap<String,Object>();
            map.put("date",result.get(i));
            map.put("num",numList.get(i));
            map.put("color",colorList.get(i));
            resultMap.add(map);
        }

        return new ResponseEntity(DefaultRes.res(StatusCode.OK, ResponseMessage.QUOTE_DATE_RETURN_SUCCESS,resultMap),httpHeaders,  HttpStatus.OK);
    }

    @GetMapping("/info/hash")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity findByHash(@RequestParam(value = "hash_text") String hash_text){
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", "application/json;charset=UTF-8");

        HashTag hashTags = hashTagRepository
                .findOneByUserAndHashtext(userService.getMyUserWithAuthorities().get(),hash_text);
        List<QuoteHash> quoteHashes = quoteHashRepository.findByHashid(hashTags);
        List<QuotePageDto> result = new ArrayList<>();

        hashTagService.hitsUp(hashTags.getId());


        for(int i=0;i<quoteHashes.size();i++){
            QuotePageDto quoteDto = new QuotePageDto();
            BeanUtils.copyProperties(quoteHashes.get(i).getQuoteid(),quoteDto);
            result.add(quoteDto);
        }

        return new ResponseEntity(DefaultRes.res(StatusCode.OK, ResponseMessage.READ_QUOTE, result),httpHeaders,  HttpStatus.OK);
    }
}
