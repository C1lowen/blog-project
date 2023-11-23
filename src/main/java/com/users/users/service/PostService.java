package com.users.users.service;

import com.users.users.dto.PostDTO;
import com.users.users.model.Comments;
import com.users.users.model.CustomUser;
import com.users.users.model.Post;
import com.users.users.model.Views;
import com.users.users.repository.PostRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserService userService;

    @Autowired
    private CommentsService commentsService;
    @Autowired
    private ViewsService viewsService;

    @Value("${path.images}")
    private String uploadFolder;

    @Value("${path.images.write}")
    private String writeFile;

    public void save(Post post){
        postRepository.save(post);
    }
    @Transactional
    public List<PostDTO> findAllPosts(){
        return postRepository.findAll().stream()
                .map(post -> {
                    PostDTO postDTO =  new PostDTO(post.getId(), userService.findById(post.getName()).get().getName(),
                            post.getDate(),
                            post.getTitle(),
                            post.getText().length() > 200 ? truncateText(post.getText(), 200) : post.getText(),
                            post.getFolder(),
                            commentsService.countCommentsByPost(post.getId()));
                    postDTO.setViews(saveViews(postDTO.getId()));
                    return postDTO;
                })
                .toList();
    }

    public Page<PostDTO> findPaginated(Pageable pageable) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<PostDTO> list;
        List<PostDTO> posts = findAllPosts();
        if (posts.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, posts.size());
            list = posts.subList(startItem, toIndex);
        }

        Page<PostDTO> bookPage
                = new PageImpl<PostDTO>(list, PageRequest.of(currentPage, pageSize), posts.size());

        return bookPage;
    }
    @Transactional
    public Post getPostById(Integer id){
        Post post = postRepository.getById(id);
        post.setCountComments(commentsService.countCommentsByPost(id));
        return post;
    }
    @Transactional
    public List<PostDTO> getLastPosts(){

         return postRepository.getLastPosts().stream()
                .map(post -> {
                    PostDTO postDTO = new PostDTO(post.getId(), userService.findById(post.getName()).get().getName(),
                            post.getDate(),
                            post.getTitle().length() > 40 ? truncateText(post.getTitle(), 40) : post.getTitle(),
                            post.getText().length() > 80 ? truncateText(post.getText(), 80) : post.getText() + "...",
                            post.getFolder(), commentsService.countCommentsByPost(post.getId()));
                    postDTO.setViews(saveViews(postDTO.getId()));
                    return postDTO;
                })
                .toList();
    }

    private String validPost(Post post){
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        if(name.isEmpty()) {
            return "Вы не авторизованы!";
        }
        if(post.getTitle().isEmpty() || post.getText().isEmpty()) {
            return "Проверьте заполнили ли вы все текстовые поля и загрузили изображение!";
        }
        if(post.getTitle().length() < 3 || post.getTitle().length() > 50){
            return "Длина заголовка не  соответствует установленным ограничениям (от 3 до 50 символов)";
        }
        return "";
    }

    private String validFile(MultipartFile file) {
        try {
            if(file.getBytes().length < 10000) {
                return "Файл слишком маленький";
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String[] array = file.getOriginalFilename().split("[.]");
        String result = array[array.length-1].toLowerCase();
        if(!(result.equals("png") || result.equals("jpg"))) {
            return "Поддерживаемые форматы (.png .jpg)";
        }

        return "";
    }
    @Transactional
    public String checkUploadPost(Post post, MultipartFile file, Model model) {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!(validate(post, model, file).isEmpty())) {
            return "site/post";
        }
        Optional<CustomUser> customUser = userService.findByName(name);
        if (customUser.isPresent()) {
            CustomUser user = customUser.get();
            post.setName(user.getId());
            post.setDate(LocalDate.now());
            String randNameFile = generateNameFile(file);
            try {
                saveImage(file, randNameFile);
                post.setFolder(uploadFolder + randNameFile);
                save(post);
            } catch (IOException e) {
                e.printStackTrace();
                errorException(model, "Вы не загрузили изображение!");
                return "site/post";
            }
        }
        return "redirect:/single";
    }


    private void saveImage (MultipartFile file, String nameFile) throws IOException {
        byte[] bytes = file.getBytes();
        Path path = Paths.get(writeFile + nameFile);
        FileCopyUtils.copy(bytes, Files.newOutputStream(path));
    }

    private String generateNameFile(MultipartFile file) {
        String randNameFile = UUID.randomUUID().toString();
        String[] array = file.getOriginalFilename().split("[.]");
        String extension = array[array.length-1].toLowerCase();
        return randNameFile + '.' + extension;
    }


    public Integer saveViews(Integer postId){
        Integer view = viewsService.findByRoles(postId);
        if (view == null) {
            viewsService.save(new Views(postId, 0));
            view = 0;
        }
        return view;
    }

    public void saveComment(Comments comments, Integer postId){
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        if(validComment(comments.getMessage()) && !(name.isEmpty())) {
            comments.setPost(postId);
            comments.setDate(LocalDate.now());
            comments.setName(name);
            commentsService.save(comments);
        }
    }
    private void errorException(Model model, String error){
        model.addAttribute("error", true);
        model.addAttribute("textException", error);
    }
    private String validate(Post post, Model model, MultipartFile file) {
        validText(post);
        String answerService = validPost(post);
        if(!answerService.isEmpty()) {
            errorException(model, answerService);
            return "site/post";
        }
        String checkFile = validFile(file);
        if(!checkFile.isEmpty()) {
            errorException(model, checkFile);
            return "site/post";
        }
        return "";
    }

    private void validText(Post post) {
        String title = post.getTitle().replaceAll("\\s+", " ");
        if(title.contains(" ") && title.length() <= 3){
            title = title.replace(" ", "");
        }
        post.setTitle(title);
    }
    @Transactional
    public void deleteByIndex(Integer id){
        List<Integer> listId = postRepository.getIdPostByIdUser(id);
        for(int userId : listId) {
            commentsService.deleteByIdPosts(userId);
        }
        postRepository.deleteByIndex(id);
    }

    public String truncateText(String text, Integer length) {
        String truncatedText = text.substring(0, length);
        String[] words = truncatedText.split("[ ]");
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < words.length - 1; i++) {
            result.append(words[i]).append(" ");
        }
        return result.toString().trim() + "...";
    }
    public boolean validComment(String comment){
        String str = comment;
        int count = comment.length() - str.replaceAll((" "), "").length();
        if(comment.isEmpty() || count == comment.length()){
            return false;
        }
        return true;
    }

}
