package com.bestseller.starbux.domain.dto;

public class OrdersPerCustomerDTO {
    private int userId;
    private String userName;
    private int totalAmountOfOrders;

    public OrdersPerCustomerDTO(int userId, String userName, int totalAmountOfOrders) {
        this.userId = userId;
        this.userName = userName;
        this.totalAmountOfOrders = totalAmountOfOrders;
    }

    public int getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public int getTotalAmountOfOrders() {
        return totalAmountOfOrders;
    }
}
