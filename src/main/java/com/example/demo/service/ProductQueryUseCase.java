package com.example.demo.service;

import com.example.demo.entity.Product;

import java.util.List;
import java.util.UUID;

public interface ProductQueryUseCase {

    Product getById(UUID productId);

    List<Product> getAll();
}
