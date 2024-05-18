package tech.csm.restcontroller;

import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import tech.csm.domain.Location;
import tech.csm.domain.Region;

@RestController
@RequestMapping("/client")
public class MyRestController {
	
	@Autowired
	private RestTemplate restTemplate;

//	@GetMapping()
//	public ResponseEntity<String> getAllLocations() throws URISyntaxException {
//		System.out.println("Get is working!..."+restTemplate);
//		
////		http://localhost:8181/restDemo/api/locations
//		URI uri = new URI("http://localhost:8181/restDemo/api/locations");
//		ResponseEntity<String> resp = restTemplate.getForEntity(uri, String.class);
//		
//		return resp;
//	    
//	}
	
//	@GetMapping()
//	public ResponseEntity<Location[]> getAllLocations() throws URISyntaxException {
//		System.out.println("Get is working!..."+restTemplate);
//		
////		http://localhost:8181/restDemo/api/locations
//		URI uri = new URI("http://localhost:8181/restDemo/api/locations");
//		ResponseEntity<Location[]> resp = restTemplate.getForEntity(uri, Location[].class);
//		
//		return resp;
//	    
//	}
	
//	@GetMapping()
//	public ResponseEntity<Location> getAllLocations() throws URISyntaxException {
//		System.out.println("Get is working!..."+restTemplate);
//		
////		http://localhost:8181/restDemo/api/locations/1700
//		URI uri = new URI("http://localhost:8181/restDemo/api/locations/1700");
//		ResponseEntity<Location> resp = restTemplate.getForEntity(uri, Location.class);
//		
//		return resp;
//	    
//	}
	
//	Making it dynamic such that client can send dynamic input. Lets utilize Request params. You could also utilize path variables. This you know!
//	@GetMapping()
//	public ResponseEntity<Location> getAllLocations(@RequestParam Integer lid) throws URISyntaxException {
//		System.out.println("Get is working!..."+restTemplate);
//		
////		http://localhost:8181/restDemo/api/locations/1700
//		URI uri = new URI("http://localhost:8181/restDemo/api/locations/"+lid);
//		ResponseEntity<Location> resp = restTemplate.getForEntity(uri, Location.class);
//		
//		return resp;
//	    
//	}
	
//	Utilizing path variables
	
	@GetMapping(path="/{lid}")
	public ResponseEntity<Location> getAllLocations(@PathVariable Integer lid) throws URISyntaxException {
		System.out.println("Get is working!..."+restTemplate);
		
//		http://localhost:8181/restDemo/api/locations/1700
		URI uri = new URI("http://localhost:8181/restDemo/api/locations/"+lid);
		ResponseEntity<Location> resp = restTemplate.getForEntity(uri, Location.class);
		
		return resp;	    
	}
	
	@PostMapping
	public ResponseEntity<Region> saveAddress(@RequestBody Region re) throws URISyntaxException {
		
//		Setting up header information
		HttpHeaders header = new HttpHeaders();
		header.setContentType(MediaType.APPLICATION_JSON);
		
//		Creating HttpEntity object
		HttpEntity<Region> he = new HttpEntity<Region>(re, header);
		
//		http://localhost:8181/restDemo/api/address
		URI uri = new URI("http://localhost:8181/restDemo/api/address");
		
		ResponseEntity<Region> res = restTemplate.postForEntity(uri, he, Region.class);
		return res;		
	}
}
