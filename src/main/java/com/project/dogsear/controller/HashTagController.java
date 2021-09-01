package com.project.dogsear.controller;

import com.project.dogsear.domain.*;
import com.project.dogsear.dto.HashReturnDto;
import com.project.dogsear.dto.HashTagDto;
import com.project.dogsear.dto.QuoteDto;
import com.project.dogsear.dto.QuotePageDto;
import com.project.dogsear.service.HashTagService;
import com.project.dogsear.service.QuoteHashService;
import com.project.dogsear.service.UserService;
import com.project.dogsear.status.DefaultRes;
import com.project.dogsear.status.DefaultResNoResult;
import com.project.dogsear.status.ResponseMessage;
import com.project.dogsear.status.StatusCode;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Sort;
import java.util.*;

@AllArgsConstructor
@RestController
@RequestMapping("/hash")
public class HashTagController {
    private final HashTagRepository hashTagRepository;
    private final UserService userService;
    private final HashTagService hashTagService;
    private final QuoteHashService quoteHashService;
    private final QuoteHashRepository quoteHashRepository;
    private final QuoteRepository quoteRepository;
    private static final int HITS_HASH_LENGTH = 10;
    HashPageRepositiry hashPageRepositiry;

    @PostMapping("/save")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity write(@RequestBody HashTagDto hashTagDto){
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", "application/json;charset=UTF-8");
        String result="이미 등록된 해시태그입니다.";
        List<HashTag> hashTags = hashTagRepository.findByUser(userService.getMyUserWithAuthorities().get());
        List<String> hashtxts = new ArrayList<>();
        for(int i=0;i<hashTags.size();i++){
            hashtxts.add(hashTags.get(i).getHashtext());
        }
        if(hashtxts.contains(hashTagDto.getHash_text())){
            return new ResponseEntity(DefaultResNoResult.res(StatusCode.HASH_EDIT_USED_NAME, ResponseMessage.HASH_EDIT_USED_NAME),httpHeaders,  HttpStatus.OK );
        }
        if(hashTagDto.getHash_text().equals("")||hashTagDto.getHash_text()==null){
            return new ResponseEntity(DefaultResNoResult.res(StatusCode.FAIL_HASH_TEXT_NULL,ResponseMessage.FAIL_HASH_TEXT_NULL),httpHeaders,HttpStatus.OK);
        }
        hashTagDto.setUserEmail(userService.getMyUserWithAuthorities().get().getEmail());
        if(hashTagService.save(hashTagDto)!=null) {
            result="해시태그가 저장되었습니다.";
            return new ResponseEntity(DefaultResNoResult.res(StatusCode.OK, ResponseMessage.CREATED_HASHTAG),httpHeaders,  HttpStatus.OK);
        }
        result="저장 오류입니다.";
        return new ResponseEntity(DefaultResNoResult.res(StatusCode.HASH_CREATE_FAIL, ResponseMessage.HASH_CREATE_FAIL),httpHeaders,  HttpStatus.OK );
    }

    @DeleteMapping("/delete/{hash_id}")  // 해시태그 삭제
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity delete(@PathVariable  Long hash_id) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", "application/json;charset=UTF-8");

        if(hashTagRepository.findById(hash_id).isPresent()){
            quoteHashService.hashDelete(hashTagRepository.findById(hash_id).get());
            hashTagRepository.delete(hashTagRepository.findById(hash_id).get());
            return new ResponseEntity(DefaultResNoResult.res(StatusCode.OK, ResponseMessage.HASH_DELETE_SUCCESS),httpHeaders,  HttpStatus.OK);
        }
        return new ResponseEntity(DefaultResNoResult.res(StatusCode.FAIL_TO_FIND_HASHTAG,ResponseMessage.FAIL_TO_FIND_HASHTAG),httpHeaders,HttpStatus.OK);
    }

    //인용구 작성할때 뿌려줄 hash 키워드로 검색
    @GetMapping("/select")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity search_for_quote(@RequestParam(value = "hash_text") String hash_text){
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", "application/json;charset=UTF-8");

        List<Object> result = new ArrayList<>();
        if(!hash_text.equals("")||hash_text!=null){
            List<HashTag> Hashtags = hashTagRepository.findByUserAndHashtextStartingWith(userService.getMyUserWithAuthorities().get(),hash_text);
            for(int i=0;i<Hashtags.size();i++){
                Map map = new HashMap<String,Object>();
                map.put("hash_text",Hashtags.get(i).getHashtext());
                map.put("count",quoteHashService.CountByHashid(Hashtags.get(i)));
                result.add(map);
                //map.put("count",quoteHashService.CountByHashid(Hashtags.get(i)) );

            }

            return new ResponseEntity(DefaultRes.res(StatusCode.OK, ResponseMessage.HASH_FOUND_RESULT,result),httpHeaders,  HttpStatus.OK);
        }
        return new ResponseEntity(DefaultRes.res(StatusCode.OK, ResponseMessage.HASH_FOUND_NO_RESULT,result),httpHeaders,  HttpStatus.OK);
    }

    @GetMapping("/info/all") //*****위에거 수정 **********
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity infoAll(){
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", "application/json;charset=UTF-8");

        List<HashReturnDto> result = hashTagRepository.findByUser(userService.getMyUserWithAuthorities().get().getUserId());
        return new ResponseEntity(DefaultRes.res(StatusCode.OK, ResponseMessage.READ_HASHTAG,result), httpHeaders, HttpStatus.OK);
    }

    @PatchMapping("/bookmark/set/{hash_id}") //유저 즐겨찾는 태그 설정 북마크
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity setBookmark(@PathVariable Long hash_id){

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", "application/json;charset=UTF-8");
        if(hashTagRepository.findById(hash_id).isPresent()){
            hashTagService.hashBookmarkSet(hash_id);
            return new ResponseEntity(DefaultResNoResult.res(StatusCode.OK, ResponseMessage.HASH_BOOKMARK_SET),httpHeaders,  HttpStatus.OK);
        }
        return new ResponseEntity(DefaultResNoResult.res(StatusCode.FAIL_TO_FIND_HASHTAG, ResponseMessage.FAIL_TO_FIND_HASHTAG),httpHeaders,  HttpStatus.OK);
    }


    @GetMapping("/info/bookmark") //유저 즐겨찾는 태그 보기
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity infoBookmark(){
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", "application/json;charset=UTF-8");

        List<HashReturnDto> result = hashTagRepository.findByUserAndBookmark(userService.getMyUserWithAuthorities().get());
        return new ResponseEntity(DefaultRes.res(StatusCode.OK, ResponseMessage.HASH_BOOKMARK_FOUND_SUCCESS,result),httpHeaders, HttpStatus.OK);
    }

    @PatchMapping("/edit/{hash_id}")  // 해시태그 수정~~ ^^
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity hashEdit(@PathVariable Long hash_id,@RequestBody Map hash_text){
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", "application/json;charset=UTF-8");

        if(hashTagRepository.findById(hash_id).isPresent()){
            String result = hashTagService.hashTextEdit(userService.getMyUserWithAuthorities().get(),hash_id,hash_text.get("hash_text").toString());
            if(result.equals("변경 성공")){
                return new ResponseEntity(DefaultResNoResult.res(StatusCode.OK, ResponseMessage.HASH_EDITED),httpHeaders,  HttpStatus.OK);
            }else {
                return new ResponseEntity(DefaultResNoResult.res(StatusCode.HASH_EDIT_USED_NAME, ResponseMessage.HASH_EDIT_USED_NAME),httpHeaders,  HttpStatus.OK);
            }
        }
        return new ResponseEntity(DefaultResNoResult.res(StatusCode.FAIL_TO_FIND_HASHTAG, ResponseMessage.FAIL_TO_FIND_HASHTAG),httpHeaders,  HttpStatus.OK);
    }

    @GetMapping("/search/{hash_id}")  // *********위에거 수정**********
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity hashSearch(@PathVariable Long hash_id){
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", "application/json;charset=UTF-8");

        if(hashTagRepository.findById(hash_id).isPresent()){
            hashTagService.hitsUp(hash_id); // 검색시 hits 올리기
            List<QuotePageDto> result = quoteHashRepository.findByHashidJoinWithQuote(hash_id);

            return new ResponseEntity(DefaultRes.res(StatusCode.OK, ResponseMessage.HASHTAG_TO_QUOTE, result),httpHeaders,  HttpStatus.OK);
        }
        return new ResponseEntity(DefaultResNoResult.res(StatusCode.FAIL_TO_FIND_HASHTAG, ResponseMessage.FAIL_TO_FIND_HASHTAG),httpHeaders,  HttpStatus.OK);
    }

    @GetMapping("/search")  // ********** 위에거 수정 *************
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity hashSearchWithText(@RequestParam(value = "hash_text") String hash_text){
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", "application/json;charset=UTF-8");

        List<QuotePageDto> result = quoteHashRepository.findByHashidJoinWithQuote(hash_text);
        HashTag hashTag = hashTagRepository.findOneByUserAndHashtext(userService.getMyUserWithAuthorities().get(),hash_text);
        if(hashTag!=null){
            hashTagService.hitsUp(hashTag.getId());
        }
        return new ResponseEntity(DefaultRes.res(StatusCode.OK, ResponseMessage.HASHTAG_TO_QUOTE, result),httpHeaders, HttpStatus.OK);
    }

    @GetMapping("/info/top/3") //위에거 수정
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity useTopHash3(){
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", "application/json;charset=UTF-8");

        List<Object> result = quoteRepository.findByUserBest3(userService.getMyUserWithAuthorities().get().getUserId());
        return new ResponseEntity(DefaultRes.res(StatusCode.OK, ResponseMessage.READ_HASHTAG_TOP3,result),httpHeaders,  HttpStatus.OK);
    }

    @GetMapping("/info/hits") // ******* 위에거 수정 **********
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity hashHitsInfo(){
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", "application/json;charset=UTF-8");

        PageRequest pageRequest = PageRequest.of(0, HITS_HASH_LENGTH, Sort.by(Sort.Direction.DESC,"hits"));
        List<HashReturnDto> result = hashPageRepositiry.findByUserAndHits(userService.getMyUserWithAuthorities().get().getUserId(),pageRequest);

        return new ResponseEntity(DefaultRes.res(StatusCode.OK, ResponseMessage.HASH_HITS_FOUND_SUCCESS,result),httpHeaders,  HttpStatus.OK);
    }

    @GetMapping("/info/recent") // 최근 사용한 해시태그
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity recentlyUsedHash(){
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", "application/json;charset=UTF-8");

        List<String> result = hashTagRepository.findByUserAndDateDesc(userService.getMyUserWithAuthorities().get().getUserId());

        return new ResponseEntity(DefaultRes.res(StatusCode.OK, ResponseMessage.HASH_RECENTLY_USED_ROAD_SUCCESS,result),httpHeaders,  HttpStatus.OK);
    }


}