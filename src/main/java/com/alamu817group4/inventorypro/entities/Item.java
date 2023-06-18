package com.alamu817group4.inventorypro.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Getter
@Setter
@Builder
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "items")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    @Min(0)
    private int quantity;
}
