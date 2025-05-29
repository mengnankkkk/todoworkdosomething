package com.mengnankk.shopping.service;

import com.mengnankk.shopping.entity.CartItem;
import java.util.List;

public interface CartService {
    void addToCart(String userId, Long productId, Integer quantity);
    void removeFromCart(String userId, Long productId);
    void updateQuantity(String userId, Long productId, Integer quantity);
    List<CartItem> getCartItems(String userId);
    void clearCart(String userId);
} 