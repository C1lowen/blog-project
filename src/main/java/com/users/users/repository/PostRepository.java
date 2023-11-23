package com.users.users.repository;

import com.users.users.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {
    @Query(value = "select * from post ORDER BY id DESC limit 2",nativeQuery = true)
    List<Post> getLastPosts();

    @Query(value = "select * from post ORDER BY id DESC",nativeQuery = true)
    List<Post> findAll();
    @Query(value = "delete from post where name = :id",nativeQuery = true)
    @Modifying
    void deleteByIndex(@Param("id") Integer user);

    Post getById(Integer id);
    @Query(value = "select id from post where name = :userId",nativeQuery = true)
    List<Integer> getIdPostByIdUser(@Param("userId") Integer userId);

    @Query(value = "select nextval('post_id_seq')", nativeQuery = true)
    Integer generateNextId();

}
