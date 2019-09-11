package com.coelho.configuration;

import com.coelho.dtos.KafkaMessage;
import io.quarkus.kafka.client.serialization.JsonbSerializer;
import java.util.Properties;
import javax.enterprise.context.ApplicationScoped;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class KafkaConfiguration {

    private static String bootstrapServer = "kafka:9092";

    @ConfigProperty(name="kafka.bootstrap-servers")
    String bootstrapServers;

    public static Producer<String, KafkaMessage> createProducer() {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonbSerializer.class);
        return new KafkaProducer<>(props);
    }
}