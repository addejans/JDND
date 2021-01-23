package com.example.demo.controllers;


import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemControllerTest {

    private ItemController itemController;

    private ItemRepository itemRepo = mock(ItemRepository.class);

    @Before
    public void setup() {
        itemController = new ItemController();
        TestUtils.injectObjects(itemController, "itemRepository", itemRepo);
    }

    @Test
    public void getItemsByName_Success() {
        List<Item> items = createDummyItemList();

        when(itemRepo.findByName(anyString())).thenReturn(items);
        ResponseEntity<List<Item>> responseEntity = itemController.getItemsByName("addejans");

        assertEquals(200, responseEntity.getStatusCodeValue());
        List<Item> responseList = responseEntity.getBody();
        assertNotNull(responseList);
        assertEquals(items.size(), responseList.size());
        assertEquals(items.get(0).getId(), responseList.get(0).getId());
    }

    private List<Item> createDummyItemList() {
        Item item = new Item();
        item.setId(1L);
        return Collections.singletonList(item);
    }

}