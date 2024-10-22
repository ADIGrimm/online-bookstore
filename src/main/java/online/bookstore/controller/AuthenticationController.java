package online.bookstore.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import online.bookstore.dto.user.UserRegistrationRequestDto;
import online.bookstore.dto.user.UserResponseDto;
import online.bookstore.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    private final UserService userService;

    @PostMapping("/registration")
    public UserResponseDto createBook(@Valid @RequestBody UserRegistrationRequestDto requestDto) {
        return userService.registration(requestDto);
    }

}
