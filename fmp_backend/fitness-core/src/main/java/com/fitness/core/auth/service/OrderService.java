package com.fitness.core.auth.service;

import com.fitness.core.auth.domain.Order;
import com.fitness.core.auth.domain.OrderItem;
import com.fitness.core.auth.domain.CartItem;
import com.fitness.core.auth.domain.Product;
import com.fitness.core.auth.domain.ProductVariant;
import com.fitness.core.auth.domain.PosItem;
import com.fitness.core.auth.domain.Payment;
import com.fitness.core.auth.port.in.IOrderUseCase;
import com.fitness.core.auth.port.in.IInventoryUseCase;
import com.fitness.core.auth.port.out.IOrderRepositoryPort;
import com.fitness.core.auth.port.out.ICartRepositoryPort;
import com.fitness.core.auth.port.out.IProductRepositoryPort;
import com.fitness.core.auth.port.out.IPaymentRepositoryPort;
import com.fitness.core.common.exception.DomainException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderUseCase {

    private final IOrderRepositoryPort orderRepoPort;
    private final ICartRepositoryPort cartRepoPort;
    private final IProductRepositoryPort productRepoPort;
    private final IInventoryUseCase inventoryUseCase;
    private final IPaymentRepositoryPort paymentRepoPort;

    // Sinh mã đơn hàng tự động
    private String generateOrderNumber() {
        return "ORD-" + System.currentTimeMillis() + "-" + (int)(Math.random() * 900 + 100);
    }

    @Override
    @Transactional
    public Order checkoutFromCart(UUID userId, UUID branchId, UUID shippingAddressId, String paymentMethod) {
        var cartOpt = cartRepoPort.findCartByUserId(userId);
        if (cartOpt.isEmpty()) {
            throw new DomainException("CART_EMPTY", "Giỏ hàng không tồn tại");
        }

        List<CartItem> cartItems = cartRepoPort.findItemsByCartId(cartOpt.get().getId());
        if (cartItems.isEmpty()) {
            throw new DomainException("CART_EMPTY", "Giỏ hàng trống rỗng");
        }

        // Tạo trước ID đơn hàng để gán cho các OrderItem
        UUID orderId = UUID.randomUUID();

        BigDecimal total = BigDecimal.ZERO;
        List<OrderItem> orderItems = new ArrayList<>();

        for (CartItem item : cartItems) {
            Product product = productRepoPort.findProductById(item.getProductId())
                    .orElseThrow(() -> new DomainException("PRODUCT_NOT_FOUND", "Sản phẩm không tồn tại"));

            BigDecimal basePrice = product.getPrice();

            // Cộng thêm giá nếu có biến thể
            if (item.getVariantId() != null) {
                ProductVariant variant = product.getVariants().stream()
                        .filter(v -> v.getId().equals(item.getVariantId()))
                        .findFirst()
                        .orElseThrow(() -> new DomainException("VARIANT_NOT_FOUND", "Biến thể không hợp lệ"));

                basePrice = basePrice.add(variant.getPriceAdjustment());
            }

            // Cập nhật tồn kho
            inventoryUseCase.adjustStock(
                    branchId,
                    item.getProductId(),
                    item.getVariantId(),
                    "EXPORT",
                    item.getQuantity(),
                    userId
            );

            // Tính tổng tiền từng sản phẩm
            BigDecimal itemTotal = basePrice.multiply(BigDecimal.valueOf(item.getQuantity()));
            total = total.add(itemTotal);

            orderItems.add(OrderItem.builder()
                    .id(UUID.randomUUID())
                    .orderId(orderId) // Gán khóa ngoại đến đơn hàng
                    .productId(item.getProductId())
                    .variantId(item.getVariantId())
                    .quantity(item.getQuantity())
                    .unitPrice(basePrice)
                    .build());
        }

        // Tạo đơn hàng hoàn chỉnh trước khi lưu
        Order targetOrder = Order.builder()
                .id(orderId)
                .orderNumber(generateOrderNumber())
                .userId(userId)
                .branchId(branchId)
                .totalAmount(total)
                .discountAmount(BigDecimal.ZERO)
                .finalAmount(total)
                .orderStatus("PENDING")
                .shippingAddressId(shippingAddressId)
                .createdAt(LocalDateTime.now())
                .build();

        // Lưu đơn hàng, chi tiết đơn
        Order savedOrder = orderRepoPort.saveOrder(targetOrder);
        orderRepoPort.saveOrderItems(orderItems);

        //  TỰ ĐỘNG ĐÚC VÀ LƯU BẢN GHI THANH TOÁN ONLINE TẠI ĐÂY
        Payment targetPayment = Payment.builder()
                .id(UUID.randomUUID()) // Sinh mã paymentId mới tinh
                .orderId(orderId)
                .userId(userId)
                .amount(total)         // Số tiền khớp khít với tổng tiền đơn hàng
                .status("PENDING")     // Trạng thái chờ thanh toán ngân hàng/momo
                .paymentMethod(paymentMethod)
                .createdAt(LocalDateTime.now())
                .build();
        paymentRepoPort.savePayment(targetPayment); // Gọi Port đẩy dữ liệu thật xuống DB

        orderRepoPort.clearCartItems(userId);
        savedOrder.setItems(orderItems);
        return savedOrder;
    }

    @Override
    @Transactional
    public Order createPosOrder(UUID branchId, UUID customerUserId, List<PosItem> items, UUID staffId) {
        if (items == null || items.isEmpty()) {
            throw new DomainException("ORDER_ITEMS_EMPTY", "Hóa đơn POS phải có ít nhất 1 mặt hàng");
        }

        // Tạo trước ID đơn hàng POS
        UUID orderId = UUID.randomUUID();

        BigDecimal total = BigDecimal.ZERO;
        List<OrderItem> orderItems = new ArrayList<>();

        for (PosItem reqItem : items) {
            Product product = productRepoPort.findProductById(reqItem.getProductId())
                    .orElseThrow(() -> new DomainException("PRODUCT_NOT_FOUND", "Sản phẩm không tồn tại"));

            BigDecimal basePrice = product.getPrice();

            // Cộng thêm giá nếu có biến thể
            if (reqItem.getVariantId() != null) {
                ProductVariant variant = product.getVariants().stream()
                        .filter(v -> v.getId().equals(reqItem.getVariantId()))
                        .findFirst()
                        .orElseThrow(() -> new DomainException("VARIANT_NOT_FOUND", "Biến thể không đúng"));

                basePrice = basePrice.add(variant.getPriceAdjustment());
            }

            // Cập nhật tồn kho
            inventoryUseCase.adjustStock(
                    branchId,
                    reqItem.getProductId(),
                    reqItem.getVariantId(),
                    "EXPORT",
                    reqItem.getQuantity(),
                    staffId
            );

            // Tính tổng tiền từng sản phẩm
            BigDecimal itemTotal = basePrice.multiply(BigDecimal.valueOf(reqItem.getQuantity()));
            total = total.add(itemTotal);

            orderItems.add(OrderItem.builder()
                    .id(UUID.randomUUID())
                    .orderId(orderId) // Gán khóa ngoại đến đơn hàng
                    .productId(reqItem.getProductId())
                    .variantId(reqItem.getVariantId())
                    .quantity(reqItem.getQuantity())
                    .unitPrice(basePrice)
                    .build());
        }

        // Tạo đơn hàng POS hoàn chỉnh
        Order targetOrder = Order.builder()
                .id(orderId)
                .orderNumber(generateOrderNumber())
                .userId(customerUserId)
                .branchId(branchId)
                .totalAmount(total)
                .discountAmount(BigDecimal.ZERO)
                .finalAmount(total)
                .orderStatus("COMPLETED")
                .shippingAddressId(null)
                .createdAt(LocalDateTime.now())
                .build();

        // Lưu đơn hàng và chi tiết đơn
        Order savedOrder = orderRepoPort.saveOrder(targetOrder);
        orderRepoPort.saveOrderItems(orderItems);

        // Lưu hóa đơn thanh toán tiền mặt thành công trực tiếp để đồng bộ báo cáo tài chính
        Payment posPayment = Payment.builder()
                .id(UUID.randomUUID())
                .orderId(orderId)
                .userId(customerUserId)
                .amount(total)
                .status("SUCCESS")
                .paymentMethod("CASH") // Tiền mặt trao tay quầy POS
                .createdAt(LocalDateTime.now())
                .build();
        paymentRepoPort.savePayment(posPayment);

        savedOrder.setItems(orderItems);
        return savedOrder;
    }

    @Override
    public Order getOrderById(UUID orderId) {
        Order order = orderRepoPort.findOrderById(orderId)
                .orElseThrow(() -> new DomainException("ORDER_NOT_FOUND", "Hóa đơn không tồn tại"));

        // Gắn danh sách sản phẩm của đơn hàng
        order.setItems(orderRepoPort.findItemsByOrderId(orderId));
        return order;
    }

    @Override
    public List<Order> getMemberOrderHistory(UUID userId) {
        // Lấy lịch sử mua hàng của member
        return orderRepoPort.findOrdersByUserId(userId);
    }
}