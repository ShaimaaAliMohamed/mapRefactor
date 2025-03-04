package com.sadeem.smap.controller;

import com.sadeem.smap.dto.ProductDto;
import com.sadeem.smap.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Locale;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    /**
     * Get all products.
     */
    @GetMapping
    public ResponseEntity<Iterable<ProductDto>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    /**
     * Get paginated list of products.
     */
    @GetMapping("/list")
    public ResponseEntity<Page<ProductDto>> getProductsPaginated(Pageable pageable) {
        return ResponseEntity.ok(productService.getProductsPaginated(pageable));
    }

    /**
     * Get product by ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable Long id, Locale locale) {
        return productService.getProductById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Create a new product.
     */
    @PostMapping("/add")
    public ResponseEntity<Void> createProduct(@RequestBody ProductDto productDto, @RequestParam("file") MultipartFile file, Locale locale) throws IOException {
        productService.createProduct(productDto, file);
        return ResponseEntity.status(201).build();
    }

    /**
     * Update an existing product.
     */
    @PutMapping("/edit")
    public ResponseEntity<Void> updateProduct(@RequestBody ProductDto productDto, @RequestParam(value = "file", required = false) MultipartFile file, Locale locale) throws IOException {
        productService.updateProduct(productDto, file);
        return ResponseEntity.ok().build();
    }

    /**
     * Delete a product by ID (soft delete).
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id, Locale locale) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}