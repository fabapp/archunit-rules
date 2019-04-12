package de.fabiankrueger.archunit.order.domain;

import java.util.Optional;

public interface PaymenInfoRepository {
  Optional<PaymentInfo> findByOrderId(String orderId);
}
