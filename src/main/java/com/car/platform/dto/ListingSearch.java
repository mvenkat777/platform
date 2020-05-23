/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.car.platform.dto;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class ListingSearch {
    private String code;
    private String make;
    private String model;
    private int kw;
    private int year;
    private String color;
    private BigDecimal price;
}
