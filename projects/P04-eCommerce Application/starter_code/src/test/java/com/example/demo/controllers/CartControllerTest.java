package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CartControllerTest {

    private CartController cartController;

    private UserRepository userRepo = mock(UserRepository.class);

    private CartRepository cartRepo = mock(CartRepository.class);

    private ItemRepository itemRepo = mock(ItemRepository.class);

    @Before
    public void setup() {
        cartController = new CartController();
        TestUtils.injectObjects(cartController, "userRepository", userRepo);
        TestUtils.injectObjects(cartController, "cartRepository", cartRepo);
        TestUtils.injectObjects(cartController, "itemRepository", itemRepo);
    }

    @Test
    public void add_item_to_cart_happy_path() {
        Cart emptyCart = new Cart();

        User user = new User();
        user.setUsername("addejans");
        user.setCart(emptyCart);

        Item item = new Item();
        item.setName("Pencil");
        item.setPrice(new BigDecimal(234.99));

        when(userRepo.findByUsername(anyString())).thenReturn(user);
        when(itemRepo.findById(anyLong())).thenReturn(Optional.of(item));

        ResponseEntity<Cart> responseEntity = cartController.addTocart(
                new ModifyCartRequest("addejans", 1, 5)
        );

        assertEquals(200, responseEntity.getStatusCodeValue());

        Cart cart = responseEntity.getBody();

        assertNotNull(cart);
        assertEquals(new BigDecimal(234.99*5), cart.getTotal());
        assertEquals(Arrays.asList(item, item, item, item, item), cart.getItems());
    }

    @Test
    public void remove_item_from_cart_happy_path() {
        Cart cart = new Cart();

        User user = new User();
        user.setUsername("addejans");
        user.setCart(cart);

        Item item = new Item();
        item.setName("Pencil");
        item.setPrice(new BigDecimal(234.99));

        cart.addItem(item);

        when(userRepo.findByUsername(anyString())).thenReturn(user);
        when(itemRepo.findById(anyLong())).thenReturn(Optional.of(item));

        ResponseEntity<Cart> responseEntity = cartController.removeFromcart(
                new ModifyCartRequest("username", 1, 1)
        );

        assertEquals(200, responseEntity.getStatusCodeValue());

        Cart emptyCart = responseEntity.getBody();

        assertNotNull(emptyCart);
        // https://stackoverflow.com/questions/20645922/how-do-force-bigdecimal-from-rounding-in-junit-assertequals
//        assertEquals(new BigDecimal("0"), emptyCart.getTotal());
        BigDecimal expectedCartValue = (new BigDecimal("0"));
        BigDecimal actualCartValue = emptyCart.getTotal();
        Double epsilon = Double.valueOf(expectedCartValue.subtract(actualCartValue).toString());
        assertTrue(epsilon < 0.0001);
        assertEquals(0, emptyCart.getItems().size());
    }

}
