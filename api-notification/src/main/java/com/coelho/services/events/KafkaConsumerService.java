package com.coelho.services.events;

import com.coelho.configuration.KafkaConfiguration;
import com.coelho.dtos.KafkaMessage;
import com.coelho.services.impl.NotificationsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.runtime.StartupEvent;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverOptions;

@ApplicationScoped
public class KafkaConsumerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaConsumerService.class);

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Inject
    NotificationsService notificationsService;

    private static final String TOPICS_PATTERN = ".*-api-users|.*-api-sales";

    void onStart(@Observes StartupEvent ev){
        initializeReceiver();
    }

    private void initializeReceiver() {
        ReceiverOptions<String, KafkaMessage> receiverOptions = new KafkaConfiguration().receiverOptions();
        receiverOptions.subscription(Pattern.compile(TOPICS_PATTERN));
        LOGGER.info("Initializing Kafka Receiver");

        KafkaReceiver.create(receiverOptions)
                .receive()
                .doOnError(error -> LOGGER.error("Error consuming message from kafka\n", error))
                .subscribe(notificationsService::sendEmail);
    }
}