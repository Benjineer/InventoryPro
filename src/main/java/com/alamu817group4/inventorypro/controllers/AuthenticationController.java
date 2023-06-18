package com.alamu817group4.inventorypro.controllers;

import com.alamu817group4.inventorypro.dtos.AuthenticationRequest;
import com.alamu817group4.inventorypro.dtos.ResponseObject;
import com.alamu817group4.inventorypro.services.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/authenticate")
    public ResponseEntity<ResponseObject<?>> authenticate(@RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(ResponseObject.builder()
                .status(ResponseObject.ResponseStatus.SUCCESSFUL)
                .data(authenticationService.authenticate(request))
                .build());
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<ResponseObject<?>> refreshToken(@RequestHeader String refreshToken) {
        return ResponseEntity.ok(ResponseObject.builder()
                .status(ResponseObject.ResponseStatus.SUCCESSFUL)
                .data(authenticationService.refreshToken(refreshToken))
                .build());
    }
}
