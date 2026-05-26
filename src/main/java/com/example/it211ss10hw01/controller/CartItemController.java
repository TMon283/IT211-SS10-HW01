package com.example.it211ss10hw01.controller;

import com.example.it211ss10hw01.model.dto.response.ApiDataResponse;
import com.example.it211ss10hw01.model.entity.CartItem;
import com.example.it211ss10hw01.service.CartItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartItemController {
    private final CartItemService cartItemService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiDataResponse<List<CartItem>>> getCartItems(@PathVariable String userId) {
        List<CartItem> items = cartItemService.getCartByUser(userId);
        if (items == null || items.isEmpty()) {
            return new ResponseEntity<>(new ApiDataResponse<>(
                    false,
                    "Không tìm thấy giỏ hàng của user có id=" + userId,
                    null,
                    HttpStatus.NOT_FOUND
            ), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(new ApiDataResponse<>(
                true,
                "Lấy giỏ hàng của user có id=" + userId + " thành công",
                items,
                HttpStatus.OK
        ), HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<ApiDataResponse<CartItem>> addCartItem(@RequestBody CartItem cartItem) {
        if (cartItem.getQuantity() == null || cartItem.getQuantity() <= 0 ||
                cartItem.getProductId() == null || cartItem.getProductId().isBlank()) {
            return new ResponseEntity<>(new ApiDataResponse<>(
                    false,
                    "Dữ liệu không hợp lệ: quantity phải >=1 và productId không được rỗng",
                    null,
                    HttpStatus.BAD_REQUEST
            ), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new ApiDataResponse<>(
                true,
                "Thêm sản phẩm vào giỏ hàng thành công",
                cartItemService.addToCart(cartItem),
                HttpStatus.CREATED
        ), HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiDataResponse<CartItem>> updateCartItem(
            @PathVariable Long id,
            @RequestParam Integer quantity) {

        if (quantity == null || quantity <= 0) {
            return new ResponseEntity<>(new ApiDataResponse<>(
                    false,
                    "Quantity phải >= 1",
                    null,
                    HttpStatus.BAD_REQUEST
            ), HttpStatus.BAD_REQUEST);
        }

        CartItem existing = cartItemService.getCartItemById(id);
        if (existing == null) {
            return new ResponseEntity<>(new ApiDataResponse<>(
                    false,
                    "Không tìm thấy giỏ hàng có id=" + id,
                    null,
                    HttpStatus.NOT_FOUND
            ), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(new ApiDataResponse<>(
                true,
                "Sửa thông tin thành công",
                cartItemService.updateCartItem(id, quantity),
                HttpStatus.OK
        ), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiDataResponse<Boolean>> deleteCartItem(@PathVariable Long id) {
        CartItem existing = cartItemService.getCartItemById(id);
        if (existing == null) {
            return new ResponseEntity<>(new ApiDataResponse<>(
                    false,
                    "Không tìm thấy giỏ hàng có id=" + id,
                    null,
                    HttpStatus.NOT_FOUND
            ), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(new ApiDataResponse<>(
                true,
                "Xóa giỏ hàng có id=" + id + " thành công",
                cartItemService.deleteCartItem(id),
                HttpStatus.OK
        ), HttpStatus.OK);
    }

    @GetMapping("/item/{id}")
    public ResponseEntity<ApiDataResponse<CartItem>> getCartItem(@PathVariable Long id) {
        CartItem item = cartItemService.getCartItemById(id);
        if (item == null) {
            return new ResponseEntity<>(new ApiDataResponse<>(
                    false,
                    "Không tìm thấy giỏ hàng có id=" + id,
                    null,
                    HttpStatus.NOT_FOUND
            ), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(new ApiDataResponse<>(
                true,
                "Lấy giỏ hàng có id=" + id + " thành công",
                item,
                HttpStatus.OK
        ), HttpStatus.OK);
    }
}

