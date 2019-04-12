package de.fabiankrueger.archunit.customer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CustomerModule {

  public static void main(String[] args) {
    SpringApplication.run(CustomerModule.class, args);
  }
}
