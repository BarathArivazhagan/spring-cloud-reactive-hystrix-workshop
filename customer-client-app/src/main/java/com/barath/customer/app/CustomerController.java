package com.barath.customer.app;


import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.lang.invoke.MethodHandles;
import java.util.Arrays;

@RestController
@RequestMapping(value = "/customer",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class CustomerController {

    private static final Logger logger= LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private CustomerService customerService;

    public CustomerController(CustomerService customerService){
        this.customerService=customerService;
    }


    @PostMapping(value = "/create")
    public Mono<Customer> createCustomer(@RequestBody Customer customer){

        return customerService.saveCustomer(customer);

    }

    @GetMapping(value = "/getCustomers")
    @HystrixCommand(commandKey = "customers",fallbackMethod = "getDefaultCustomers")
    public Flux<Customer> getCustomers(){
        return customerService.getCustomers();
    }

    public Flux<Customer> getDefaultCustomers(){

        return Flux.fromIterable(Arrays.asList(new Customer(1L,"Test"))).log();

    }
}
