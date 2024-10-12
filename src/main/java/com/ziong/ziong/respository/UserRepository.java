package com.ziong.ziong.respository;

import com.ziong.ziong.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    @Query("SELECT u FROM User u WHERE u.email = :email")
    User findByEmail( @Param("email")String email);

    @Query("SELECT u FROM User u WHERE u.userName = :userName")
    User findByUsername(@Param("userName")String fullName);

    boolean existsByEmail(String email);

    boolean existsByUserName(String userName);
    User findByResetPasswordToken(String token);

}
