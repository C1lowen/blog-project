package com.users.users.service;
import com.users.users.dto.Search;
import com.users.users.dto.UserDTO;
import com.users.users.model.Role;

import com.users.users.repository.*;
import com.users.users.model.CustomUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CommentsService commentsService;

    public List<UserDTO> findAll() {
        return createListUserDTO(userRepository.findAll());
    }
    @Transactional
    public void add(@Validated UserDTO user) {
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        String password = user.getPassword();
        user.setPassword(passwordEncoder.encode(password));
        Role role = roleRepository.findByName(user.getRole()).get();
        CustomUser customUser = new CustomUser(user.getName(), role, user.getPassword(), user.getEmail());

       userRepository.save(customUser);
    }

    public Optional<CustomUser> findByName(String name){
       return userRepository.findByName(name);
    }

    @Transactional
    public void update(UserDTO newUser) {
        UsernamePasswordAuthenticationToken token= (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        User user = (User) token.getPrincipal();
        commentsService.updateInfo(user.getUsername(), newUser.getName());
        findByName(user.getUsername()).ifPresent((customUser)  -> {
            userRepository.saveUser(customUser.getId(), newUser.getName(), newUser.getEmail(), passwordEncoder.encode(newUser.getPassword()),newUser.getFacebook(), newUser.getInstagram(), newUser.getTelegram() );
            User newUser3 = new User(newUser.getName(), "", user.isEnabled(), user.isAccountNonExpired(), user.isCredentialsNonExpired(), user.isAccountNonLocked(), user.getAuthorities());
            UsernamePasswordAuthenticationToken newToken = new UsernamePasswordAuthenticationToken(newUser3, token.getCredentials(), token.getAuthorities());
            SecurityContext securityContext = SecurityContextHolder.getContext();
            securityContext.setAuthentication(newToken);
        });
    }

    @Transactional
    public Optional<CustomUser> findById(Integer id){
        return userRepository.findById(id);
    }


    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        CustomUser user = findByName(username).orElseThrow(() -> new UsernameNotFoundException(
                "Пользователь с именем " + username + " не найден"
        ));

        List<GrantedAuthority> roles = List.of(
                new SimpleGrantedAuthority("ROLE_" +user.getRole().getName())
        );

        return new User(user.getName(), user.getPassword(), roles);
    }
    @Transactional
    public void deleteById(Integer id) {
        userRepository.deleteById(id);
    }

    public List<UserDTO> getByName(String name) {
        return createListUserDTO(userRepository.getByName(name));
    }
    @Transactional
    private List<UserDTO> createListUserDTO(List<CustomUser> users){
        return users.stream()
                .map(user -> new UserDTO(user.getId(),user.getName(), user.getRole().getName(),user.getPassword(), user.getEmail(), null, null, null))
                .collect(Collectors.toList());
    }

    @Transactional
    public String findUser(Search search, Model model){
        String loggedInUsername = SecurityContextHolder.getContext().getAuthentication().getName();

        if (search.getName().isEmpty()) {
            return "redirect:/admin";
        }

        Optional<CustomUser> userOptional = findByName(search.getName());
        if (userOptional.isPresent()) {
            CustomUser customUser = userOptional.get();
            if (loggedInUsername.equals(customUser.getName()) || "Admin".equals(customUser.getRole().getName())) {
                model.addAttribute("allUsers", null);
            } else {
                List<UserDTO> userDTO = List.of(new UserDTO(
                        customUser.getId(),
                        customUser.getName(),
                        customUser.getRole().getName(),
                        customUser.getPassword(),
                        customUser.getEmail(),
                        customUser.getInstagram(),
                        customUser.getFacebook(),
                        customUser.getTelegram()
                ));
                model.addAttribute("allUsers", userDTO);
            }

            return "site/admin";
        } else {
            model.addAttribute("allUsers", null);
            return "site/admin";
        }
    }
}
