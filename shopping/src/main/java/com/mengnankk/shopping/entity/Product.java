package com.mengnankk.shopping.entity;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class Product {
    private Long id;
    private String name;
    private BigDecimal price;
    private String description;
    private Integer stock;
} 