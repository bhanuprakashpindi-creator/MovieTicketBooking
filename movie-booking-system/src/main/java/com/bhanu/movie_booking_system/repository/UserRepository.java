package com.bhanu.movie_booking_system.repository;

import com.bhanu.movie_booking_system.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    User findByName(String name);
    boolean existsByEmail(String email);

    boolean existsByPhoneNumber(String phoneNumber);
}
