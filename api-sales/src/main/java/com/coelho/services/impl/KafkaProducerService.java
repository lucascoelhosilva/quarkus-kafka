package com.coelho.services.impl;

import com.coelho.configuration.KafkaConfiguration;
import com.coelho.dtos.KafkaMessage;
import com.coelho.models.Sale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.ApplicationScoped;
import org.apache.kafka.clients.producer.ProducerRecord;

@ApplicationScoped
public class KafkaProducerService {

    private static final Logger LOGGER = Logger.getLogger(KafkaProducerService.class.getName());

    private static final String KAFKA_TOPIC = "%s-api-sales";

    public void process(Sale sale) {

        KafkaConfiguration.createProducer().send(
                new ProducerRecord<>(buildTopicName(sale.getCustomerId().toString()), buildMessage(sale))
        );
    }

    private String buildTopicName(final String customerId) {
        return String.format(KAFKA_TOPIC, customerId);
    }

    private KafkaMessage buildMessage(Sale sale) {
        LOGGER.log(Level.INFO, "Sending to KAFKA " + sale.toString());

        return KafkaMessage.builder().payload(sale.toString()).build();
    }
}