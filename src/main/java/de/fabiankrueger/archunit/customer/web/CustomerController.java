package de.fabiankrueger.archunit.customer.web;

import de.fabiankrueger.archunit.customer.api.Customer;
import de.fabiankrueger.archunit.customer.api.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class CustomerController {

  private CustomerService customerService;

//  @Autowired
//  public CustomerController(CustomerService customerService) {
//    this.customerService = customerService;
//  }

  @PostMapping("/customer")
  public Customer createName(@RequestBody Customer customer) {
    return customerService.addCustomer(customer);
  }

  @Autowired
  void setCustomerService(CustomerService customerService) {
    // uncomment line to break SpringArchRules.verifySetterAnnotatedWithAutowired
    // Integer foo = 1;
    this.customerService = customerService;
  }

}
