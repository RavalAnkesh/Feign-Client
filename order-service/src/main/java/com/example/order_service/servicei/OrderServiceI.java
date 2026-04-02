package com.example.order_service.servicei;

import com.example.order_service.dto.OrderResponseDTO;
import com.example.order_service.entity.Order;

import java.util.List;

public interface OrderServiceI {
    Order placeOrUpdateOrder(Order order);
    List<OrderResponseDTO> getAllOrders();
    Order getOrderById(Long orderId);
    void cancelOrder(Long orderId);
}
