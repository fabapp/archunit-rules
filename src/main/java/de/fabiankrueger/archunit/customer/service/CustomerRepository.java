package de.fabiankrueger.archunit.customer.service;

import de.fabiankrueger.archunit.customer.api.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

interface CustomerRepository extends JpaRepository<Customer, Long> {

}
