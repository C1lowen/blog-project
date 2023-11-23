package com.users.users.controller;

import com.users.users.dto.PostDTO;
import com.users.users.dto.Search;
import com.users.users.model.*;
import com.users.users.service.CommentsService;
import com.users.users.service.PostService;
import com.users.users.service.UserService;
import com.users.users.service.ViewsService;
import org.mapstruct.control.MappingControl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import java.time.LocalDate;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
public class PostController {

    @Autowired
    private PostService postService;
    @Autowired
    private CommentsService commentsService;
    @Autowired
    private UserService userService;
    @Autowired
    private ViewsService viewsService;


    @Transactional
    @PostMapping ("/upload")
    public String uploadPost(@ModelAttribute("post") Post post, @RequestParam("postImage") MultipartFile file, Model model) {
        if(checkAuth()) return "redirect:/logout";
        model.addAttribute("posteNew", postService.getLastPosts());
        return postService.checkUploadPost(post, file, model);
    }

    @GetMapping("/readpost/{postId}")
    public String readPost(@PathVariable Integer postId, Model model) {
        if(checkAuth()) return "redirect:/logout";
        Post post = postService.getPostById(postId);
        model.addAttribute("postread", postService.getPostById(postId));
        userService.findById(post.getName()).ifPresent(customUser -> model.addAttribute("author", customUser.getName()));
        model.addAttribute("comments", commentsService.findByIdPost(postId));
        model.addAttribute("postComment", new Comments());
        model.addAttribute("userName", SecurityContextHolder.getContext().getAuthentication().getName());
        int view = viewsService.findByRoles(postId);
        viewsService.update(view+1, postId);
        return "site/readpost";
    }

    @PostMapping("/savecomment")
    public String writeComments(@ModelAttribute("postComment") Comments comments,
                                @ModelAttribute("postId") Integer postId){
        if(checkAuth()) return "redirect:/logout";
        postService.saveComment(comments, postId);
        saveNewViews(postId);
        return "redirect:/readpost/" + postId;
    }

    @GetMapping("/deletecomm/{id}")
    public String deleteComment(@PathVariable Integer id){
        if(checkAuth()) return "redirect:/logout";
        Integer postId = null;
        Optional<Comments> comments = commentsService.findById(id);
        if(comments.isPresent()){
            postId = comments.get().getPost();
            commentsService.deleteById(id);
        }
        saveNewViews(postId);
        return "redirect:/readpost/" + postId;
    }

    private void saveNewViews (Integer postId) {
        int view = viewsService.findByRoles(postId);
        viewsService.update(view-1, postId);
    }

    public boolean checkAuth(){
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        if(userService.findByName(name).isEmpty()) {
            SecurityContextHolder.clearContext();
            return true;
        }
        return false;
    }

}
