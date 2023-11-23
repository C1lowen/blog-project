package com.users.users.repository;

import com.users.users.model.Views;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ViewsRepository extends JpaRepository<Views, Integer> {
    @Query(value = "select count_view from views where post_id = :id",nativeQuery = true)
    Integer findByRole(@Param("id") Integer idRole);

    @Query(value = "UPDATE views SET count_view = :count where post_id = :id", nativeQuery = true)
    @Modifying
    void update(@Param("count") Integer count, @Param("id") Integer id);
}
