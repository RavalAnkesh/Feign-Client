package com.example.order_service.controller;

import com.example.order_service.dto.OrderResponseDTO;
import com.example.order_service.entity.Order;
import com.example.order_service.serviceimpl.OrderServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderServiceImpl orderServiceImpl;

    @PostMapping
    public ResponseEntity<Order> placeOrUpdateOrder(@RequestBody Order order) {
        Order savedOrder = orderServiceImpl.placeOrUpdateOrder(order);
        return new ResponseEntity<>(savedOrder, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<OrderResponseDTO>> getAllOrders(){
        List<OrderResponseDTO> allOrders = orderServiceImpl.getAllOrders();
        if(allOrders.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(allOrders, HttpStatus.OK);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<?> getOrderById(@PathVariable Long orderId){
        Order order = orderServiceImpl.getOrderById(orderId);
        if(order == null){
            return new ResponseEntity<>("Order not found", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<String> cancelOrder(@PathVariable Long orderId){
        Order order = orderServiceImpl.getOrderById(orderId);
        if(order == null){
            return new ResponseEntity<>("Order not found", HttpStatus.NOT_FOUND);
        }
        orderServiceImpl.cancelOrder(orderId);
        return new ResponseEntity<>("Order cancelled successfully", HttpStatus.OK);
    }
}