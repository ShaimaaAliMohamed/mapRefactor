package com.sadeem.smap.repository;

import com.sadeem.smap.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}