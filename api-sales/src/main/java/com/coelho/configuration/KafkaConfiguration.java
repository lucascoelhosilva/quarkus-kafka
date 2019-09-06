package com.coelho.configuration;

import com.coelho.dtos.KafkaMessage;
import java.util.HashMap;
import java.util.Map;
import javax.enterprise.context.ApplicationScoped;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.connect.json.JsonSerializer;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@ApplicationScoped
@Configuration
public class KafkaConfiguration {

//    @ConfigProperty(name = "kafka.bootstrap-servers", defaultValue = "localhost:9092")
    private static String bootstrapServer = "localhost:9092";

    @Bean
    public static Producer<String, KafkaMessage> createProducer() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class.getName());
        return new KafkaProducer<>(props);
    }

}