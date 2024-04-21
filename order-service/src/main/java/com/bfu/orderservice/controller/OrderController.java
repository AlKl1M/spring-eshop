package com.bfu.orderservice.controller;

import com.bfu.orderservice.client.OrderServiceClient;
import com.bfu.orderservice.controller.payload.ArrayOfSimplifiedProduct;
import com.bfu.orderservice.controller.payload.OrderResponse;
import com.bfu.orderservice.controller.payload.SimplifiedProductResponse;
import com.bfu.orderservice.entity.Order;
import com.bfu.orderservice.entity.OrderProduct;
import com.bfu.orderservice.service.Order.OrderService;
import com.bfu.orderservice.service.OrderProduct.OrderProductService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.sql.Update;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/order")
@Slf4j
@AllArgsConstructor
public class OrderController {
    private final OrderServiceClient client;
    private final OrderService orderService;
    private final OrderProductService orderProductService;
    @PostMapping("/create-order")
    public ResponseEntity<?> createOrder(Principal principal){
        @Valid ArrayOfSimplifiedProduct products = client.getCart();
        String userId = ((JwtAuthenticationToken) principal).getToken().getSubject();
        log.info("Start to create order");
        Order order = orderService.createOrder(userId);
        log.info("Start to create OrderProduct");
        orderProductService.addOrderProduct(products, order);
        return ResponseEntity.ok("Order has been created");
    }
    @PostMapping("/test-create-order")
    public ResponseEntity<?> testCreateOrder(@RequestBody ArrayOfSimplifiedProduct products, Principal principal){
        String userId = ((JwtAuthenticationToken) principal).getToken().getSubject();
        log.info("Start to create order");
        Order order = orderService.createOrder(userId);
        log.info("Start to create OrderProduct");
        orderProductService.addOrderProduct(products, order);
        return ResponseEntity.ok("Order has been created");
    }

    @GetMapping("/get-order")
    public List<OrderResponse> getOrder(Principal principal){
        String userId = ((JwtAuthenticationToken) principal).getToken().getSubject();
        List<OrderResponse> responseList = new ArrayList<>();
        for (Order order: orderService.getOrder(userId)){
            responseList.add(OrderResponse.from(
                    order,
                    orderProductService.getOrderProducts(order.getOrderId())
                    )
            );
        }
        return responseList;
    }

    @DeleteMapping("/delete-order")
    public ResponseEntity<?> deleteOrder(String orderId) {
        orderService.deleteOrder(orderId);
        orderProductService.deleteOrderProduct(orderId);
        return ResponseEntity.noContent().build();
    }
}
