package de.fabiankrueger.archunit.order.domain;

import de.fabiankrueger.archunit.customer.api.CustomerService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SalesOrderPaymentService {

  private final OrderRepository orderRepository;
  private final PaymenInfoRepository paymentInfoRepository;

  public void processInboundOrder(SalesOrder order) {
    SalesOrder persistedSalesOrder = orderRepository.save(order);
    Optional<PaymentInfo> paymentInfo = paymentInfoRepository.findByOrderId(persistedSalesOrder.getOrderId());

  }
}
