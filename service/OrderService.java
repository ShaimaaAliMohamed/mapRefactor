package com.sadeem.smap.service;

import com.sadeem.smap.dto.OrderDto;
import com.sadeem.smap.model.Order;
import com.sadeem.smap.model.Process;
import com.sadeem.smap.model.Product;
import com.sadeem.smap.repository.OrderRepository;
import com.sadeem.smap.repository.ProcessRepository;
import com.sadeem.smap.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProcessRepository processRepository;

    public Iterable<OrderDto> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public Page<OrderDto> getOrdersPaginated(Pageable pageable) {
        return orderRepository.findAll(pageable).map(this::convertToDto);
    }

    public Optional<OrderDto> getOrderById(Long id) {
        return orderRepository.findById(id).map(this::convertToDto);
    }

    public void createOrder(OrderDto orderDto) {
        Order order = convertToEntity(orderDto);
        orderRepository.save(order);
    }

    public void updateOrder(OrderDto orderDto) {
        Optional<Order> optionalOrder = orderRepository.findById(orderDto.getId());
        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            Product product = productRepository.findById(orderDto.getProductId()).orElseThrow();
            order.setProduct(product);
            order.setOrderName(orderDto.getName());
            order.setOrderQuantity(orderDto.getQuantity());

            // Clear and add processes only if the product is changed
            if (order.getProduct().getProductId() != orderDto.getProductId()) {
                Set<Process> processes = orderDto.getProductParts().stream()
                        .map(partId -> new Process(order, productRepository.findById(partId).orElseThrow()))
                        .collect(Collectors.toSet());
                order.getProcesses().clear();
                order.getProcesses().addAll(processes);
            }

            orderRepository.save(order);
        }
    }

    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }

    public void deleteAllOrders() {
        orderRepository.deleteAll();
    }

    private OrderDto convertToDto(Order order) {
        OrderDto dto = new OrderDto();
        dto.setId(order.getId());
        dto.setName(order.getOrderName());
        dto.setProductId(order.getProduct().getProductId());
        dto.setQuantity(order.getOrderQuantity());
        dto.setProcessIds(order.getProcesses().stream()
                .map(Process::getProcessId)
                .collect(Collectors.toSet()));
        return dto;
    }

    private Order convertToEntity(OrderDto orderDto) {
        Order order = new Order();
        order.setId(orderDto.getId());
        order.setProduct(productRepository.findById(orderDto.getProductId()).orElseThrow());
        order.setOrderName(orderDto.getName());
        order.setOrderQuantity(orderDto.getQuantity());
        order.setProcesses(orderDto.getProductParts().stream()
                .map(partId -> new Process(order, productRepository.findById(partId).orElseThrow()))
                .collect(Collectors.toSet()));
        return order;
    }
}