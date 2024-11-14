package online.bookstore.controller;

import online.bookstore.model.User;
import org.springframework.security.core.Authentication;

public interface UserContextHelper {
    default Long getUserId(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return user.getId();
    }
}
