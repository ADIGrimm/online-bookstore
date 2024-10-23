package online.bookstore.service.impl;

import java.util.Set;
import lombok.RequiredArgsConstructor;
import online.bookstore.dto.user.UserRegistrationRequestDto;
import online.bookstore.dto.user.UserResponseDto;
import online.bookstore.mapper.UserMapper;
import online.bookstore.model.Role;
import online.bookstore.model.User;
import online.bookstore.repository.role.RoleRepository;
import online.bookstore.repository.user.UserRepository;
import online.bookstore.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Override
    @Transactional
    public UserResponseDto registration(UserRegistrationRequestDto requestDto) {
        User user = userMapper.toModel(requestDto);
        user.setPassword(passwordEncoder.encode(requestDto.getPassword()));
        user.setRoles(Set.of(roleRepository.findByName(Role.RoleName.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Can't find role"))));
        return userMapper.toDto(userRepository.save(user));
    }
}
