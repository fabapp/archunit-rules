package de.fabiankrueger.archunit.order.domain;

public interface OrderRepository {
  SalesOrder save(SalesOrder salesOrder);
  SalesOrder findByOrderId(String orderId);
}
