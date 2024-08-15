package com.fmss.api_gateway.config.logger;

import com.fmss.api_gateway.model.Log;
import com.fmss.api_gateway.repository.LogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Component
@RequiredArgsConstructor
@Slf4j
public class MongoDBConsumer {
    private final LogRepository logRepository;
    @RabbitListener(queues = "${rabbitmq.queue.logs-queue}", containerFactory = "rabbitListenerContainerFactory")
    private void mongoConsumer(Log message) {

        Mono.just(message)
                .flatMap(logRepository::save)
                .subscribeOn(Schedulers.boundedElastic())
                .subscribe(
                        savedLog -> log.info("Log saved successfully with ID: {}", savedLog.getThread()),
                        error -> log.error("Error saving log: ", error)
                );
    }
}
