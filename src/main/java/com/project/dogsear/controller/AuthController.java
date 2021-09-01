package com.project.dogsear.controller;

import com.project.dogsear.domain.User;
import com.project.dogsear.domain.UserRepository;
import com.project.dogsear.dto.LoginDto;
import com.project.dogsear.dto.TokenDto;
import com.project.dogsear.jwt.JwtFilter;
import com.project.dogsear.jwt.TokenProvider;
import com.project.dogsear.status.DefaultRes;
import com.project.dogsear.status.DefaultResNoResult;
import com.project.dogsear.status.ResponseMessage;
import com.project.dogsear.status.StatusCode;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(TokenProvider tokenProvider, AuthenticationManagerBuilder authenticationManagerBuilder, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.tokenProvider = tokenProvider;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @PostMapping("/login")
    public ResponseEntity login(@Valid @RequestBody LoginDto loginDto) {
        //Map result = new HashMap<String, Object>();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", "application/json;charset=UTF-8");
        String resultm = "이메일과 비밀번호가 일치하지 않습니다.";
        List<User> userList = userRepository.findAll();
        String email = loginDto.getEmail();
        String password = loginDto.getPassword();
        String regExp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";

        if( !Pattern.matches(regExp, email) ){ //이메일 형식 검사
            resultm = "잘못된 이메일 형식입니다.";
            return new ResponseEntity(DefaultResNoResult.res(StatusCode.FAIL_CREATED_USER_INCORRECT_EMAIL_FORMAT, ResponseMessage.FAIL_CREATED_USER_INCORRECT_EMAIL_FORMAT),httpHeaders,  HttpStatus.OK); //이메일형식 잘못됐을때
        }
        for(int i=0;i<userList.size();i++){
            if((userList.get(i).getEmail().equals(email))&&(passwordEncoder.matches(password,userList.get(i).getPassword()))){
                resultm = "로그인 성공";
            }
        }

        if (resultm.equals("로그인 성공")){
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword());

            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            String jwt = tokenProvider.createToken(authentication);


            httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);

            //result.put("token", new TokenDto(jwt));
            return new ResponseEntity(DefaultRes.res(StatusCode.OK, ResponseMessage.LOGIN_SUCCESS, new TokenDto(jwt)),httpHeaders,  HttpStatus.OK);
        }
        return new ResponseEntity(DefaultResNoResult.res(StatusCode.LOGIN_FAIL_MISMATCH_INFO, ResponseMessage.LOGIN_FAIL_MISMATCH_INFO),httpHeaders,  HttpStatus.OK);
    }
}
