package com.users.users.service;

import com.users.users.dto.UserDTO;
import com.users.users.model.CustomUser;
import com.users.users.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class MainService {
    @Autowired
    private UserService userService;
    @Autowired
    private AuthenticationManager authenticationManager;

    public String checkValideted(UserDTO user){
        String name = user.getName();
        String password = user.getPassword();
        String nameAuth = SecurityContextHolder.getContext().getAuthentication().getName();
        if(userService.findByName(name).isPresent() && !(name.equals(nameAuth))){
            return "Пользователь с таким именем уже существует!";
        }
        if(name.length() < 3){
            return "Слишком короткое имя пользователя! (от 3 до 15 символов)";
        }
        if(name.length() >= 15){
            return "Слишком длинное имя пользователя! (от 3 до 15 символов)";
        }
        String regex = "[@. /;'+-]";
        if (Pattern.compile(regex).matcher(name).find()) {
            return "Имя содержит недопустимые символы";
        }
        if(password.length() < 8){
            return "Слишком короткий пароль! (от 8 до 30 символов)";
        }
        if(password.length() > 30){
            return "Слишком длинный пароль! (от 8 до 30 символов)";
        }
        return "";
    }
    @Transactional
    public UserDTO userPanelSetting(){
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        CustomUser customUser = userService.findByName(name).orElse(new CustomUser());

       return new UserDTO(customUser.getId(), customUser.getName(), customUser.getRole().getName(),
                customUser.getPassword(), customUser.getEmail(), customUser.getInstagram(),
                customUser.getFacebook(), customUser.getTelegram());

    }


    public void listNumber(Integer firstNum,Integer lastNum, Model model){
        List<Integer> pageNumbers = IntStream.rangeClosed(firstNum, lastNum)
                .boxed()
                .collect(Collectors.toList());
        model.addAttribute("pageNumbers", pageNumbers);
    }

    public void pointPrevAndNext(Integer currentPage,Integer totalPages,Model model) {
        if(currentPage+1 <=totalPages) {
            model.addAttribute("addPage", currentPage + 1);
        }
        if(currentPage +1 > totalPages) {
            model.addAttribute("addPage", currentPage);
        }
        if(currentPage - 1 >= 1) {
            model.addAttribute("delPage", currentPage - 1);
        }
    }
    @Transactional
    public void authenticated(HttpServletRequest request, UserDTO user, String password){
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getName(), password));
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authenticate);
        HttpSession session = request.getSession(true);
        session.setAttribute("SPRING_SECURITY_CONTEXT", securityContext);
    }

}
