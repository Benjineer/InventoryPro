package com.alamu817group4.inventorypro.repositories;

import com.alamu817group4.inventorypro.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    void deleteByIdAndUserEmail(Long id, String email);

    Optional<Order> findByUserEmail(String email);

    List<Order> findAllByUserEmail(String email);
}
