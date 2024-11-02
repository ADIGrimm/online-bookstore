package online.bookstore.service.impl;

import java.util.Set;
import lombok.RequiredArgsConstructor;
import online.bookstore.dto.user.UserRegistrationRequestDto;
import online.bookstore.dto.user.UserResponseDto;
import online.bookstore.exception.EntityNotFoundException;
import online.bookstore.exception.RegistrationException;
import online.bookstore.mapper.UserMapper;
import online.bookstore.model.Role;
import online.bookstore.model.ShoppingCart;
import online.bookstore.model.User;
import online.bookstore.repository.cart.ShoppingCartRepository;
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
    private final ShoppingCartRepository shoppingCartRepository;

    @Override
    @Transactional
    public UserResponseDto registration(UserRegistrationRequestDto requestDto) {
        if (userRepository.existsByEmail(requestDto.getEmail())) {
            throw new RegistrationException(
                    String.format("User with this email: %s already exists", requestDto.getEmail()
                    ));
        }
        User user = userMapper.toModel(requestDto);
        user.setPassword(passwordEncoder.encode(requestDto.getPassword()));
        user.setRoles(Set.of(roleRepository.findByName(Role.RoleName.ROLE_USER)
                .orElseThrow(() -> new EntityNotFoundException("Can't find role"))));
        ShoppingCart cart = new ShoppingCart();
        cart.setUser(user);
        shoppingCartRepository.save(cart);
        return userMapper.toDto(userRepository.save(user));
    }
}
