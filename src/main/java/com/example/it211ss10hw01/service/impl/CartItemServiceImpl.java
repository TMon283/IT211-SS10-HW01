package com.example.it211ss10hw01.service.impl;

import com.example.it211ss10hw01.model.entity.CartItem;
import com.example.it211ss10hw01.repository.CartItemRepository;
import com.example.it211ss10hw01.service.CartItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartItemServiceImpl implements CartItemService {
    private final CartItemRepository cartItemRepository;

    @Override
    public CartItem addToCart(CartItem request) {
        log.info("Yêu cầu thêm sản phẩm vào giỏ: userId={}, productId={}, quantity={}",
                request.getUserId(), request.getProductId(), request.getQuantity());

        if (request.getQuantity() == null || request.getQuantity() <= 0) {
            log.warn("Thao tác bất thường: quantity không hợp lệ ({}). Bỏ qua request.", request.getQuantity());
            return null;
        }
        if (request.getProductId() == null || request.getProductId().isBlank()) {
            log.warn("Thao tác bất thường: productId rỗng. Bỏ qua request.");
            return null;
        }

        Optional<CartItem> existing = cartItemRepository.findByUserIdAndProductId(request.getUserId(), request.getProductId());
        if (existing.isPresent()) {
            CartItem item = existing.get();
            item.setQuantity(item.getQuantity() + request.getQuantity());
            CartItem updated = cartItemRepository.save(item);
            log.info("Cập nhật số lượng sản phẩm thành công: {}", updated);
            return updated;
        } else {
            CartItem saved = cartItemRepository.save(request);
            log.info("Thêm mới sản phẩm vào giỏ thành công: {}", saved);
            return saved;
        }
    }

    @Override
    public List<CartItem> getCartByUser(String userId) {
        log.info("Yêu cầu lấy giỏ hàng cho userId={}", userId);
        List<CartItem> items = cartItemRepository.findByUserId(userId);
        if (items == null || items.isEmpty()) {
            log.warn("Không tìm thấy sản phẩm nào trong giỏ cho userId={}", userId);
        } else {
            log.info("Tìm thấy {} sản phẩm trong giỏ cho userId={}", items.size(), userId);
        }
        return items;
    }

    @Override
    public CartItem updateCartItem(Long id, Integer quantity) {
        log.info("Yêu cầu cập nhật CartItem id={}, quantity={}", id, quantity);
        return cartItemRepository.findById(id)
                .map(item -> {
                    item.setQuantity(quantity);
                    CartItem updated = cartItemRepository.save(item);
                    log.info("Cập nhật thành công CartItem: {}", updated);
                    return updated;
                })
                .orElseGet(() -> {
                    log.warn("Không tìm thấy CartItem id={}", id);
                    return null;
                });
    }

    @Override
    public boolean deleteCartItem(Long id) {
        log.info("Yêu cầu xóa CartItem id={}", id);
        if (cartItemRepository.existsById(id)) {
            cartItemRepository.deleteById(id);
            log.info("Xóa thành công CartItem id={}", id);
            return true;
        } else {
            log.warn("Không tìm thấy CartItem id={}", id);
            return false;
        }
    }

    @Override
    public CartItem getCartItemById(Long id) {
        log.info("Yêu cầu lấy CartItem theo id={}", id);
        return cartItemRepository.findById(id)
                .orElseGet(() -> {
                    log.warn("Không tìm thấy CartItem id={}", id);
                    return null;
                });
    }
}
