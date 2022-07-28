package com.example.bikestorestests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;


@ExtendWith(SpringExtension.class)
class BikestoresRestRequestsTests {

	private static final String BASEPATH = "http://localhost:8080/bikestores/test/";
	private static RestTemplate restTemplate;
	private String id;

	@BeforeAll 
	public static void setUp(){
		restTemplate = new RestTemplate();		
	}

	@Test
	void readCustomersAutomationTest() throws JsonMappingException, JsonProcessingException {

		ResponseEntity<String> response =  restTemplate.getForEntity(
				BASEPATH + "customers", String.class);

		assertEquals(HttpStatus.OK, response.getStatusCode());

		if (HttpStatus.OK.equals(response.getStatusCode())) {
			@SuppressWarnings("unchecked")
			List<HashMap<String,Object>> result = new ObjectMapper().readValue(response.getBody().replaceAll("=",":"), List.class);

			assertNotNull(result.get(0));
			assertTrue(result.size()>0);
		}
	}

	@Test
	void createAndDeleteCustomerAutomationTest() throws JsonMappingException, JsonProcessingException, JSONException {
		//CREATE
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		JSONObject personJsonObject = new JSONObject();
		personJsonObject.put("city", "Napoli");
		personJsonObject.put("email", "prova@yahoo.com");
		personJsonObject.put("firstName", "Luca");
		personJsonObject.put("lastName", "Nocella");
		personJsonObject.put("phone", null);
		personJsonObject.put("state", "CA");
		personJsonObject.put("street", "910 Vine Street");
		personJsonObject.put("zipCode", "95008");
		personJsonObject.put("orders", null);

		HttpEntity<String> request = new HttpEntity<String>(personJsonObject.toString(), headers);

		ResponseEntity<String> responseCreate = restTemplate.postForEntity(BASEPATH + "customer", request, String.class);

		assertEquals(HttpStatus.OK, responseCreate.getStatusCode());

		if (HttpStatus.OK.equals(responseCreate.getStatusCode())) {
			@SuppressWarnings("unchecked")
			List<HashMap<String,Object>> result = new ObjectMapper().readValue(responseCreate.getBody().replaceAll("=",":"), List.class);

			assertNotNull(result.get(0));
			assertTrue(result.size()>0);
			id = result.get(0).get("customerId").toString();

		}
		//DELETE
		ResponseEntity<String> responseDelete =  restTemplate.getForEntity(
				BASEPATH + "customer/" + id, String.class);
		assertEquals(HttpStatus.OK, responseDelete.getStatusCode());
	}

}
