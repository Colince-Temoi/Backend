package tech.csm.controller;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import tech.csm.domain.Location;
import tech.csm.domain.Region;
import tech.csm.service.LocationsService;
import tech.csm.service.RegionService;
import tech.csm.wrappers.Locations;

@RestController
@RequestMapping("/api")
public class LocationController {

	@Autowired
    private LocationsService locationsService;
	
	@Autowired
	private RegionService regionService;


//    @GetMapping(value ="/locations", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
//    public List<Location> getAllLocations() {
//        return locationsService.getAllLocations();
//    }
    
	@GetMapping("/locations")
	public Locations getAllLocations() {
	    List<Location> locations = locationsService.getAllLocations();
	    return new Locations(locations);
	}
    
    @GetMapping(value ="/locations/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public Location getLocationById(@PathVariable("id") Integer id) {
//        System.out.println("Get location by ID endpoint called");
        return locationsService.getLocationById(id);
    }
    
    @GetMapping(value ="/address/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public Region getRegionById(@PathVariable("id") Integer id) {
        return regionService.getRegionById(id);
    }
    
//    @PostMapping(value ="/address", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
//    public Region saveAddress(@RequestBody Region region) {
//    	
//    	System.out.println(region);
//        Integer savedRegionId = regionService.saveAddress(region);
//        return regionService.getRegionById(savedRegionId);
//    }
    
    
    /*
     * In this code, we are using ResponseEntity to have fine-grained control over the HTTP response. We are returning a 200 OK status code with the savedRegion object in the response body.

If you want to return a different status code, you can use other methods like ResponseEntity.created(), ResponseEntity.badRequest(), ResponseEntity.notFound(), etc. 

For example, if you want to return a 201 Created status code, which is often used when a new resource is created, you can do:

return ResponseEntity.created(URI.create("/address/" + savedRegionId)).body(savedRegion);
This will return a 201 Created status code with the URI of the newly created resource in the Location header and the savedRegion object in the response body.

Remember that you need to handle the validation errors in your controller method. You can do this by adding a @ExceptionHandler method or by using a global exception handler

In this example, we are using the @Valid annotation to trigger the validations and the BindingResult object to get the validation errors. If there are any validation errors, we throw a MethodArgumentNotValidException. You can then handle this exception in a global exception handler or in an @ExceptionHandler method.

You are correct! The code included in the 'if' block is crucial for triggering the validation process. The Spring framework's data binding process automatically performs validation on the '@RequestBody' object, which is the 'region' parameter in this case.

The 'BindingResult' object holds the outcome of this validation, containing error details if any validation constraints are violated. When you call 'bindingResult.hasErrors()', it checks whether there are any validation errors present in the 'BindingResult'. 

Here's why the manual throw is essential:

If there are validation errors, the code in the 'if' block manually throws a 'MethodArgumentNotValidException'. This exception is then caught by the '@ExceptionHandler' method in the 'GlobalExceptionHandler' class, which you had added later.

The '@ExceptionHandler' method then handles the exception and constructs the appropriate error response (400 status code along with a list of error messages).

If you omit this manual throw, the validation errors will be silently ignored, and the code will proceed as if there were no validation issues. The '@ExceptionHandler' method would never be triggered, and you would receive a successful response (likely a 201 Created status code) without any proper error indication.

In summary, the manual throw acts as a bridge between the automatic validation performed by Spring and the custom error handling provided by the '@ExceptionHandler' method. It is essential for triggering the exception and subsequent error response generation when validation fails.
     * */
    @PostMapping(value ="/address", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Region> saveAddress(@RequestBody @Valid Region region, BindingResult bindingResult) throws MethodArgumentNotValidException {
    	
    
    	if (bindingResult.hasErrors()) {
//    		 System.out.println(region);
            throw new MethodArgumentNotValidException(null, bindingResult);
        }
            
//            System.out.println(region);
            Integer savedRegionId = regionService.saveAddress(region);
            Region savedRegion = regionService.getRegionById(savedRegionId);
            
//            return ResponseEntity.ok(savedRegion); // 200 OK status
            return ResponseEntity.created(URI.create("/address/" + savedRegionId)).body(savedRegion);
        }
    
    @PutMapping(value ="/address", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity updateAddress(@RequestBody @Valid Region region, BindingResult bindingResult) throws MethodArgumentNotValidException {
        
        if (bindingResult.hasErrors()) {
            System.out.println(region);
            throw new MethodArgumentNotValidException(null, bindingResult);
        }

        System.out.println("Updating Region: " + region); // Debugging statement

        Integer rowsAffected = regionService.updateAddress(region);

        if(rowsAffected > 0) {
            Region updatedRegion = regionService.getRegionById(region.getRegionId());
            return ResponseEntity.ok(updatedRegion);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping(value ="/address/{id}")
    public ResponseEntity deleteAddress(@PathVariable("id") Integer id) {
        
//        System.out.println("Deleting Region with Id: " + id); // Debugging statement

        Integer rowsAffected = regionService.deleteAddress(id);

        if(rowsAffected > 0) {
            return ResponseEntity.ok("Region with Id: " + id + " deleted successfully.");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
}