package com.bestseller.starbux.domain.dto;

public class OrderDTO {
    private int originalAmount;
    private int discountedAmount;
    private CartDTO cart;

    public OrderDTO(int originalAmount, int discountedAmount, CartDTO cart) {
        this.originalAmount = originalAmount;
        this.discountedAmount = discountedAmount;
        this.cart = cart;
    }

    public int getOriginalAmount() {
        return originalAmount;
    }

    public int getDiscountedAmount() {
        return discountedAmount;
    }

    public CartDTO getCart() {
        return cart;
    }
}
