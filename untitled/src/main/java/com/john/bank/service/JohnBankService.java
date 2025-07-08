package com.john.bank.service;

import com.john.bank.dto.CustomerDTO;
import com.john.bank.models.Customer;
import com.john.bank.repository.JohnBankRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class JohnBankService {

    private static final Logger log = LoggerFactory.getLogger(JohnBankService.class);
    private final JohnBankRepository johnBankRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public JohnBankService(JohnBankRepository johnBankRepository, BCryptPasswordEncoder passwordEncoder) {
        this.johnBankRepository = johnBankRepository;
        this.bCryptPasswordEncoder = passwordEncoder;
    }

    public void addCustomerToRepository(CustomerDTO customerDto){
        String customerName = customerDto.getName();
        String customerPassword = customerDto.getPassword();
        try{
            if(customerPassword.length() <= 5 || customerPassword.length() >= 30) {
                throw new IllegalArgumentException("Password must be between 5 and 30 characters.");
            }
            String hashedPassword = bCryptPasswordEncoder.encode(customerDto.getPassword());

            Customer customer = new Customer();
            customer.setName(customerName);
            customer.setPassword(hashedPassword);

            johnBankRepository.addCustomer(customer);
            log.info("Registering customer: {}", customerName);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
