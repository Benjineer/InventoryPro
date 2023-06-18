package com.alamu817group4.inventorypro.repositories;

import com.alamu817group4.inventorypro.entities.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
}
