package com.test.product_service.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.test.product_service.Entity.Product;
import com.test.product_service.Repository.ProductRepository;
import com.test.product_service.dto.ProductRequest;
import com.test.product_service.dto.ProductResponse;

import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class ProductService {

	@Autowired
	ProductRepository productRepositoy;
	
	public void createProduct(ProductRequest productRequest) {
		Product product = Product.builder().name(productRequest.getName())
				.description(productRequest.getDescription())
				.price(productRequest.getPrice())
				.build();
		productRepositoy.save(product);
		
		log.info("Product {}  is saved",product.getId());
		
	}

	public List<ProductResponse> getAllProducts() {
		// TODO Auto-generated method stub
		List<Product> products = productRepositoy.findAll();
		
		return products.stream().map(this::mapToProductResponse).collect(Collectors.toList());
		
	}

	private ProductResponse mapToProductResponse(Product product) {
		// TODO Auto-generated method stub
		return ProductResponse.builder()
				.id(product.getId())
				.name(product.getName())
				.description(product.getDescription())
				.price(product.getPrice())
				.build();
	}
	
	
}
