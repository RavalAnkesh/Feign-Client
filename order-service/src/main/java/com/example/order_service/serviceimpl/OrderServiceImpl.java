package com.example.order_service.serviceimpl;

import com.example.order_service.dto.OrderResponseDTO;
import com.example.order_service.entity.Order;
import com.example.order_service.exception.OrderNotFoundException;
import com.example.order_service.exception.ProductNotFoundException;
import com.example.order_service.exception.UserNotFoundException;
import com.example.order_service.feign.ProductClient;
import com.example.order_service.feign.UserClient;
import com.example.order_service.repository.OrderRepository;
import com.example.order_service.servicei.OrderServiceI;
import feign.FeignException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderServiceI {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserClient userClient;

    @Autowired
    private ProductClient productClient;

    @Override
    @CircuitBreaker(name="UserProductCB", fallbackMethod = "placeOrderFallback")
    public Order placeOrUpdateOrder(Order order) {

        UserClient.UserResponse user;
        ProductClient.ProductResponse product;

        try {
            user = userClient.getUserById(order.getUserId());
        } catch (FeignException.NotFound e) {
            throw new UserNotFoundException("User Not Found With User ID: " + order.getUserId());
        }

        try {
            product = productClient.getProductById(order.getProductId());
        } catch (FeignException.NotFound e) {
            throw new ProductNotFoundException("Product Not Found With Product ID: " + order.getProductId());
        }

        Order orderToSave;

        if (order.getOrderId() != null) {
            orderToSave = orderRepository.findById(order.getOrderId())
                    .orElseThrow(() -> new OrderNotFoundException("Order Not Found With OrderID: " + order.getOrderId()));

            orderToSave.setUserId(order.getUserId());
            orderToSave.setProductId(order.getProductId());
            orderToSave.setQuantity(order.getQuantity());
        } else {
            orderToSave = new Order();
            orderToSave.setUserId(order.getUserId());
            orderToSave.setProductId(order.getProductId());
            orderToSave.setQuantity(order.getQuantity());
        }

        orderToSave.setUserName(user.getName());
        orderToSave.setProductName(product.getName());
        orderToSave.setTotalPrice(product.getPrice() * order.getQuantity());

        return orderRepository.save(orderToSave);
    }
    public Order placeOrderFallback(Order order, Throwable t) {
        System.out.println("Circuit breaker triggered: " + t.getMessage());
        order.setOrderId(null);
        order.setUserId(null);
        order.setProductId(null);
        order.setUserName(null);
        order.setProductName(null);
        order.setQuantity(0);
        order.setTotalPrice(0.0);

        order.setMessage("Service temporarily unavailable. Please try again later.");
        return order;
    }
    @Override
    public List<OrderResponseDTO> getAllOrders() {
        List<Order> orders = orderRepository.findAll();

        return orders.stream().map(order ->
                new OrderResponseDTO(
                        order.getOrderId(),
                        order.getUserName(),
                        order.getProductName(),
                        order.getQuantity(),
                        order.getTotalPrice()
                )
        ).toList();
    }

    @Override
    public Order getOrderById(Long orderId) {
       return orderRepository
               .findById(orderId)
               .orElseThrow(()->new OrderNotFoundException("OrderNot Found With OrderID : "+orderId));
    }

    @Override
    public void cancelOrder(Long orderId) {
        Order order = orderRepository
                .findById(orderId)
                .orElseThrow(()->new OrderNotFoundException("order Not Found With ID : "+orderId));
        orderRepository.delete(order);
    }
}