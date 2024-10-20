package online.bookstore.service.impl;

import lombok.RequiredArgsConstructor;
import online.bookstore.dto.user.UserRegistrationRequestDto;
import online.bookstore.dto.user.UserResponseDto;
import online.bookstore.mapper.UserMapper;
import online.bookstore.model.User;
import online.bookstore.repository.user.UserRepository;
import online.bookstore.service.UserService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserResponseDto registration(UserRegistrationRequestDto requestDto) {
        User user = userMapper.toModel(requestDto);
        return userMapper.toDto(userRepository.save(user));
    }
}
