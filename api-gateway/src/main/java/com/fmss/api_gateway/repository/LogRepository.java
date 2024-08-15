package com.fmss.api_gateway.repository;

import com.fmss.api_gateway.model.Log;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface LogRepository extends ReactiveMongoRepository<Log, String> {
}
