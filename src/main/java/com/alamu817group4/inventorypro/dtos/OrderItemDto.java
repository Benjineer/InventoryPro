package com.alamu817group4.inventorypro.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDto {

    @NotNull(message = "itemId is required")
    private Long itemId;

    @NotNull(message = "quantity is required")
    private int quantity;
}
