package com.sadeem.smap.repository;

import com.sadeem.smap.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}