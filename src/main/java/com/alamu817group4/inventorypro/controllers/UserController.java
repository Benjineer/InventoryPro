package com.alamu817group4.inventorypro.controllers;

import com.alamu817group4.inventorypro.dtos.RegisterRequest;
import com.alamu817group4.inventorypro.dtos.ResponseObject;
import com.alamu817group4.inventorypro.entities.User;
import com.alamu817group4.inventorypro.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<ResponseObject<?>> register(@RequestBody RegisterRequest request) {
        User user = userService.register(request);
        return ResponseEntity
                .created(URI.create(String.format("/api/v1/user/%s", user.getId())))
                .body(ResponseObject.builder()
                        .status(ResponseObject.ResponseStatus.SUCCESSFUL)
                        .data(user)
                        .build());
    }

    @GetMapping("/me")
    public ResponseEntity<ResponseObject<?>> getUser(@AuthenticationPrincipal(expression = "username") String username) {
        return ResponseEntity.ok(ResponseObject.builder()
                .status(ResponseObject.ResponseStatus.SUCCESSFUL)
                .data(userService.getUser(username))
                .build());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ResponseObject<?>> getUser(@PathVariable Long id) {
        return ResponseEntity.ok(ResponseObject.builder()
                .status(ResponseObject.ResponseStatus.SUCCESSFUL)
                .data(userService.getUser(id))
                .build());
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ResponseObject<?>> getUsers() {
        return ResponseEntity.ok(ResponseObject.builder()
                .status(ResponseObject.ResponseStatus.SUCCESSFUL)
                .data(userService.getUsers())
                .build());
    }

    @PutMapping
    public ResponseObject<Object> updateUser(@AuthenticationPrincipal(expression = "username") String username,
                                             @RequestBody User updatedUser) {
        userService.updateUser(username, updatedUser);
        return ResponseObject.builder()
                .status(ResponseObject.ResponseStatus.SUCCESSFUL)
                .build();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseObject<Object> updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
        userService.updateUser(id, updatedUser);
        return ResponseObject.builder()
                .status(ResponseObject.ResponseStatus.SUCCESSFUL)
                .build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseObject<Object> removeUser(@PathVariable Long id) {
        userService.removeUser(id);
        return ResponseObject.builder()
                .status(ResponseObject.ResponseStatus.SUCCESSFUL)
                .build();
    }
}
