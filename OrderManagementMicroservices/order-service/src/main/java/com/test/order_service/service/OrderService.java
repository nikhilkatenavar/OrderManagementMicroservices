package com.test.order_service.service;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import com.test.order_service.dto.InventoryResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.test.order_service.dto.OrderLineItemsDto;
import com.test.order_service.dto.OrderRequest;
import com.test.order_service.entity.Order;
import com.test.order_service.entity.OrderLineItems;
import com.test.order_service.repository.OrderRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@Transactional
@Slf4j
public class OrderService {

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	WebClient.Builder webClientBuilder;

	public void placeOrder(OrderRequest orderRequest) {
		Order order = new Order();
		log.info("quantity is {}",orderRequest.getOrderLineItemsDto());
		order.setOrderNumber(UUID.randomUUID().toString());

		List<OrderLineItems> orderLineItems = orderRequest.getOrderLineItemsDto()
				.stream()
				.map(this::mapToDto)
				.toList();
		order.setOrderLineItems(orderLineItems);
		List<String> skuCodes = order.getOrderLineItems().stream().map(OrderLineItems::getSkuCode).toList();
		//Call inventory service and place order if the product is instock
		InventoryResponse[] inventoryResponseArray = webClientBuilder.build().get()
				.uri("http://inventory-service/api/inventory",
						uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).build())
				.retrieve()
				.bodyToMono(InventoryResponse[].class)
				.block();
		boolean allProductsInStock = Arrays.stream(inventoryResponseArray).allMatch(InventoryResponse::isInStock);
		if(allProductsInStock){
			orderRepository.save(order);
		}
		else{
			throw new IllegalArgumentException("Product is not in stock, Please try again later");
		}


	}
	
	public OrderLineItems mapToDto(OrderLineItemsDto orderLineItemsDto) {
		OrderLineItems orderLineItems = new OrderLineItems();
		orderLineItems.setPrice(orderLineItemsDto.getPrice());
		orderLineItems.setQuantity(orderLineItems.getQuantity());
		orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());
		return orderLineItems;
		
	}
}

