package com.users.users.service;

import com.users.users.model.Comments;
import com.users.users.repository.CommentsRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CommentsService {

    @Autowired
    private CommentsRepository commentsRepository;


    public void save(Comments comments){
        commentsRepository.save(comments);
    }

    public List<Comments> findByIdPost(Integer idPost){
        return commentsRepository.findByIdPost(idPost);
    }

    public Integer countCommentsByPost(Integer id) {
       return commentsRepository.countCommentsByPost(id);
    }
    @Transactional
    public void deleteByName(String name) {
        commentsRepository.deleteByName(name);
    }
    @Transactional
    public void deleteById(Integer id) {
        commentsRepository.deleteById(id);
    }
    @Transactional
    public void deleteByIdPosts(Integer id){
        commentsRepository.deleteByIdPosts(id);
    }

    public Optional<Comments> findById(Integer id) {
        return commentsRepository.findById(id);
    }

    public void updateInfo(String parentName, String newName){
        commentsRepository.updateInfo(parentName, newName);
    }
}
