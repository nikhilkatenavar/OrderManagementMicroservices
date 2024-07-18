package com.test.product_service.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.test.product_service.Entity.Product;

public interface ProductRepository extends MongoRepository<Product, String>{

}
