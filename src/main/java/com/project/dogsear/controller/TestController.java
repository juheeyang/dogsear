package com.project.dogsear.controller;


import com.project.dogsear.dto.UserWithImageDto;
import com.project.dogsear.service.UserService;
import com.project.dogsear.status.DefaultRes;
import com.project.dogsear.status.ResponseMessage;
import com.project.dogsear.status.StatusCode;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.boot.autoconfigure.mongo.MongoProperties.DEFAULT_URI;

@RestController
@AllArgsConstructor
public class TestController {

    private final UserService userService;

//    @PostMapping("/file")
//    @ResponseStatus(HttpStatus.CREATED)
//    public List<String> upload(@RequestPart List<MultipartFile> files) throws Exception {
//        List<String> list = new ArrayList<>();
//        for (MultipartFile file : files) {
//            String originalfileName = file.getOriginalFilename();
//            File dest = new File("C:/Image/" + originalfileName);
//            file.transferTo(dest);
//            // TODO
//        }
//        return list;
//    }
//
//    @PostMapping("/single")
//    @ResponseStatus(HttpStatus.CREATED)
//    public String uploadSingle(@ModelAttribute UserWithImageDto user) throws Exception {
//        String rootPath = FileSystemView.getFileSystemView().getHomeDirectory().toString();
//        String basePath = rootPath + "/" + "single";
//        String filePath = basePath + "/" + user.getProfileImage().getOriginalFilename();
//        File dest = new File(filePath);
//        user.getProfileImage().transferTo(dest); // 파일 업로드 작업 수행
//
//        user.getUserDto().setImage(filePath);
//
//        String saveUser =  userService.signup(user.getUserDto());
//        return saveUser;
//    }
}
