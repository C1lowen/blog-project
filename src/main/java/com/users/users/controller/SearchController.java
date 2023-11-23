package com.users.users.controller;

import com.users.users.dto.PostSearchDTO;
import com.users.users.dto.Search;
import com.users.users.model.Post;
import com.users.users.service.PostService;
import com.users.users.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class SearchController {
    @Autowired
    private SearchService searchService;
    @Autowired
    private PostController postController;
    @PostMapping("/fingpost")
    public String findPost(@ModelAttribute("request") Search search, Model model){
        if(postController.checkAuth()) return "redirect:/logout";
        if(search.getName().isEmpty()) return "redirect:/";
        model.addAttribute("searchlist", searchService.search(search.getName()));
        return "site/index";
    }
}
