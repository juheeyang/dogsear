package com.project.dogsear.controller;

import com.project.dogsear.domain.User;
import com.project.dogsear.domain.UserRepository;
import com.project.dogsear.dto.UserDto;
import com.project.dogsear.service.MailService;
import com.project.dogsear.service.UserService;
import com.project.dogsear.status.DefaultRes;
import com.project.dogsear.status.DefaultResNoResult;
import com.project.dogsear.status.ResponseMessage;
import com.project.dogsear.status.StatusCode;

import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;


import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private String server = "/";
    private String local = "\\";
    private String slash = "";



    public UserController(UserService userService, MailService mailService, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }


    @PostMapping("/save")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity uploadSingle( @RequestBody UserDto userDto) throws Exception {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", "application/json;charset=UTF-8");

        String saveUser = userService.signup(userDto);
        if(saveUser.equals("닉네임 null값")){
            return new ResponseEntity(DefaultResNoResult.res(StatusCode.FAIL_NICKNAME_NULL, ResponseMessage.FAIL_NICKNAME_NULL),httpHeaders,  HttpStatus.OK);
        }
        if(saveUser.equals("이메일 null값")){
            return new ResponseEntity(DefaultResNoResult.res(StatusCode.FAIL_EMAIL_NULL, ResponseMessage.FAIL_EMAIL_NULL),httpHeaders,  HttpStatus.OK);
        }
        if(saveUser.equals("비밀번호 확인이 일치하지 않습니다.")){
            return new ResponseEntity(DefaultResNoResult.res(StatusCode.FAIL_CREATED_USER_INCORRECT_PASSWORD_CHECK, ResponseMessage.FAIL_CREATED_USER_INCORRECT_PASSWORD_CHECK),httpHeaders,  HttpStatus.OK);
        }
        if(saveUser.equals("비밀번호 형식 오류")){
            return new ResponseEntity(DefaultResNoResult.res(StatusCode.FAIL_INCORRECT_PASSWORD_FORMAT, ResponseMessage.FAIL_INCORRECT_PASSWORD_FORMAT),httpHeaders,  HttpStatus.OK);
        }
        if (saveUser.equals("이미 가입되어 있는 유저입니다."))
            return new ResponseEntity(DefaultResNoResult.res(StatusCode.FAIL_CREATED_USER_SIGNED_EMAIL, ResponseMessage.FAIL_CREATED_USER_SIGNED_EMAIL),httpHeaders,  HttpStatus.OK); //이미 존재할때
        if (saveUser.equals("잘못된 이메일 형식입니다."))
            return new ResponseEntity(DefaultResNoResult.res(StatusCode.FAIL_CREATED_USER_INCORRECT_EMAIL_FORMAT, ResponseMessage.FAIL_CREATED_USER_INCORRECT_EMAIL_FORMAT),httpHeaders,  HttpStatus.OK); //잘못된 이메일
        return new ResponseEntity(DefaultResNoResult.res(StatusCode.OK, ResponseMessage.CREATED_USER ),httpHeaders,  HttpStatus.OK); //제대로 회원가입 성공
    }


    @PatchMapping("/edit/nickname")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity nicknameEdit(@RequestBody Map userInfo){
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", "application/json;charset=UTF-8");

        String result = "변경된 내용이 없습니다.";

        if(userInfo.get("nickname") != null && !userInfo.get("nickname").equals("")){
            System.out.println(userInfo.get("nickname").toString());
            result = userService.nicknameEdit(userService.getMyUserWithAuthorities().get().getEmail(),userInfo.get("nickname").toString());
            return new ResponseEntity(DefaultResNoResult.res(StatusCode.OK, ResponseMessage.UPDATE_USER), HttpStatus.OK);
        }
        return new ResponseEntity(DefaultResNoResult.res(StatusCode.FAIL_UPDATE_USER_NULL_NICKNAME, ResponseMessage.FAIL_UPDATE_USER_NULL_NICKNAME),httpHeaders,  HttpStatus.OK);

    }

    @PatchMapping("/edit/password")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity passwordEdit(@RequestBody Map userInfo){
        String result = "변경된 내용이 없습니다.";

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", "application/json;charset=UTF-8");

        String pw_pattern = "^[A-Za-z[0-9]]{10,20}$";
        Matcher match;
        match = Pattern.compile(pw_pattern).matcher(userInfo.get("password_check").toString());


        System.out.println("password_check::"+userInfo.get("password_check").toString());
        System.out.println("new_password::"+userInfo.get("new_password").toString());
        System.out.println("old_password::"+ userInfo.get("old_password"));
        System.out.println("old_password(EncodedMatches)::"+passwordEncoder.matches(userInfo.get("old_password").toString(),userService.getMyUserWithAuthorities().get().getPassword()));
        System.out.println("old_password(Encoded)::"+passwordEncoder.encode(userInfo.get("old_password").toString()));
        System.out.println("토큰에 담겨있던 객체의 비밀번호::::"+userService.getMyUserWithAuthorities().get().getPassword() );


        if(!match.find()) {
            return new ResponseEntity<>(DefaultResNoResult.res(StatusCode.FAIL_INCORRECT_PASSWORD_FORMAT, ResponseMessage.FAIL_INCORRECT_PASSWORD_FORMAT), HttpStatus.OK);
        }

        if(!userInfo.get("new_password").equals(userInfo.get("password_check"))){
            return new ResponseEntity<>(DefaultResNoResult.res(StatusCode.FAIL_CREATED_USER_INCORRECT_PASSWORD_CHECK, ResponseMessage.FAIL_CREATED_USER_INCORRECT_PASSWORD_CHECK), HttpStatus.OK);
        }

        if(!passwordEncoder.matches(userInfo.get("old_password").toString(),userService.getMyUserWithAuthorities().get().getPassword())){
            return new ResponseEntity<>(DefaultResNoResult.res(StatusCode.OLD_PASSWORD_INCORRECT, ResponseMessage.OLD_PASSWORD_INCORRECT),  HttpStatus.OK);
        }

        if(userInfo.get("new_password") != null){
            result = userService.passwordEdit(userService.getMyUserWithAuthorities().get().getEmail(),userInfo.get("new_password").toString());
            return new ResponseEntity<>(DefaultResNoResult.res(StatusCode.OK, ResponseMessage.UPDATE_USER), HttpStatus.OK);
        }
        DefaultResNoResult def= new DefaultResNoResult(StatusCode.FAIL_UPDATE_USER,ResponseMessage.FAIL_UPDATE_USER);
        return new ResponseEntity<>(def,httpHeaders,  HttpStatus.OK);

//        return new ResponseEntity(DefaultResNoResult.res(StatusCode.BAD_REQUEST, ResponseMessage.FAIL_UPDATE_USER),  HttpStatus.BAD_REQUEST);
//        return new ResponseEntity<>(DefaultResNoResult.res(StatusCode.BAD_REQUEST, ResponseMessage.FAIL_UPDATE_USER),httpHeaders, HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity userDelete(){
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", "application/json;charset=UTF-8");

        String result ="";
        result = userService.deleteUser(userService.getMyUserWithAuthorities().get().getUserId());
        if(result.equals("회원 탈퇴 성공") )
            return new ResponseEntity(DefaultResNoResult.res(StatusCode.OK, ResponseMessage.DELETE_USER),httpHeaders,  HttpStatus.OK);
        return new ResponseEntity(DefaultResNoResult.res(StatusCode.FAIL_DELETE_USER, ResponseMessage.FAUL_DELETE_USER ),httpHeaders,  HttpStatus.OK);
    }

    @GetMapping("/info")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity userInfo() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", "application/json;charset=UTF-8");

        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(userService.getMyUserWithAuthorities().get(),userDto);
        return new ResponseEntity(DefaultRes.res(StatusCode.OK, ResponseMessage.READ_USER , userDto),httpHeaders,  HttpStatus.OK);
    }



    @GetMapping("/user")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<User> getMyUserInfo() {
        return ResponseEntity.ok(userService.getMyUserWithAuthorities().get());
    }

    @GetMapping("/user/{username}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<User> getUserInfo(@PathVariable String username) {
        return ResponseEntity.ok(userService.getUserWithAuthorities(username).get());
    }

    @PatchMapping("/image")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity editImage(@RequestBody Map<String,Integer> userInfo){
        if(userInfo.get("image")!=null && userInfo.get("image")!=0){
            userService.editImage(userService.getMyUserWithAuthorities().get().getUserId(),userInfo.get("image"));
            return new ResponseEntity(DefaultResNoResult.res(StatusCode.OK, ResponseMessage.SUCCESS_EDIT_IMAGE),HttpStatus.OK);
        }
        return new ResponseEntity(DefaultResNoResult.res(StatusCode.BAD_REQUEST, ResponseMessage.FAIL_UPDATE_USER_IMAGE_NOT_SELECTED),HttpStatus.OK);

    }




}
