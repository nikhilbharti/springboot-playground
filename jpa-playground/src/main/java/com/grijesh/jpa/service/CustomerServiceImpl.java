package com.grijesh.jpa.service;

import com.google.common.collect.Lists;
import com.grijesh.jpa.domain.Customer;
import com.grijesh.jpa.domain.CustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.metrics.CounterService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by grijesh on 19/11/15.
 */
@Service
public class CustomerServiceImpl implements CustomerService {

    private CounterService counterService;

    private static final Logger log = LoggerFactory.getLogger(CustomerServiceImpl.class);

    private CustomerRepository repository;

    @Autowired
    public CustomerServiceImpl(CustomerRepository repository,CounterService counterService) {
        this.repository = repository;
        this.counterService = counterService;
    }

    @Override
    public void runExample() {
        repository.save(new Customer("Jack", "Bauer"));
        repository.save(new Customer("Chloe", "O'Brian"));
        repository.save(new Customer("Kim", "Bauer"));
        repository.save(new Customer("David", "Palmer"));
        repository.save(new Customer("Michelle", "Dessler"));

        // fetch all customers
        log.info("Customers found with findAll():");
        log.info("-------------------------------");
        for (Customer customer : repository.findAll()) {
            log.info(customer.toString());
        }
        log.info("");

        // fetch an individual customer by ID
        Customer customer = repository.findOne(1L);
        log.info("Customer found with findOne(1L):");
        log.info("--------------------------------");
        log.info(customer.toString());
        log.info("");

        // fetch customers by last name
        log.info("Customer found with findByLastName('Bauer'):");
        log.info("--------------------------------------------");
        for (Customer bauer : repository.findByLastName("Bauer")) {
            log.info(bauer.toString());
        }
        log.info("");
    }

    @Override
    public List<Customer> listAllCustomers() {
        List<Customer> list = Lists.newArrayList(repository.findAll());
        list.forEach(item -> log.info(item.toString()));
        counterService.increment("counter.list_all_customer_operation");
        return list;
    }

    @Override
    @Transactional
    public void save(Customer customer) {
        repository.save(customer);
    }
}
