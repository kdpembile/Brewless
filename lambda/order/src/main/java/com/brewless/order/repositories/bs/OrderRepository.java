package com.brewless.order.repositories.bs;

import com.brewless.order.models.bs.Order;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface OrderRepository extends ReactiveMongoRepository<Order, ObjectId> {

  Mono<Order> findByTxnRefNumber(String txnRefNumber);

}
