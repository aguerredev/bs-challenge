package com.bestseller.starbux.domain.dto;

import com.bestseller.starbux.domain.Beverage;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class CartDTO {
    private int userId;
    private String userName;
    @JsonProperty("Cart")
    private List<Beverage> beverageList;
    private int cartAmount;

    public CartDTO(int userId, String userName) {
        this.userId = userId;
        this.userName = userName;
        this.beverageList = new ArrayList<>();
    }

    public int getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public List<Beverage> getBeverageList() {
        return beverageList;
    }

    public int getCartAmount() {
        return cartAmount;
    }

    public void setCartAmount(int cartAmount) {
        this.cartAmount = cartAmount;
    }
}
