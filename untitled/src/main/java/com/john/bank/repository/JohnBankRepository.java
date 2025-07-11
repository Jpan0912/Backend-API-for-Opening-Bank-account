package com.john.bank.repository;

import com.john.bank.models.Customer;

public interface JohnBankRepository {
    public void addCustomer(Customer customer);

    public Customer getCustomerByName(String name);
}
