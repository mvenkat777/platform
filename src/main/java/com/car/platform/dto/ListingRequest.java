/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.car.platform.dto;

import java.util.List;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListingRequest {
    @Min(1)
    private Long dealer;
    @NotBlank(message = "Please provide the provider for this listing")
    private String provider;
    @NotEmpty(message = "Invalid listings provided")
    private List<ListingDto> listings;
}
