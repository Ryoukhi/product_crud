package com.eadl.product_management.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.eadl.product_management.entities.Product;

import java.util.List;
import java.math.BigDecimal;


@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    
    // Méthodes de recherche personnalisées
    List<Product> findByNameContainingIgnoreCase(String name);
    
    List<Product> findByAvailable(Boolean available);
    
    List<Product> findByPriceLessThanEqual(java.math.BigDecimal price);

    List<Product> findByPrice(BigDecimal price);
}