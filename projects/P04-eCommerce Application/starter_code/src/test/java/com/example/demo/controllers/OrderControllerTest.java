package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderControllerTest {

    private OrderController orderController;

    private UserRepository userRepo = mock(UserRepository.class);

    private OrderRepository orderRepo = mock(OrderRepository.class);


    @Before
    public void setup() {
        orderController = new OrderController();
        TestUtils.injectObjects(orderController, "userRepository", userRepo);
        TestUtils.injectObjects(orderController, "orderRepository", orderRepo);
    }

    @Test
    public void submit_order_happy_path() {
        User user = createDummyUser();

        when(userRepo.findByUsername(anyString())).thenReturn(user);
        ResponseEntity<UserOrder> responseEntity = orderController.submit(user.getUsername());

        assertEquals(200, responseEntity.getStatusCodeValue());

        UserOrder order = responseEntity.getBody();
        assertNotNull(order);
        assertEquals(user.getCart().getItems(), order.getItems());
        assertEquals(user.getCart().getTotal(), order.getTotal());
        assertEquals(user.getUsername(), order.getUser().getUsername());
    }

    @Test
    public void submit_order_fail_user_not_found() {
        when(userRepo.findByUsername(anyString())).thenReturn(null);
        ResponseEntity<UserOrder> responseEntity = orderController.submit("unregistered_user");

        assertEquals(404, responseEntity.getStatusCodeValue());
    }


    @Test
    public void get_user_order_happy_path() {
        List<UserOrder> orders = createDummyUserOrder();
        when(userRepo.findByUsername(anyString())).thenReturn(new User());
        when(orderRepo.findByUser(any(User.class))).thenReturn(orders);
        ResponseEntity<List<UserOrder>> responseEntity = orderController.getOrdersForUser("username");

        assertEquals(200, responseEntity.getStatusCodeValue());
        List<UserOrder> orderList = responseEntity.getBody();
        assertNotNull(orderList);
        assertEquals(orders.size(), orderList.size());
        assertEquals(orders.get(0).getId(), orderList.get(0).getId());
        assertEquals(orders.get(0).getUser().getId(), orderList.get(0).getUser().getId());
    }

    @Test
    public void get_user_order_fail_user_not_found() {
        when(userRepo.findByUsername(anyString())).thenReturn(null);
        ResponseEntity<UserOrder> responseEntity = orderController.submit("unregistered_user");
        assertEquals(404, responseEntity.getStatusCodeValue());
    }

    private Item createDummyItem() {
        Item item = new Item();
        item.setId(1L);
        item.setPrice(new BigDecimal(234.99));
        item.setName("Pencil");
        item.setDescription("World's most expensive pencil!");
        return item;
    }

    private User createDummyUser() {
        Item item = createDummyItem();
        Cart cart = new Cart();
        User user = new User();
        user.setId(1L);
        user.setUsername("addejans");
        user.setCart(cart);
        cart.setUser(user);
        cart.addItem(item);
        return user;
    }

    private List<UserOrder> createDummyUserOrder() {
        Item item = createDummyItem();
        User user = new User();
        user.setId(1L);
        UserOrder order = new UserOrder();
        order.setId(10L);
        order.setItems(Collections.singletonList(item));
        order.setUser(user);
        return Collections.singletonList(order);
    }
}