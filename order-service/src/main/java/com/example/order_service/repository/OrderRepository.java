package com.example.order_service.repository;

import com.example.order_service.dto.OrderResponseDTO;
import com.example.order_service.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<OrderResponseDTO> findByUserName(String userName);
    List<OrderResponseDTO> findByUserId(Long id);
    List<OrderResponseDTO> findByProductId(Long id);
}