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
    public Order placeOrder(Order order) {

        UserClient.UserResponse user;
        ProductClient.ProductResponse product;

        try {
            user = userClient.getUserById(order.getUserId());
        } catch (FeignException.NotFound e) {
            throw new UserNotFoundException("User Not Found With User ID : " + order.getUserId());
        }

        try {
            product = productClient.getProductById(order.getProductId());
        } catch (FeignException.NotFound e) {
            throw new ProductNotFoundException("Product Not Found With Product ID : " + order.getProductId());
        }

        order.setUserName(user.getName());
        order.setProductName(product.getName());
        order.setTotalPrice(product.getPrice() * order.getQuantity());

        return orderRepository.save(order);
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