package com.example.springlab.controller;

import com.example.springlab.dto.CreateProductRequest;
import com.example.springlab.model.Product;
import com.example.springlab.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private ProductService productService;

    //constructor injection
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public List<Product> findAll() {
        return productService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> findById(@PathVariable Long id) {
        Product product = productService.findById(id);
        if(product==null){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(product);
    }

    @PostMapping
    public ResponseEntity<Product> create(
           @Valid @RequestBody CreateProductRequest request) {
        Product product1= productService.create(request.name(), request.price());

       // return ResponseEntity.status(HttpStatus.CREATED).body(product1);

        //
        return ResponseEntity
                .created(URI.create("/api/products/" + product1.id()))
                .body(product1);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> update(
            @PathVariable Long id,
           @Valid @RequestBody CreateProductRequest request) {

        Product updated = productService.update(
                id, request.name(), request.price());

        if (updated == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id) {

        if (!productService.delete(id)) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }
}
