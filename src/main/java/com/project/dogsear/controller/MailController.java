package com.project.dogsear.controller;

import com.project.dogsear.domain.UserRepository;
import com.project.dogsear.dto.MailDto;
import com.project.dogsear.service.MailService;
import com.project.dogsear.service.UserService;
import com.project.dogsear.status.DefaultRes;
import com.project.dogsear.status.DefaultResNoResult;
import com.project.dogsear.status.ResponseMessage;
import com.project.dogsear.status.StatusCode;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.MalformedParameterizedTypeException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

@RestController
@AllArgsConstructor
public class MailController {
    private final MailService mailService;
    private final UserRepository userRepository;
    private final UserService userService;

    @PostMapping("/sendmail")
    public ResponseEntity sendEmail(@RequestBody Map userInfo){
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", "application/json;charset=UTF-8");

        String result = "가입되어 있지 않은 이메일 입니다.";
        String userEmail = userInfo.get("email").toString();
        String regExp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";

        if( !Pattern.matches(regExp, userEmail) ){ //이메일 형식 검사
            return new ResponseEntity(DefaultResNoResult.res(StatusCode.FAIL_CREATED_USER_INCORRECT_EMAIL_FORMAT, ResponseMessage.FAIL_CREATED_USER_INCORRECT_EMAIL_FORMAT),httpHeaders,  HttpStatus.OK);
        }

        if (userRepository.findOneWithAuthoritiesByEmail(userEmail).orElse(null) != null) {
            String userName = userRepository.findOneWithAuthoritiesByEmail(userEmail).get().getNickname();
            MailDto dto = mailService.createMail(userEmail,userName);
            mailService.mailSend(dto);
            System.out.println(mailService.ePw);
            result = userService.passwordEdit(userEmail,mailService.ePw);
            return new ResponseEntity(DefaultResNoResult.res(StatusCode.OK, ResponseMessage.SEND_EMAIL), HttpStatus.OK);
        }else{
            return new ResponseEntity(DefaultResNoResult.res(StatusCode.FAIL_SEND_EMAIL, ResponseMessage.FAIL_SEND_EMAIL),httpHeaders,  HttpStatus.OK);
        }

    }
}
