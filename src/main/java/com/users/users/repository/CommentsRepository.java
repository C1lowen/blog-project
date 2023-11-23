package com.users.users.repository;

import com.users.users.model.Comments;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentsRepository extends JpaRepository<Comments, Integer> {
    @Query(value = "select * from comments where post = :idPost ORDER BY id DESC",nativeQuery = true)
    List<Comments> findByIdPost(@Param("idPost") Integer idPost);

    Integer countCommentsByPost(Integer post);

    @Query(value = "delete from comments where name = :name", nativeQuery = true)
    @Modifying
    void deleteByName(@Param("name") String name);

    @Query(value = "update comments set name = :newName where name = :parentName ", nativeQuery = true)
    @Modifying
    void updateInfo(@Param("parentName") String parentName, @Param("newName") String newName);
    @Query(value = "delete from comments where post = :id", nativeQuery = true)
    @Modifying
    void deleteByIdPosts(@Param("id") Integer id);
}
