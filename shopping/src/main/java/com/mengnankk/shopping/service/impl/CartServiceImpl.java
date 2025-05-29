package com.mengnankk.shopping.service.impl;

import com.mengnankk.shopping.entity.CartItem;
import com.mengnankk.shopping.entity.Product;
import com.mengnankk.shopping.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    
    private final RedisTemplate<String, Object> redisTemplate;
    private static final String CART_PREFIX = "cart:";
    private static final long CART_EXPIRE_DAYS = 7;

    @Override
    public void addToCart(String userId, Long productId, Integer quantity) {
        String cartKey = CART_PREFIX + userId;
        CartItem cartItem = (CartItem) redisTemplate.opsForHash().get(cartKey, productId.toString());
        
        if (cartItem == null) {
            cartItem = new CartItem();
            cartItem.setProductId(productId);
            cartItem.setQuantity(quantity);
            // TODO: 从商品服务获取商品信息
            // 这里模拟商品信息
            Product product = getProduct(productId);
            cartItem.setProductName(product.getName());
            cartItem.setPrice(product.getPrice());
        } else {
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
        }
        
        cartItem.setTotalPrice(cartItem.getPrice().multiply(java.math.BigDecimal.valueOf(cartItem.getQuantity())));
        redisTemplate.opsForHash().put(cartKey, productId.toString(), cartItem);
        redisTemplate.expire(cartKey, CART_EXPIRE_DAYS, TimeUnit.DAYS);
    }

    @Override
    public void removeFromCart(String userId, Long productId) {
        String cartKey = CART_PREFIX + userId;
        redisTemplate.opsForHash().delete(cartKey, productId.toString());
    }

    @Override
    public void updateQuantity(String userId, Long productId, Integer quantity) {
        String cartKey = CART_PREFIX + userId;
        CartItem cartItem = (CartItem) redisTemplate.opsForHash().get(cartKey, productId.toString());
        
        if (cartItem != null) {
            cartItem.setQuantity(quantity);
            cartItem.setTotalPrice(cartItem.getPrice().multiply(java.math.BigDecimal.valueOf(quantity)));
            redisTemplate.opsForHash().put(cartKey, productId.toString(), cartItem);
        }
    }

    @Override
    public List<CartItem> getCartItems(String userId) {
        String cartKey = CART_PREFIX + userId;
        Map<Object, Object> entries = redisTemplate.opsForHash().entries(cartKey);
        List<CartItem> cartItems = new ArrayList<>();
        
        entries.values().forEach(obj -> {
            if (obj instanceof CartItem) {
                cartItems.add((CartItem) obj);
            }
        });
        
        return cartItems;
    }

    @Override
    public void clearCart(String userId) {
        String cartKey = CART_PREFIX + userId;
        redisTemplate.delete(cartKey);
    }

    // 模拟获取商品信息
    private Product getProduct(Long productId) {
        // 这里应该调用商品服务获取商品信息，这里做模拟
        Product product = new Product();
        product.setId(productId);
        product.setName("商品" + productId);
        product.setPrice(new java.math.BigDecimal("99.99"));
        return product;
    }
} 