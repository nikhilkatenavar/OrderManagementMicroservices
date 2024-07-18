package com.test.inventory_service.service;

import com.test.inventory_service.Entity.Inventory;
import com.test.inventory_service.dto.InventoryResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.test.inventory_service.repository.InventoryRepository;

import java.util.List;


@Service
@Slf4j
public class InventoryService {
	
	@Autowired
	InventoryRepository inventoryRepository;
	
	@Transactional(readOnly = true)
	public List<InventoryResponse> isInStock(List<String> skuCode) {
		log.info("Entered Controller {}",skuCode.size() );
		return inventoryRepository.findByskuCodeIn(skuCode).stream()
				.map(inventory ->
					InventoryResponse.builder()
							.skuCode(inventory.getSkuCode())
							.isInStock(inventory.getQuantity() > 0)
							.build()
				).toList();
	}



}
