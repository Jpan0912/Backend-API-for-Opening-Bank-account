package com.john.bank.service;

import com.john.bank.dto.AccountDto;
import com.john.bank.dto.CustomerDto;
import com.john.bank.dto.LoginRequestDto;
import com.john.bank.models.Account;
import com.john.bank.models.Customer;
import com.john.bank.util.JwtUtil;
import com.john.bank.repository.JohnBankRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class JohnBankService {

    private static final Logger log = LoggerFactory.getLogger(JohnBankService.class);
    private final JohnBankRepository johnBankRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtUtil jwtUtil;

    @Autowired
    public JohnBankService(JohnBankRepository johnBankRepository, BCryptPasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.johnBankRepository = johnBankRepository;
        this.bCryptPasswordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public void addAccountToRepository(AccountDto accountDto, String apiToken){

        String accountName = jwtUtil.getUsernameFromToken(apiToken);
        String accountNumber = accountDto.getAccountNumber();
        log.info("Adding account {} to user: {}",accountNumber, accountName);

        Account account = new Account();
        account.setAccountName(accountName);
        account.setAccountNumber(accountNumber);

        johnBankRepository.addAccount(account);
    }


    public void addCustomerToRepository(CustomerDto customerDto){
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

    public String authenticateCustomer(LoginRequestDto loginDto){
        Customer customer = johnBankRepository.getCustomerByName(loginDto.getName());

        if(customer == null || !bCryptPasswordEncoder.matches(loginDto.getPassword(), customer.getPassword())){
            throw new IllegalArgumentException("Invalid credentials");
        }
        return jwtUtil.generateToken(customer.getName());
    }
}
