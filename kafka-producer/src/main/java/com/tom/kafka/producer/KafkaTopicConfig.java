package com.tom.kafka.producer;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.config.TopicConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.util.unit.DataSize;

@Lazy(false)
@Configuration
@KafkaStatusInterface
public class KafkaTopicConfig {

	@Value("${kafka.config.global.size}")
	private DataSize globalTopicSize;

	@Value("${kafka.config.global.retention-time}")
	private Duration globalTopicRetentionTime;
	
    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;
	
    @Value("${kafka.topics.stream.book}")
    private String topicName;
    
    @Bean
    Map<String, Object> producerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        props.put(JsonSerializer.ADD_TYPE_INFO_HEADERS, false);
        return props;
    }
	
    @Bean
    ProducerFactory<String, BookData> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    @Bean
    KafkaTemplate<String, BookData> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
    
    @Bean
    NewTopic bookTopic() {
        return TopicBuilder.name(topicName)
                .partitions(4)
                .replicas(2)
                .config(TopicConfig.RETENTION_MS_CONFIG, String.valueOf(globalTopicRetentionTime))
                .config(TopicConfig.RETENTION_BYTES_CONFIG, String.valueOf(globalTopicSize))
                .build();
    }
	
}
