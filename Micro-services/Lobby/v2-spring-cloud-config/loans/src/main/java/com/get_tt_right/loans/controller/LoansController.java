package com.get_tt_right.loans.controller;

import com.get_tt_right.loans.constants.LoansConstants;
import com.get_tt_right.loans.dto.ErrorResponseDto;
import com.get_tt_right.loans.dto.LoansContactInfoDto;
import com.get_tt_right.loans.dto.LoansDto;
import com.get_tt_right.loans.dto.ResponseDto;
import com.get_tt_right.loans.service.ILoansService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author Colince Temoi
 */

/** Updates  as on 24/01/2025
 * 1. We have done changes related to the autowiring of Environment and LoansContactInfoDto beans.
 * 2. We are also trying to read the build.version from the application.properties file with the help of @Value annotation.
 * 3. We have also created a new constructor where we are injecting the ILoansService bean. You know the reason why we are doing this. Incase you forgot, refer with the AccountsController.java file.
 * - After making these initial autowiring changes, we have also created 3 different APIs to get the build information, java version and contact information.
 * - We have also created a new DTO class called LoansContactInfoDto.java where we are trying to map all the properties from the application.yaml file to the Pojo class.
 * - We have also created a new annotation called @EnableConfigurationProperties inside the LoansApplication.java file to activate the configuration properties feature and to read the configuration properties from the LoansContactInfoDto class.
 * - In the resources folder we have 2 new yaml files called application_qa.yml and application_prod.yml. These files are used to maintain the configuration properties for the QA and PROD environments for this microservice.
 * . You can check out all the 3 yml files present inside the resources folder of this microservice for more information.
 * . At first, we just copied everything as it was in Accounts microservice application.yml file to the Loans microservice application.yml file. The build.version is same, except we did some DML on the values of
 * loans.message, loans.contactDetails and loans.onCallSupport.
 * We also added the property spring.config.import to import the application_qa.yml and application_prod.yml files. We also added spring.profiles.active=qa to activate the QA profile.
 * - Inside application_prod.yml we also did maintain a property spring.config.activate.on-profile=prod to tell Spring framework to consider this whenever someone is trying to activate the prod profile.
 * - We also have other properties i.e., build.version, loans.message, loans.contactDetails and loans.onCallSupport.
 * . This same kind of setup discussed for application_prod.yml we have also done for application_qa.yml file.
 * . That was it about the changes we needed to do here inside the Loans microservice. These are very similar to the changes we did in the Accounts microservice.
 * . You can then run this loans microservice in debug mode and using postman you can test the APIs like build-info, java-version and contact-info.
 * - If you invoke build-info API, since right now the QA profile is activated, you will get the build version as 2.0.
 * - If you invoke java-version API, you will get the JAVA_HOME path set up inside my local system as a response.
 * - If you invoke contact-info API, you will get the contact details related to the QA profile as a response.
 * . Now, you can also activate a profile of for example production using externalized configuration i.e., Environment variables, command line arguments or system properties, etc.
 * . CommandLine/Program arguments -> --spring.profiles.active=prod >> If you now invoke the build-info API, you will get the build version as 1.0. If you invoke java-version API, you will get the JAVA_HOME path set up inside my local system as a response. If you invoke contact-info API, you will get the contact details related to the PROD profile as a response.
 * . Environment variables -> spring.profiles.active=prod
 * . System properties -> -Dspring.profiles.active=prod
 * With this, all the changes inside loans microservice are done. And it is the very same changes that we have also done inside the cards microservice.
 * This way, we have done all the required changes in all the 3 microservices. If you have any doubts, refer with the GitHub repository.
 * Also be careful with the yml files because even if you try to mess up with some space or indentation or even spelling you may face some surprises. So, be careful with that.
 * Like this you should be clear, but like we said this is not the best approach, it is the most basic approach that any microservice application can use. For organizations, where they are going to build 100s of microservices,
 * this approach is not recommended as it has some serious disadvantages which we will discuss next - Check slide for this. For that, we have a better approach called Spring Cloud Config Server. We will discuss that in the next section.
 * */

@Tag(
        name = "CRUD REST APIs for Loans in EazyBank",
        description = "CRUD REST APIs in EazyBank to CREATE, UPDATE, FETCH AND DELETE loan details"
)
@RestController
@RequestMapping(path = "/api", produces = {MediaType.APPLICATION_JSON_VALUE})
//@AllArgsConstructor
@Validated
public class LoansController {
// Injecting LoanService related bean to this controller.
    private ILoansService iLoansService;

    public LoansController(ILoansService iLoansService) {
        this.iLoansService = iLoansService;
    }

    @Value("${build.version}")
    private String buildVersion;

    @Autowired
    private Environment environment;

    @Autowired
    private LoansContactInfoDto loansContactInfoDto;

    @Operation(
            summary = "Create Loan REST API",
            description = "REST API to create new loan inside EazyBank"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "HTTP Status CREATED"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    }
    )
    /** Here I'm using @RequestParam to get the mobile number from the client.
     * I'm also using @Pattern to validate the mobile number.
     * Based upon the mobile number, I'm going to create a new loan.
     * Make sure you are giving the same mobile number you have used for accounts microservice because in the coming sections, using the same mobile number, we are going to fetch all the records present inside the accounts, loans and cards  microservices by using varoius concepts available inside the Spring Cloud project.
     * */
    @PostMapping("/create")
    public ResponseEntity<ResponseDto> createLoan(@RequestParam
                                                      @Pattern(regexp="(^$|[0-9]{10})",message = "Mobile number must be 10 digits")
                                                      String mobileNumber) {
        iLoansService.createLoan(mobileNumber);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDto(LoansConstants.STATUS_201, LoansConstants.MESSAGE_201));
    }

    @Operation(
            summary = "Fetch Loan Details REST API",
            description = "REST API to fetch loan details based on a mobile number"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    }
    )
    /** Here I'm using @RequestParam to get the mobile number from the client.
     * I'm also using @Pattern to validate the mobile number.
     * Based upon the mobile number, I'm going to fetch the loan details.
     * And I'm going to return the loan details to the client application.
     * */
    @GetMapping("/fetch")
    public ResponseEntity<LoansDto> fetchLoanDetails(@RequestParam
                                                               @Pattern(regexp="(^$|[0-9]{10})",message = "Mobile number must be 10 digits")
                                                               String mobileNumber) {
        LoansDto loansDto = iLoansService.fetchLoan(mobileNumber);
        return ResponseEntity.status(HttpStatus.OK).body(loansDto);
    }

    @Operation(
            summary = "Update Loan Details REST API",
            description = "REST API to update loan details based on a loan number"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "417",
                    description = "Expectation Failed"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
        }
    )
    /** Here I'm using @Valid to validate the request body.
     * I'm using @RequestBody to get the loan details from the client.
     * Based upon the loan number, I'm going to update the loan details.
     * */
    @PutMapping("/update")
    public ResponseEntity<ResponseDto> updateLoanDetails(@Valid @RequestBody LoansDto loansDto) {
        boolean isUpdated = iLoansService.updateLoan(loansDto);
        if(isUpdated) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseDto(LoansConstants.STATUS_200, LoansConstants.MESSAGE_200));
        }else{
            return ResponseEntity
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .body(new ResponseDto(LoansConstants.STATUS_417, LoansConstants.MESSAGE_417_UPDATE));
        }
    }

    /**
     * Deletes loan details based on the provided mobile number.
     *
     * This method uses @RequestParam to retrieve the mobile number from the client.
     * It validates the mobile number format using @Pattern. If the loan details are
     * successfully deleted, it returns an HTTP status of 200 OK. If the deletion fails
     * due to any reason, it returns an HTTP status of 417 Expectation Failed. In case
     * of an internal server error, a 500 Internal Server Error response is returned.
     *
     * @param mobileNumber the mobile number of the customer whose loan details are to be deleted
     * @return ResponseEntity containing the status code and message
     */
    @Operation(
            summary = "Delete Loan Details REST API",
            description = "REST API to delete Loan details based on a mobile number"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "417",
                    description = "Expectation Failed"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    }
    )
    @DeleteMapping("/delete")
    public ResponseEntity<ResponseDto> deleteLoanDetails(@RequestParam
                                                                @Pattern(regexp="(^$|[0-9]{10})",message = "Mobile number must be 10 digits")
                                                                String mobileNumber) {
        boolean isDeleted = iLoansService.deleteLoan(mobileNumber);
        if(isDeleted) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseDto(LoansConstants.STATUS_200, LoansConstants.MESSAGE_200));
        }else{
            return ResponseEntity
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .body(new ResponseDto(LoansConstants.STATUS_417, LoansConstants.MESSAGE_417_DELETE));
        }
    }

    @Operation(
            summary = "Get Build information",
            description = "Get Build information that is deployed into cards microservice"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    }
    )
    @GetMapping("/build-info") // Will give the response what build version we are trying to use right now with the help of the Java property/variable buildVersion.
    public ResponseEntity<String> getBuildInfo() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(buildVersion);
    }

    @Operation(
            summary = "Get Java version",
            description = "Get Java versions details that is installed into cards microservice"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    }
    )
    @GetMapping("/java-version") // Will help us to read the JAVA_HOME environment variable and will give that as a response to the client.
    public ResponseEntity<String> getJavaVersion() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(environment.getProperty("JAVA_HOME"));
    }

    @Operation(
            summary = "Get Contact Info",
            description = "Contact Info details that can be reached out in case of any issues"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    }
    )
    @GetMapping("/contact-info")
    public ResponseEntity<LoansContactInfoDto> getContactInfo() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(loansContactInfoDto); // Reading all the properties defined inside the application.yaml by populating into the object of LoansContactInfoDto
    }

}
