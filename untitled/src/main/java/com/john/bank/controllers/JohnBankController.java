package com.john.bank.controllers;

import com.john.bank.dto.CustomerDto;
import com.john.bank.dto.CustomerResponseDto;
import com.john.bank.dto.LoginRequestDto;
import com.john.bank.dto.LoginResponseDto;
import com.john.bank.service.JohnBankService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JohnBankController {

    private final JohnBankService johnBankService;
    private static final Logger log = LoggerFactory.getLogger(JohnBankService.class);

    @Autowired
    public JohnBankController(JohnBankService johnBankService){
        this.johnBankService = johnBankService;
    }

    @PostMapping("/customer")
    public ResponseEntity<CustomerResponseDto> registerCustomer(@RequestBody CustomerDto customerDTO) {

        log.info("Initialising customer registration");
        try {
            if (customerDTO != null) {
                johnBankService.addCustomerToRepository(customerDTO);

                CustomerResponseDto responseDto = new CustomerResponseDto();
                responseDto.setCustomerName(customerDTO.getName());
                responseDto.setAccountCreationStatus("Customer created successfully");

                return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new CustomerResponseDto(customerDTO.getName(), "Invalid password length"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new CustomerResponseDto(customerDTO.getName(), "Something went wrong."));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new CustomerResponseDto(null, "Missing request body"));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto loginDto){
        try {
            String token = johnBankService.authenticateCustomer(loginDto);
            return ResponseEntity.ok(new LoginResponseDto(token));
        }catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new LoginResponseDto("Invalid credentials"));
        }
    }
}
