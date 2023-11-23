package com.users.users.controller;

import com.users.users.dto.Search;
import com.users.users.model.CustomUser;
import com.users.users.service.CommentsService;
import com.users.users.service.PostService;
import com.users.users.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.users.users.dto.UserDTO;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private PostService postService;

    @Autowired
    private CommentsService commentsService;

    @Autowired
    private PostController postController;



    @GetMapping("/admin")
    @PreAuthorize("hasRole('ROLE_Admin')")
    public String admin(Model model){
        if(postController.checkAuth()) return "redirect:/logout";
        List<UserDTO> users = userService.findAll().stream()
                .filter(user -> user.getRole().equals("User"))
                .collect(Collectors.toList());
        model.addAttribute("allUsers", users);
        model.addAttribute("search", new Search());
        return "site/admin";
    }

    @GetMapping("/admin/remove/{id}")
    @PreAuthorize("hasRole('ROLE_Admin')")
    public String remove(@PathVariable Integer id) {
        if(postController.checkAuth()) return "redirect:/login";
        userService.findById(id).ifPresent(user -> commentsService.deleteByName(user.getName()));
        postService.deleteByIndex(id);
        userService.deleteById(id);
        return "redirect:/admin";
    }

    @GetMapping("/searchuser")
    @PreAuthorize("hasRole('ROLE_Admin')")
    public String searchUser(@ModelAttribute("search") Search search, Model model){
        if(postController.checkAuth()) return "redirect:/login";
        return userService.findUser(search, model);
    }

}
