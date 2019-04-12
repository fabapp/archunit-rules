package de.fabiankrueger.archunit.customer.api;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
//@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class Customer {
  @Id
  @GeneratedValue
  private Long id;
  private String name;
  @Embedded
  private Address address;
}
