package com.bfu.orderservice.service.OrderProduct;

import com.bfu.orderservice.controller.payload.ArrayOfSimplifiedProduct;
import com.bfu.orderservice.controller.payload.SimplifiedProductResponse;
import com.bfu.orderservice.entity.Order;
import com.bfu.orderservice.entity.OrderProduct;
import com.bfu.orderservice.repository.OrderProductRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class OrderProductServiceImpl implements OrderProductService{
    private final OrderProductRepository orderProductRepository;
    @Override
    public void addOrderProduct(ArrayOfSimplifiedProduct products, Order order) {
        for (SimplifiedProductResponse product: products.products()) {
            orderProductRepository.save(
                    OrderProduct.builder()
                            .name(product.name())
                            .price(product.price())
                            .quantity(product.quantity())
                            .productId(product.productId())
                            .order(order)
                            .build()
            );
        }
    }

    @Override
    public List<SimplifiedProductResponse> getOrderProducts(String orderId) {
        List<SimplifiedProductResponse> products = new ArrayList<>();
        Optional<List<OrderProduct>> orderProducts = orderProductRepository.findAllByOrder_OrderId(orderId);
        for(OrderProduct product: orderProducts.get()){
            products.add(
                    SimplifiedProductResponse.from(product)
            );
        }
        return products;
    }

    @Override
    public void deleteOrderProduct(String orderId) {
        Optional<List<OrderProduct>> products = orderProductRepository.findAllByOrder_OrderId(orderId);
        products.ifPresent(orderProductRepository::deleteAll);
    }
}
