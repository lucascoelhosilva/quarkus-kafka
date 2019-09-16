package com.coelho.services.events;

import com.coelho.configuration.KafkaConfiguration;
import com.coelho.dtos.KafkaMessage;
import com.coelho.services.impl.NotificationsService;
import io.quarkus.runtime.StartupEvent;
import java.util.regex.Pattern;
import javax.annotation.PostConstruct;
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

    @Inject
    NotificationsService notificationsService;

    void onStart(@Observes StartupEvent ev){
        initializeReceiver();
    }

    private void initializeReceiver() {
        ReceiverOptions<String, KafkaMessage> receiverOptions = new KafkaConfiguration().receiverOptions();
        LOGGER.info("Initializing Kafka Receiver");

        KafkaReceiver.create(receiverOptions)
                .receive()
                .doOnError(error -> LOGGER.error("Error consuming message from kafka\n", error))
                .subscribe(notificationsService::sendEmail);
    }
}