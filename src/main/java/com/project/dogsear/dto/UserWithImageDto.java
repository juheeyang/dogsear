package com.project.dogsear.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class UserWithImageDto {
    private MultipartFile profileImage;
    private UserDto userDto;
}
