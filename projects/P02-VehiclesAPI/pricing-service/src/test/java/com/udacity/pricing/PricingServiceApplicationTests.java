package com.udacity.pricing;

import com.udacity.pricing.domain.price.Price;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class PricingServiceApplicationTests {

	String BASE_URL = "http://localhost:";

//	@Autowired
//	MockMvc mockMvc;

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	public void contextLoads() {
	}

	@Test
	public void testGetPriceById() {
		ResponseEntity<Price> response =
				this.restTemplate.getForEntity(BASE_URL + port + "/services/price/?vehicleId=1", Price.class);
		assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
		//TODO: Read from data.sql
//		assertThat(response.getBody().getPrice().intValue(), equalTo(10000));
	}


	//TODO: Check this out and implement later (given time)
//	@Test
//	public void testGetAllPrices() {
//		ResponseEntity<List> response =
//				restTemplate.getForEntity(BASE_URL + port + "/prices", List.class);
//		assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
//	}

//	@Test
//	public void getPrice() throws Exception {
//		mockMvc.perform(get("/services/price/?vehicleId=1"))
//				.andExpect(status().isOk())
//				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
//				.andExpect(content().json("{}"));
//	}
//
//	@Test
//	public void testInitializedRepositorySampleData() {
//		Iterable<Price> prices = priceRepository.findAll();
//		assertThat(prices, is(iterableWithSize(5)));
//		assertNotNull(prices.iterator().next().getId());
//	}





}
