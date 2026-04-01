package com.example.order_service.dto;

public class OrderResponseDTO {
    private Long orderId;
    private String productName;
    private String userName;
    private int quantity;
    private double totalPrice;

    public OrderResponseDTO(Long orderId, String productName, String userName, int quantity, double totalPrice) {
        this.orderId = orderId;
        this.productName = productName;
        this.userName = userName;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
}