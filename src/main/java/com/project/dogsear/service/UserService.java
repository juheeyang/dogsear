package com.project.dogsear.service;

import com.project.dogsear.domain.*;
import com.project.dogsear.dto.UserDto;
import com.project.dogsear.util.SecurityUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final QuoteRepository quoteRepository;
    private final HashTagRepository hashTagRepository;
    private final QuoteHashRepository quoteHashRepository;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, QuoteRepository quoteRepository, HashTagRepository hashTagRepository, QuoteHashRepository quoteHashRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.quoteRepository = quoteRepository;
        this.hashTagRepository = hashTagRepository;
        this.quoteHashRepository = quoteHashRepository;
    }

    @Transactional
    public String signup(UserDto userDto) {
        String result;
        result = "";
        String regExp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";

        if(userDto.getEmail()==null|| userDto.getEmail().equals("")){
            result="이메일 null값";
            return result;
        }
        if(userDto.getNickname()==null||userDto.getNickname().equals("")){
            result="닉네임 null값";
            return result;
        }
        if(!userDto.getPassword().equals(userDto.getPassword_check())){
            result =  "비밀번호 확인이 일치하지 않습니다.";
            return result;
        }
        String pw_pattern = "^[A-Za-z[0-9]]{10,20}$";
        Matcher match;
        match = Pattern.compile(pw_pattern).matcher(userDto.getPassword());
        if(!match.find()) {
            result = "비밀번호 형식 오류";
            return result;
        }

        if( !Pattern.matches(regExp, userDto.getEmail()) ){ //이메일 형식 검사
            result = "잘못된 이메일 형식입니다.";
            return result;
        }
        if (userRepository.findOneWithAuthoritiesByEmail(userDto.getEmail()).orElse(null) != null) {
            result = "이미 가입되어 있는 유저입니다.";
        }
        else{
            //빌더 패턴의 장점
            Authority authority = Authority.builder()
                    .authorityName("ROLE_USER")
                    .build();

            User user = User.builder()
                    .email(userDto.getEmail())
                    .password(passwordEncoder.encode(userDto.getPassword()))
                    .nickname(userDto.getNickname())
                    .image(userDto.getImage())
                    .authorities(Collections.singleton(authority))
                    .activated(true)
                    .build();

            userRepository.save(user);
            result = "회원가입 성공";
        }
        return result;
    }


    @Transactional
    public String passwordEdit(String email,String password){

        userRepository.findOneWithAuthoritiesByEmail(email).get().setPassword(passwordEncoder.encode(password));
        return "비밀번호가 변경되었습니다.";
    }

    @Transactional
    public String nicknameEdit(String email, String nickname){
        userRepository.findOneWithAuthoritiesByEmail(email).get().setNickname(nickname);
        return "닉네임이 변경되었습니다.";
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserWithAuthorities(String username) {
        return userRepository.findOneWithAuthoritiesByEmail(username);
    }

    @Transactional(readOnly = true)
    public Optional<User> getMyUserWithAuthorities() {
        return SecurityUtil.getCurrentEmail().flatMap(userRepository::findOneWithAuthoritiesByEmail);
    }


    @Transactional
    public String deleteUser(Long id){
        quoteHashRepository.deleteQuoteHashByUserId(id);
        quoteRepository.deleteQuoteByUserId(id);
        hashTagRepository.deleteHashByUserId(id);

        userRepository.deleteById(id);
        return "회원 탈퇴 성공";
    }

    public void editImage(Long id, Integer image){
        if(image!=0){
            userRepository.editImageById(image,id);
        }
        else{
            userRepository.editImageById(null,id);
        }
    }

}
