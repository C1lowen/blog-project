package com.users.users.controller;
import com.users.users.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class CustomLogoutHandler implements LogoutHandler {

    private final UserService userService;

    public CustomLogoutHandler(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void logout(jakarta.servlet.http.HttpServletRequest request, jakarta.servlet.http.HttpServletResponse response, Authentication authentication) {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();

        // Проверка, существует ли пользователь в базе данных
        if (userService.findByName(name).isEmpty()) {
            // Выход пользователя из системы
            SecurityContextHolder.clearContext();
        }
    }
}
