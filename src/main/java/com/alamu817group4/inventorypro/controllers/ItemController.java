package com.alamu817group4.inventorypro.controllers;

import com.alamu817group4.inventorypro.dtos.ResponseObject;
import com.alamu817group4.inventorypro.entities.Item;
import com.alamu817group4.inventorypro.services.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/items")
public class ItemController {
    private final ItemService itemService;

    @GetMapping
    public ResponseObject<Object> getAllItems() {
        return ResponseObject.builder()
                .data(itemService.getAllItems())
                .status(ResponseObject.ResponseStatus.SUCCESSFUL)
                .build();
    }

    @GetMapping("/{id}")
    public ResponseObject<Object> getItemById(@PathVariable Long id) {
        return ResponseObject.builder()
                .data(itemService.getItemById(id))
                .status(ResponseObject.ResponseStatus.SUCCESSFUL)
                .build();
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN') || hasAuthority('ROLE_MANAGER')")
    public ResponseEntity<ResponseObject> addItem(@RequestBody Item item) {
        return new ResponseEntity<>(ResponseObject.builder()
                .status(ResponseObject.ResponseStatus.SUCCESSFUL)
                .data(itemService.addItem(item))
                .build(), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') || hasAuthority('ROLE_MANAGER')")
    public ResponseObject<Object> updateItem(@PathVariable Long id, @RequestBody Item updatedItem) {
        itemService.updateItem(id, updatedItem);
        return ResponseObject.builder()
                .status(ResponseObject.ResponseStatus.SUCCESSFUL)
                .build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') || hasAuthority('ROLE_MANAGER')")
    public ResponseObject<Object> removeItem(@PathVariable Long id){
        itemService.removeItem(id);
        return ResponseObject.builder()
                .status(ResponseObject.ResponseStatus.SUCCESSFUL)
                .build();
    }
}
