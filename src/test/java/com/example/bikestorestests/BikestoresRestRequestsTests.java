package com.example.bikestorestests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;


@ExtendWith(SpringExtension.class)
class BikestoresRestRequestsTests {

	private static String id;
	private static final String BASEPATH = "http://localhost:8080/bikestores/test/";
	private static final String GETCUSTOMERSPATH = "customers";
	private static final String GETCUSTOMERBYIDPATH = "customer/";
	private static final String CREATECUSTOMERPATH = "customer";
	private static final String DELETECUSTOMERBYIDPATH = "customer/";
	private static final String PUTBYIDPATH = "customer/";
	private static final String PATCHBYIDPATH = "customerName/";
	private static RestTemplate restTemplate;

	@BeforeAll 
	public static void setUp(){
		restTemplate = new RestTemplate();
		restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
	}

	@Test
	void readCustomersAutomationTest() throws JsonMappingException, JsonProcessingException {

		ResponseEntity<String> response =  restTemplate.exchange(
				BASEPATH + GETCUSTOMERSPATH, HttpMethod.GET, null, String.class);


		assertEquals(HttpStatus.OK, response.getStatusCode());

		if (HttpStatus.OK.equals(response.getStatusCode())) {
			@SuppressWarnings("unchecked")
			List<HashMap<String,Object>> result = new ObjectMapper().readValue(response.getBody().replaceAll("=",":"), List.class);

			assertTrue(result.size()>0);
			assertNotNull(result.get(0));
		}
	}

	@Test
	void readCustomerByIdAutomationTest() throws JsonMappingException, JsonProcessingException, JSONException {

		createCustomer();
		ResponseEntity<String> response = restTemplate.exchange(
				BASEPATH + GETCUSTOMERBYIDPATH + id, HttpMethod.GET, null, String.class);

		assertEquals(HttpStatus.OK, response.getStatusCode());

		if (HttpStatus.OK.equals(response.getStatusCode())) {
			@SuppressWarnings("unchecked")
			List<HashMap<String,Object>> result = new ObjectMapper().readValue(response.getBody().replaceAll("=",":"), List.class);

			assertTrue(result.size()>0);
			assertNotNull(result.get(0));
			assertEquals(id, result.get(0).get("customerId").toString());
		}

		deleteCustomer();
	}

	@Test
	void createCustomerAutomationTest() throws JsonMappingException, JsonProcessingException, JSONException {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		JSONObject personJsonObject = new JSONObject();
		personJsonObject.put("city", "Napoli");
		personJsonObject.put("email", "prova@yahoo.com");
		personJsonObject.put("firstName", "Luca");
		personJsonObject.put("lastName", "Nocella");
		personJsonObject.put("phone", "3334446667");
		personJsonObject.put("state", "CA");
		personJsonObject.put("street", "910 Vine Street");
		personJsonObject.put("zipCode", "95008");
		personJsonObject.put("orders", null);

		HttpEntity<String> request = new HttpEntity<String>(personJsonObject.toString(), headers);

		ResponseEntity<String> responseCreate = restTemplate.exchange(
				BASEPATH + CREATECUSTOMERPATH, HttpMethod.POST, request, String.class);

		assertEquals(HttpStatus.OK, responseCreate.getStatusCode());

		if (HttpStatus.OK.equals(responseCreate.getStatusCode())) {
			@SuppressWarnings("unchecked")
			List<HashMap<String,Object>> result = new ObjectMapper().readValue(responseCreate.getBody().replaceAll("=",":"), List.class);

			assertNotNull(result.get(0));
			assertTrue(result.size()>0);
			assertEquals(result.get(0).get("city"), personJsonObject.get("city"));
			assertEquals(result.get(0).get("email"), personJsonObject.get("email"));
			assertEquals(result.get(0).get("firstName"), personJsonObject.get("firstName"));
			assertEquals(result.get(0).get("lastName"), personJsonObject.get("lastName"));
			assertEquals(result.get(0).get("phone"), personJsonObject.get("phone"));
			assertEquals(result.get(0).get("state"), personJsonObject.get("state"));
			assertEquals(result.get(0).get("street"), personJsonObject.get("street"));
			assertEquals(result.get(0).get("zipCode"), personJsonObject.get("zipCode"));

			id = result.get(0).get("customerId").toString();
			deleteCustomer();
		}
	}

	@Test
	void deleteCustomerAutomationTest() throws JsonMappingException, JsonProcessingException, JSONException {
		createCustomer();
		ResponseEntity<String> response = restTemplate.exchange(BASEPATH + DELETECUSTOMERBYIDPATH + id, HttpMethod.DELETE, null, String.class);
		assertEquals(HttpStatus.OK, response.getStatusCode());

		ResponseEntity<String> responseRead;
		try {
			responseRead = restTemplate.exchange(BASEPATH + GETCUSTOMERBYIDPATH + id, HttpMethod.GET, null, String.class);

		} catch (HttpStatusCodeException ex) {
			responseRead = new ResponseEntity<String>(ex.getResponseBodyAsString(), ex.getResponseHeaders(), ex.getStatusCode());
		}

		assertEquals(HttpStatus.NOT_FOUND, responseRead.getStatusCode());
	}

	@Test
	void updateCustomerAutomationTest() throws JsonMappingException, JsonProcessingException, JSONException, URISyntaxException {
		createCustomer();

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		JSONObject personJsonObject = new JSONObject();
		personJsonObject.put("customerId", id);
		personJsonObject.put("city", "NapoliUpdated");
		personJsonObject.put("email", "prova@yahooUpdated.com");
		personJsonObject.put("firstName", "LucaUpdated");
		personJsonObject.put("lastName", "NocellaUpdated");
		personJsonObject.put("phone", "3334446668");
		personJsonObject.put("state", "CA");
		personJsonObject.put("street", "910 Vine Street");
		personJsonObject.put("zipCode", "95008");
		personJsonObject.put("orders", null);

		HttpEntity<String> request = new HttpEntity<String>(personJsonObject.toString(), headers);

		ResponseEntity<String> responseUpdate = restTemplate.exchange(BASEPATH + PUTBYIDPATH + id, HttpMethod.PUT, request, String.class);

		assertEquals(HttpStatus.OK, responseUpdate.getStatusCode());

		if (HttpStatus.OK.equals(responseUpdate.getStatusCode())) {
			@SuppressWarnings("unchecked")
			List<HashMap<String,Object>> result = new ObjectMapper().readValue(responseUpdate.getBody().replaceAll("=",":"), List.class);

			assertNotNull(result.get(0));
			assertTrue(result.size()>0);
			assertEquals(result.get(0).get("city"), personJsonObject.get("city"));
			assertEquals(result.get(0).get("email"), personJsonObject.get("email"));
			assertEquals(result.get(0).get("firstName"), personJsonObject.get("firstName"));
			assertEquals(result.get(0).get("lastName"), personJsonObject.get("lastName"));
			assertEquals(result.get(0).get("phone"), personJsonObject.get("phone"));
			assertEquals(result.get(0).get("state"), personJsonObject.get("state"));
			assertEquals(result.get(0).get("street"), personJsonObject.get("street"));
			assertEquals(result.get(0).get("zipCode"), personJsonObject.get("zipCode"));
			assertEquals(result.get(0).get("customerId").toString(), personJsonObject.get("customerId"));
		}

		deleteCustomer();
	}

	@Test
	void updateNameCustomerAutomationTest() throws JsonMappingException, JsonProcessingException, JSONException, URISyntaxException {
		createCustomer();

		String firstName = "LucaUpdated";
		String lastName = "NocellaUpdated";
		HttpHeaders headers = new HttpHeaders();
		headers.add("cognome", lastName);

		HttpEntity<String> request = new HttpEntity<String>(null, headers);
		ResponseEntity<String> responseUpdate = restTemplate.exchange(BASEPATH + PATCHBYIDPATH + id + "?name=" + firstName, HttpMethod.PATCH, request, String.class);

		assertEquals(HttpStatus.OK, responseUpdate.getStatusCode());

		if (HttpStatus.OK.equals(responseUpdate.getStatusCode())) {
			@SuppressWarnings("unchecked")
			List<HashMap<String,Object>> result = new ObjectMapper().readValue(responseUpdate.getBody().replaceAll("=",":"), List.class);

			assertNotNull(result.get(0));
			assertTrue(result.size()>0);
			assertEquals(result.get(0).get("customerId").toString(), id);
			assertEquals(result.get(0).get("firstName"), firstName);
			assertEquals(result.get(0).get("lastName"), lastName);
		}

		deleteCustomer();
	}



	private static void createCustomer() throws JSONException, JsonMappingException, JsonProcessingException{
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		JSONObject personJsonObject = new JSONObject();
		personJsonObject.put("city", "Napoli");
		personJsonObject.put("email", "prova@yahoo.com");
		personJsonObject.put("firstName", "Luca");
		personJsonObject.put("lastName", "Nocella");
		personJsonObject.put("phone", "3334446667");
		personJsonObject.put("state", "CA");
		personJsonObject.put("street", "910 Vine Street");
		personJsonObject.put("zipCode", "95008");
		personJsonObject.put("orders", null);

		HttpEntity<String> request = new HttpEntity<String>(personJsonObject.toString(), headers);
		ResponseEntity<String> responseCreate = restTemplate.exchange(BASEPATH + CREATECUSTOMERPATH, HttpMethod.POST, request, String.class);
		assertEquals(HttpStatus.OK, responseCreate.getStatusCode());

		@SuppressWarnings("unchecked")
		List<HashMap<String,Object>> result = new ObjectMapper().readValue(responseCreate.getBody().replaceAll("=",":"), List.class);
		assertNotNull(result.get(0));
		assertTrue(result.size()>0);
		id = result.get(0).get("customerId").toString();
	}

	public void deleteCustomer() throws JsonMappingException, JsonProcessingException, JSONException {
		restTemplate.exchange(BASEPATH + DELETECUSTOMERBYIDPATH + id, HttpMethod.DELETE, null, String.class);

		ResponseEntity<String> response;
		try {
			response = restTemplate.exchange(
					BASEPATH + DELETECUSTOMERBYIDPATH + id, HttpMethod.GET, null, String.class);
		} catch (HttpStatusCodeException ex) {
			response = new ResponseEntity<String>(ex.getResponseBodyAsString(), ex.getResponseHeaders(), ex.getStatusCode());
		}

		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
	}
}

