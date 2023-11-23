package com.users.users.service;

import com.users.users.dto.PostDTO;
import com.users.users.dto.PostSearchDTO;
import com.users.users.model.Post;
import com.users.users.model.Views;
import com.users.users.repository.PostRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SearchService {

    @Autowired
    private PostService postService;

    @Autowired
    private CommentsService commentsService;
    @Autowired
    private ViewsService viewsService;

    private  List<String> words;
    @Transactional
    public List<PostSearchDTO> search(String info){
        words = Arrays.stream(info.split("[ ]")).toList();
        return postService.findAllPosts().stream()
                .map(post -> {
                    PostSearchDTO postSearchDTO =  new PostSearchDTO(post.getId(), post.getName(),
                            post.getDate(),
                            post.getTitle(),
                            post.getText().length() > 200 ? postService.truncateText(post.getText(), 200) : post.getText() + "...",
                            post.getFolder(), commentsService.countCommentsByPost(post.getId()));
                    postSearchDTO.setViews(postService.saveViews(postSearchDTO.getId()));
                    return postSearchDTO;
                })
                .filter(post -> findByTitle(post) || findByUserName(post))
                .sorted(Comparator.comparingInt(PostSearchDTO::getIndex).reversed())
                .toList();
    }
    private boolean predicate(PostSearchDTO post, String str){
        for(String word : words) {
            if (str.toLowerCase().contains(word.toLowerCase())) {
                int index = post.getIndex();
                post.setIndex(index == 0 ? 1 : ++index);
            }
        }
        return post.getIndex() > 0;
    }

    private boolean findByTitle(PostSearchDTO post){
        return predicate(post, post.getTitle());
    }

    private boolean findByUserName(PostSearchDTO post){
        return predicate(post, post.getName());
    }

}
