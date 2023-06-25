package com.alamu817group4.inventorypro.repositories;

import com.alamu817group4.inventorypro.entities.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
