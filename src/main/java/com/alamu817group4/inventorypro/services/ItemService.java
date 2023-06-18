package com.alamu817group4.inventorypro.services;

import com.alamu817group4.inventorypro.entities.Item;
import com.alamu817group4.inventorypro.repositories.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.FetchNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemService {

    private static final int LOW_STOCK_THRESHOLD = 10;

    private final ItemRepository itemRepository;
    private final JavaMailSender mailSender;

    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }

    public Item getItemById(Long id) {

        return itemRepository.findById(id)
                .orElseThrow(() -> new FetchNotFoundException(Item.class.getSimpleName(), id.toString()));
    }

    public Item addItem(Item item) {
        return itemRepository.save(item);
    }

    public void updateItem(Long id, Item updatedItem) throws FetchNotFoundException {
        itemRepository.findById(id)
                .ifPresentOrElse(
                        item -> {
                            item.setName(updatedItem.getName());
                            item.setDescription(updatedItem.getDescription());
                            item.setQuantity(updatedItem.getQuantity());
                            itemRepository.save(updatedItem);
                        },
                        () -> {
                            throw new FetchNotFoundException(Item.class.getSimpleName(), id.toString());
                        });
    }

    public void removeItem(Long id) {
        itemRepository.deleteById(id);
    }

    @Scheduled(cron = "0 0 8 * * *") // Run every day at 8 AM
    public void checkLowStock() {
        List<Item> lowStockItems = itemRepository.findAll()
                .stream().filter(item -> item.getQuantity() < LOW_STOCK_THRESHOLD)
                .collect(Collectors.toList());

        if (!lowStockItems.isEmpty()) {
            sendLowStockNotification(lowStockItems);
        }
    }

    private void sendLowStockNotification(List<Item> lowStockItems) {
        StringBuilder textBuilder = new StringBuilder();
        textBuilder.append("The following items are low in stock:\n\n");
        for (Item item : lowStockItems) {
            textBuilder.append("Item Name: ").append(item.getName()).append("\n");
            textBuilder.append("Quantity: ").append(item.getQuantity()).append("\n\n");
        }

        String subject = "Low Stock Notification";
        String text = textBuilder.toString();

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("inventorypro@alamu817group4.com");
        message.setTo("gameamster00227@lmavbfad.xyz"); // Set the recipient's email address
        message.setSubject(subject);
        message.setText(text);

        mailSender.send(message);
    }
}
