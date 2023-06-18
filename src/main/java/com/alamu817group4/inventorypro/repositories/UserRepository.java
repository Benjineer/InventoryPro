package com.alamu817group4.inventorypro.repositories;

import com.alamu817group4.inventorypro.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findByEmail(String email);

  User getReferenceByEmail(String email);
}
