package com.users.users.repository;

import com.users.users.dto.UserDTO;
import com.users.users.model.CustomUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<CustomUser, Integer> {
    List<CustomUser> getByName(String name);
    @Query(value = "select * from users u where u.name = :name", nativeQuery = true)
    Optional<CustomUser> findByName(@Param("name") String name);

    @Query(value = "update users set name = :name, email = :email, password = :password, facebook = :facebook, instagram = :instagram, telegram = :telegram where id = :id", nativeQuery = true)
    @Modifying
    void saveUser(@Param("id") Integer id, @Param("name") String name, @Param("email")String email,
                  @Param("password") String password, @Param("facebook")String facebook,
                  @Param("instagram")String instagram, @Param("telegram")String telegram);
    @Query(value = "select name from users u where u.name = :name", nativeQuery = true)
    boolean checkName(@Param("name") String name);

}
