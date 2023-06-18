package com.alamu817group4.inventorypro.entities;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class Address {

    @NotBlank(message = "street is required")
    private String street;

    @NotBlank(message = "city is required")
    private String city;

    @NotBlank(message = "state is required")
    private String state;

    private String postalCode;

    @NotBlank(message = "country is required")
    private String country;
}
