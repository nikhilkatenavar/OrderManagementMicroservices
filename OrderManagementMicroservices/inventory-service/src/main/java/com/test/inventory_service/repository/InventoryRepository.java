package com.test.inventory_service.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.test.inventory_service.Entity.Inventory;

public interface InventoryRepository extends JpaRepository<Inventory, Long>{

//	public Optional<Inventory> findByskuCode(String skuCode);
	public List<Inventory> findByskuCodeIn(List<String> skuCode);
}
