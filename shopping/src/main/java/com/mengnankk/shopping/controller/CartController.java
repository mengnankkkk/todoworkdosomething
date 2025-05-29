package com.mengnankk.shopping.controller;

import com.mengnankk.shopping.entity.CartItem;
import com.mengnankk.shopping.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "购物车接口")
@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @Operation(summary = "添加商品到购物车")
    @PostMapping("/add")
    public void addToCart(@RequestParam String userId,
                         @RequestParam Long productId,
                         @RequestParam Integer quantity) {
        cartService.addToCart(userId, productId, quantity);
    }

    @Operation(summary = "从购物车移除商品")
    @DeleteMapping("/remove")
    public void removeFromCart(@RequestParam String userId,
                             @RequestParam Long productId) {
        cartService.removeFromCart(userId, productId);
    }

    @Operation(summary = "更新购物车商品数量")
    @PutMapping("/update")
    public void updateQuantity(@RequestParam String userId,
                             @RequestParam Long productId,
                             @RequestParam Integer quantity) {
        cartService.updateQuantity(userId, productId, quantity);
    }

    @Operation(summary = "获取购物车商品列表")
    @GetMapping("/list")
    public List<CartItem> getCartItems(@RequestParam String userId) {
        return cartService.getCartItems(userId);
    }

    @Operation(summary = "清空购物车")
    @DeleteMapping("/clear")
    public void clearCart(@RequestParam String userId) {
        cartService.clearCart(userId);
    }
} 