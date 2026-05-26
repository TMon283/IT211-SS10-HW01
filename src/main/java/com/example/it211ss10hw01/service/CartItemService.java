package com.example.it211ss10hw01.service;

import com.example.it211ss10hw01.model.entity.CartItem;

import java.util.List;

public interface CartItemService {
    CartItem addToCart(CartItem request);
    List<CartItem> getCartByUser(String userId);
    CartItem updateCartItem(Long id, Integer quantity);
    boolean deleteCartItem(Long id);
    CartItem getCartItemById(Long id);
}

