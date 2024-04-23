package com.bfu.orderservice.service.OrderProduct;

import com.bfu.orderservice.controller.payload.ArrayOfSimplifiedProduct;
import com.bfu.orderservice.controller.payload.DeleteProductsFromOrderRequest;
import com.bfu.orderservice.controller.payload.SimplifiedProductResponse;
import com.bfu.orderservice.entity.Order;
import com.bfu.orderservice.entity.OrderProduct;
import com.bfu.orderservice.exceptions.OrderProductsNotFoundException;
import com.bfu.orderservice.repository.OrderProductRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        Optional<List<OrderProduct>> orderProducts = orderProductRepository.findAllByOrder_OrderId(orderId);
        if (orderProducts.isPresent()) {
            return orderProducts.get().stream().map(SimplifiedProductResponse::from).toList();
        }
        else
            throw OrderProductsNotFoundException.of(orderId);
    }

    @Override
    public void deleteOrderProduct(String orderId) {
        Optional<List<OrderProduct>> products = orderProductRepository.findAllByOrder_OrderId(orderId);
        products.ifPresent(orderProductRepository::deleteAll);
    }

    @Override
    public void deleteProductsOrder(DeleteProductsFromOrderRequest request) {
        orderProductRepository.findAllByOrder_OrderIdAndProductIdIn(request.orderId(), request.productsId())
                .ifPresent(orderProductRepository::deleteAll);

    }

    @Override
    public void addProductsToOrder(Order order, List<SimplifiedProductResponse> simplifiedProductList) {
        orderProductRepository.saveAll(
                simplifiedProductList.stream()
                        .map(p->OrderProduct.from(p, order))
                        .toList()
        );
    }
}
