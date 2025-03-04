package com.sadeem.smap.controller;

import com.sadeem.smap.dto.OrderDto;
import com.sadeem.smap.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * Get all orders.
     */
    @GetMapping
    public ResponseEntity<Iterable<OrderDto>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    /**
     * Get paginated list of orders.
     */
    @GetMapping("/list")
    public ResponseEntity<Page<OrderDto>> getOrdersPaginated(Pageable pageable) {
        return ResponseEntity.ok(orderService.getOrdersPaginated(pageable));
    }

    /**
     * Get order by ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<OrderDto> getOrderById(@PathVariable Long id, Locale locale) {
        return orderService.getOrderById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Create a new order.
     */
    @PostMapping("/add")
    public ResponseEntity<Void> createOrder(@RequestBody OrderDto orderDto, Locale locale) {
        orderService.createOrder(orderDto);
        return ResponseEntity.status(201).build();
    }

    /**
     * Update an existing order.
     */
    @PutMapping("/edit")
    public ResponseEntity<Void> updateOrder(@RequestBody OrderDto orderDto, Locale locale) {
        orderService.updateOrder(orderDto);
        return ResponseEntity.ok().build();
    }

    /**
     * Delete an order by ID.
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id, Locale locale) {
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Delete all orders.
     */
    @DeleteMapping("/delete-all")
    public ResponseEntity<Void> deleteAllOrders(Locale locale) {
        orderService.deleteAllOrders();
        return ResponseEntity.noContent().build();
    }
}