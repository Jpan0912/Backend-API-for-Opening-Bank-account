package com.john.bank.repository;

import com.john.bank.models.Customer;
import com.john.bank.models.Account;


public interface JohnBankRepository {
    public void addCustomer(Customer customer);

    public Customer getCustomerByName(String name);

    public void addAccount(Account account);
}
