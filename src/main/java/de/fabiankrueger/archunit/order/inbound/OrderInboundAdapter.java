package de.fabiankrueger.archunit.order.inbound;

import de.fabiankrueger.archunit.order.domain.OrderCreatedEvent;
import de.fabiankrueger.archunit.order.domain.SalesOrderPaymentService;
import de.fabiankrueger.archunit.order.domain.SalesOrder;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;

@RequiredArgsConstructor
public class OrderInboundAdapter {

  private final SalesOrderPaymentService salesOrderPaymentService;

  @EventListener
  public void onInboundOrder(OrderCreatedEvent orderCreatedEvent) {
    SalesOrder order = orderCreatedEvent.getSalesOrder();
    salesOrderPaymentService.processInboundOrder(order);
  }

}
