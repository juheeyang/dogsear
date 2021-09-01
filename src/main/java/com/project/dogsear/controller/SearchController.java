package com.project.dogsear.controller;


import com.project.dogsear.domain.*;
import com.project.dogsear.dto.HashTagDto;
import com.project.dogsear.dto.QuoteDto;
import com.project.dogsear.dto.SearchDto;
import com.project.dogsear.service.HashTagService;
import com.project.dogsear.service.QuoteHashService;
import com.project.dogsear.service.SearchService;
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

import java.util.*;

@AllArgsConstructor
@RestController
@RequestMapping("/search")
public class SearchController {
    private final UserService userService;
    private final SearchService searchService;
    private final SearchRepository searchRepository;
    private final HashTagRepository hashTagRepository;
    private final QuoteHashRepository quoteHashRepository;
    private final QuoteHashService quoteHashService;
    private final HashTagService hashTagService;

    @PostMapping("/result") //검색기록 저장
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity write(@RequestBody SearchDto searchDto){
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", "application/json;charset=UTF-8");

        Map map= new HashMap();
        searchDto.setUserEmail(userService.getMyUserWithAuthorities().get().getEmail());
        HashTag hash = hashTagRepository.findOneByUserAndHashtext(userService.getMyUserWithAuthorities().get(), searchDto.getSearchText());
        //그 아이디에 검색할 해시태그가 있는지 확인 hash가 null이면 없음

        hashTagService.hitsUp(hash.getId());

        if(hash==null){ //그 유저에게 없는 해시태그를 검색했을 때 잘못된 요청
            return new ResponseEntity(DefaultResNoResult.res(StatusCode.HASH_FOUND_RESULT_NOT_FOUND, ResponseMessage.HASH_FOUND_RESULT_NOT_FOUND),httpHeaders,  HttpStatus.OK);
        }
//        같은 검색 내역이 있으면 시간만 갱신하려고했는데 이럴려면 search의 아이디값도 같이 넣어줘야함 고민 좀 해보겠음....
        List<Search> searches = searchRepository.findByUser(userService.getMyUserWithAuthorities().get()); //그 유저의 모든 검색 결과


        for(int i=0; i<searches.size();i++){
            System.out.println("search.get ::: "+searches.get(i).getHash().getId());
            System.out.println("hash가 있으면 hash테이블의 hash_id를 찾음/ hash id ::: "+hash.getId()); //hash가 있으면 hash테이블의 hash_id를 찾음
            System.out.println("그 유저의 모든 검색 결과의 아이디/ searchID ::::  "+searches.get(i).getId()); //그 유저의 모든 검색 결과의 아이디
            if(searches.get(i).getHash().getId().equals(hash.getId())){ //모든 검색 결과의 해시 아이디와 내가 검색결과로 넣을 hashtag의 id가 같은지
                searchDto.setId(searches.get(i).getId());
                Search search = searchService.update(searches.get(i).getId(),searchDto);
                map.put("hash_text",search.getHash().getHashtext());
            }
        }
        if(map.get("hash_text")==null){
            searchDto.setSearchText(hash.getHashtext());
            Search search = searchService.save( searchDto, hash.getId() );
            map.put("hash_text",search.getHash().getHashtext());

        }

        HashTag hashTags = hashTagRepository
                .findOneByUserAndHashtext(userService.getMyUserWithAuthorities().get(),map.get("hash_text").toString());
        List<QuoteHash> quoteHashes = quoteHashRepository.findByHashid(hashTags);
        List<QuoteDto> result = new ArrayList<>();


        for(int i=0;i<quoteHashes.size();i++){
            QuoteDto quoteDto = new QuoteDto();
            List<String> hashList = quoteHashService.findHashByQuote(quoteHashes.get(i).getQuoteid());
            BeanUtils.copyProperties(quoteHashes.get(i).getQuoteid(),quoteDto);
            quoteDto.setHashList(hashList);
            quoteDto.setUserEmail(userService.getMyUserWithAuthorities().get().getEmail());
            result.add(quoteDto);
        }
        return new ResponseEntity(DefaultRes.res(StatusCode.OK, ResponseMessage.HASH_FOUND_RESULT_SUCCESS, result),httpHeaders,  HttpStatus.OK);


    }


    @GetMapping("/info") //최근검색 뿌리기
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity info(@RequestParam String count ){
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", "application/json;charset=UTF-8");

        List<Object> resultMap = new ArrayList<>();
        String result = "검색 결과가 없습니다.";
        List<Search> searches;
        if(count.equals("result5")){
            searches = searchRepository.findTop5ByUserOrderByDateDesc(userService.getMyUserWithAuthorities().get());
        }else{
            searches = searchRepository.findTop10ByUserOrderByDateDesc(userService.getMyUserWithAuthorities().get());
        }
        List<String> resultlst = new ArrayList<>();
        for(int i=0;i<searches.size();i++){
            Map map= new HashMap();
            HashTag hash= searches.get(i).getHash();
            map.put("search_id",searches.get(i).getId());
            map.put("hash_text",hash.getHashtext());
            resultlst.add(hash.getHashtext());
            resultMap.add(map);
        }
        return new ResponseEntity(DefaultRes.res(StatusCode.OK, ResponseMessage.READ_SEARCH,resultMap),httpHeaders,  HttpStatus.OK);
    }


    @DeleteMapping("/delete/{search_id}")  // 검색내역 삭제
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity delete(@PathVariable  Long search_id) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", "application/json;charset=UTF-8");

        searchRepository.delete(searchRepository.findById(search_id).get());
        return new ResponseEntity(DefaultResNoResult.res(StatusCode.OK, ResponseMessage.SEARCH_DELETE_SUCCESS),httpHeaders, HttpStatus.OK);
    }


}
