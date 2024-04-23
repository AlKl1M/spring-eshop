package com.bfu.orderservice.controller;

import com.bfu.orderservice.client.OrderServiceClient;
import com.bfu.orderservice.controller.payload.*;
import com.bfu.orderservice.entity.Order;
import com.bfu.orderservice.service.Order.OrderService;
import com.bfu.orderservice.service.OrderProduct.OrderProductService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.ast.Or;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

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
        return ResponseEntity.ok("Order has been created successfully");
    }

    @PutMapping("/change-order-status")
    public ResponseEntity<?> changeStatus(@RequestBody ChangeStatusRequest changeStatusRequest){
        log.info("Start to change status order");
        orderService.changeStatus(changeStatusRequest);
        return ResponseEntity.ok("Status has been changed successfully");
    }

    @PutMapping("/add-products-to-order")
    public ResponseEntity<?> addProductsToOrder(@RequestBody AddProductsToOrderRequest productsToOrderRequest){
        log.info("Start to add product to order");
        Order order = orderService.getOrderByOrderId(productsToOrderRequest.orderId());
        orderProductService.addProductsToOrder(order, productsToOrderRequest.products());
        log.info("Add products to order successfully");
        return ResponseEntity.ok("Products add to Order successfully");
    }

    @GetMapping("/get-order")
    public List<OrderResponse> getOrdersByUserId(Principal principal){
        log.info("Start to getting orders by orderId");
        String userId = ((JwtAuthenticationToken) principal).getToken().getSubject();
        return orderService.getOrdersByUserId(userId)
                .stream().map(p->
                        OrderResponse.from(
                                p,
                                orderProductService.getOrderProducts(p.getOrderId()))
                ).toList();
    }
    @GetMapping("/get-order/")
    public OrderResponse getOrderByOrderId(Principal principal, @RequestParam String orderId){
        log.info("Start to getting order by orderId");
        Order order = orderService.getOrderByOrderId(orderId);
        return OrderResponse.from(
                            order,
                            orderProductService.getOrderProducts(order.getOrderId())
                    );
    }

    @DeleteMapping("/delete-order")
    public ResponseEntity<?> deleteOrder(String orderId) {
        log.info("Start to delete order");
        orderService.deleteOrder(orderId);
        orderProductService.deleteOrderProduct(orderId);
        return ResponseEntity.noContent().build();
    }
    @DeleteMapping("/delete-products-order")
    public ResponseEntity<?> deleteProductsOrder(@RequestBody DeleteProductsFromOrderRequest request){
        log.info("Start to delete product from order");
        orderProductService.deleteProductsOrder(request);
        return ResponseEntity.noContent().build();
    }
}
