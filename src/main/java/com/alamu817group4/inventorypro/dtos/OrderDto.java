package com.alamu817group4.inventorypro.dtos;

import com.alamu817group4.inventorypro.entities.Address;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {

    private Long id;

    private List<@Valid OrderItemDto> orderItems;

    @Valid
    private Address deliveryAddress;

    private String paymentMethod;

    private String status;
}
