package com.alamu817group4.inventorypro.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;


import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();

    @Embedded
    private Address deliveryAddress;

    private String paymentMethod;

    private String status;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public void removeOrderItem(OrderItem orderItem) {
        orderItems.remove(orderItem);
        orderItem.setOrder(null);
    }
}
