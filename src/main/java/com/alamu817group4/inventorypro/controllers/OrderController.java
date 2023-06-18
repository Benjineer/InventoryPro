package com.alamu817group4.inventorypro.controllers;

import com.alamu817group4.inventorypro.dtos.OrderDto;
import com.alamu817group4.inventorypro.dtos.ResponseObject;
import com.alamu817group4.inventorypro.services.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
public class OrderController {
    private final OrderService orderService;

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN') || hasAuthority('ROLE_MANAGER')")
    public ResponseObject<Object> getAllOrders() {
        return ResponseObject.builder()
                .data(orderService.getAllOrders())
                .status(ResponseObject.ResponseStatus.SUCCESSFUL)
                .build();
    }

    @GetMapping("/me")
    public ResponseObject<Object> getAllOrders(@AuthenticationPrincipal(expression = "username") String username) {
        return ResponseObject.builder()
                .data(orderService.getAllOrders(username))
                .status(ResponseObject.ResponseStatus.SUCCESSFUL)
                .build();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') || hasAuthority('ROLE_MANAGER')")
    public ResponseObject<Object> getOrderById(@PathVariable Long id) {
        return ResponseObject.builder()
                .data(orderService.getOrderById(id))
                .status(ResponseObject.ResponseStatus.SUCCESSFUL)
                .build();
    }

    @PostMapping
    public ResponseObject<Object> createOrder(@AuthenticationPrincipal(expression = "username") String username,
                                              @RequestBody @Valid OrderDto orderDto) {
        return ResponseObject.builder()
                .data(orderService.createOrder(orderDto, username))
                .status(ResponseObject.ResponseStatus.SUCCESSFUL)
                .build();
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') || hasAuthority('ROLE_MANAGER')")
    public ResponseObject<Object> updateOrderStatus(@PathVariable Long id, @RequestParam String status) {
        orderService.updateOrderStatus(id, status);
        return ResponseObject.builder()
                .status(ResponseObject.ResponseStatus.SUCCESSFUL)
                .build();
    }

    @PatchMapping("/status")
    public ResponseObject<Object> updateOrderStatus(@AuthenticationPrincipal(expression = "username") String username,
                                                    @RequestParam String status) {
        orderService.updateOrderStatus(username, status);
        return ResponseObject.builder()
                .status(ResponseObject.ResponseStatus.SUCCESSFUL)
                .build();
    }

    @DeleteMapping("/{id}/me")
    public ResponseObject<Object> deleteOrder(@AuthenticationPrincipal(expression = "username") String username,
                                              @PathVariable Long id) {
        orderService.deleteOrder(username, id);
        return ResponseObject.builder()
                .status(ResponseObject.ResponseStatus.SUCCESSFUL)
                .build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseObject<Object> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return ResponseObject.builder()
                .status(ResponseObject.ResponseStatus.SUCCESSFUL)
                .build();
    }
}
