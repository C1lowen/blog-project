package com.users.users.controller;

import com.users.users.dto.PostDTO;
import com.users.users.dto.Search;
import com.users.users.dto.UserDTO;
import com.users.users.model.Answer;
import com.users.users.model.CustomUser;
import com.users.users.model.Post;
import com.users.users.service.CommentsService;
import com.users.users.service.MainService;
import com.users.users.service.PostService;
import com.users.users.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
public class MainController {
    @Autowired
    private UserService userService;
    @Autowired
    private MainService mainService;
    @Autowired
    private PostService postService;
    @Autowired
    private PostController postController;

    @GetMapping("/login")
    public String authentication(Model model){
        model.addAttribute("user", new UserDTO());
        return "auth/singup";
    }

    @PostMapping("/singup")
    public String create(@ModelAttribute("user") UserDTO user, HttpServletRequest request, Model model){
        String result = mainService.checkValideted(user);
        if(!(result.isEmpty())) {
            model.addAttribute("textException", result);
            model.addAttribute("error", true);
            return "auth/singup";
        }
        user.setRole("User");
        String password = user.getPassword();
        userService.add(user);
        mainService.authenticated(request, user, password);
        return "redirect:/";
    }

    @GetMapping("/login?error")
    public String loginError(Model model) {
        model.addAttribute("error", true);
        return "auth/singup";
    }


    @GetMapping("/")
    public String rootPage(Model model,
                           @RequestParam("page") Optional<Integer> page,
                           @RequestParam("size") Optional<Integer> size){

        int currentPage = page.orElse(1);
        int pageSize = size.orElse(6);

        Page<PostDTO> bookPage = postService.findPaginated(PageRequest.of(currentPage - 1, pageSize));

        int totalPages = bookPage.getTotalPages();
        int sizePage = 4;

        model.addAttribute("posteNew", "");
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("bookPage", bookPage);
        model.addAttribute("lastPage", totalPages);

        mainService.pointPrevAndNext(currentPage,totalPages,model);

        if (currentPage >= sizePage && currentPage < totalPages) {
            mainService.listNumber(currentPage-2,currentPage+1, model);
        } else if(totalPages <= sizePage){
            mainService.listNumber(1,totalPages, model);
        } else if(currentPage == totalPages){
            mainService.listNumber(currentPage-3,currentPage, model);
        } else if (currentPage <= totalPages){
            mainService.listNumber(1,sizePage, model);
        }
        return "site/index";
    }



    @GetMapping("/single")
    public String singlePost(Model model){
        if(postController.checkAuth()) return "redirect:/login";
        model.addAttribute("post", new Post());
        model.addAttribute("posteNew", postService.getLastPosts());
        return "site/post";
    }

    @GetMapping("/contact")
    public String contact(Model model){
        model.addAttribute("answer", new Answer());
        return "site/contact";
    }

    @GetMapping("/about")
    public String about(){
        return "site/about";
    }

    @GetMapping("/userpanel")
    public String userPanel(Model model){
        if(postController.checkAuth()) return "redirect:/logout";
        UserDTO user = mainService.userPanelSetting();
        model.addAttribute("infouser", user);
        return "site/userpanel";
    }

    @PostMapping("/userpanel/save")
    public String userPanelSave(@ModelAttribute("infouser") UserDTO user,Model model) {
        if(postController.checkAuth()) return "redirect:/logout";
        if (user.getName().isEmpty()) {
            return "site/userpanel";
        }

        String result = mainService.checkValideted(user);
        if (!result.isEmpty()) {
            model.addAttribute("errorSaveUser", true);
            model.addAttribute("textException", result);
            return "site/userpanel";
        }

        model.addAttribute("okey", true);
        model.addAttribute("textValid", "Данные успешно изменены!");
        userService.update(user);
        return "site/userpanel";
    }


    @ModelAttribute
    public void checkAuth(Model model){
        model.addAttribute("textException", "");
        model.addAttribute("error", false);
        model.addAttribute("okey", false);
    }


}
