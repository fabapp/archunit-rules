package de.fabiankrueger.archunit.order.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class OrderCreatedEvent {

  private final SalesOrder salesOrder;

}
