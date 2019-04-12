package de.fabiankrueger.archunit.customer.service;

import de.fabiankrueger.archunit.customer.api.CustomerService;
import de.fabiankrueger.archunit.customer.api.Customer;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
class CustomerServiceImpl implements CustomerService {

  private CustomerRepository customerRepository;


  @Override
  public Customer addCustomer(Customer customer) {
    return customerRepository.save(customer);
  }
}
