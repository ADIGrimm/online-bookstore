package online.bookstore.service;

import online.bookstore.dto.user.UserRegistrationRequestDto;
import online.bookstore.dto.user.UserResponseDto;

public interface UserService {
    UserResponseDto registration(UserRegistrationRequestDto requestDto);
}
