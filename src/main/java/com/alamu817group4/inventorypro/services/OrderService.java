package com.alamu817group4.inventorypro.services;

import com.alamu817group4.inventorypro.dtos.OrderDto;
import com.alamu817group4.inventorypro.dtos.OrderItemDto;
import com.alamu817group4.inventorypro.entities.Address;
import com.alamu817group4.inventorypro.entities.Item;
import com.alamu817group4.inventorypro.entities.Order;
import com.alamu817group4.inventorypro.entities.OrderItem;
import com.alamu817group4.inventorypro.exceptions.InventoryProClientException;
import com.alamu817group4.inventorypro.repositories.OrderRepository;
import com.alamu817group4.inventorypro.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.FetchNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final ItemService itemService;

    private final UserRepository userRepository;

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public List<Order> getAllOrders(String username) {
        return orderRepository.findAllByUserEmail(username);
    }

    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new FetchNotFoundException(Order.class.getSimpleName(), id.toString()));
    }

    public Order createOrder(OrderDto orderDto, String username) {
        List<OrderItemDto> orderItems = orderDto.getOrderItems();
        Address deliveryAddress = orderDto.getDeliveryAddress();

        // Validate item quantities against stock quantity
        for (OrderItemDto orderItemDto : orderItems) {
            Item item = itemService.getItemById(orderItemDto.getItemId());
            if (orderItemDto.getQuantity() > item.getQuantity()) {
                throw new InventoryProClientException("Insufficient stock for item: " + item.getName());
            }
        }

        // Create Order entity and set properties
        Order order = new Order();
        order.setUser(userRepository.getReferenceByEmail(username));
        order.setDeliveryAddress(deliveryAddress);
        order.setPaymentMethod("Payment Method"); // Set the payment method as needed
        order.setStatus("Pending"); // Set the initial status of the order

        // Create OrderItems and associate them with the Order
        List<OrderItem> orderItemList = new ArrayList<>();
        for (OrderItemDto orderItemDto : orderItems) {
            Item item = itemService.getItemById(orderItemDto.getItemId());
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setItem(item);
            orderItem.setQuantity(orderItemDto.getQuantity());
            orderItemList.add(orderItem);

            // Update the stock quantity of the item
            item.setQuantity(item.getQuantity() - orderItemDto.getQuantity());
            itemService.updateItem(item.getId(), item);
        }

        order.setOrderItems(orderItemList);

        // Save the Order entity and associated OrderItems
        return orderRepository.save(order);
    }

    public void updateOrderStatus(String username, String status) {
        orderRepository.findByUserEmail(username)
                .ifPresentOrElse(order -> {
                    order.setStatus(status);
                    orderRepository.save(order);
                }, () -> {
                    throw new FetchNotFoundException(Order.class.getSimpleName(), username);
                });
    }

    public void updateOrderStatus(Long id, String status) {
        Order order = getOrderById(id);
        order.setStatus(status);
        orderRepository.save(order);
    }

    public void deleteOrder(String username, Long id) {
        orderRepository.deleteByIdAndUserEmail(id, username);
    }

    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }
}
