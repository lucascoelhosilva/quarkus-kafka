package com.coelho.configuration;

import com.coelho.dtos.KafkaMessage;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import javax.enterprise.context.ApplicationScoped;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.connect.json.JsonDeserializer;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import reactor.kafka.receiver.ReceiverOptions;

@ApplicationScoped
public class KafkaConfiguration {

    @ConfigProperty(name="kafka.bootstrap.servers", defaultValue = "localhost:9092")
    private String bootstrapServers = "kafka:9092";

    @ConfigProperty(name = "kafka.clientId")
    private String clientId = "api-notification";

    @ConfigProperty(name = "kafka.groupId")
    private String groupId = "api-notification";

    private static final String TOPICS_PATTERN = ".*-api-users|.*-api-sales";

    public ReceiverOptions<String, KafkaMessage> receiverOptions() {

        Map<String, Object> configuration = new HashMap<>();
        configuration.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configuration.put(ConsumerConfig.CLIENT_ID_CONFIG, clientId);
        configuration.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        configuration.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configuration.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        configuration.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
        configuration.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, 1000);
        configuration.put(ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG, 1000);
        configuration.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        configuration.put(ConsumerConfig.METADATA_MAX_AGE_CONFIG, 1000);

        ReceiverOptions<String, KafkaMessage> receiverOptions = ReceiverOptions.create(configuration);
        receiverOptions.subscription(Pattern.compile(TOPICS_PATTERN));

        return receiverOptions;
    }
}