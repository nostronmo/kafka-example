package com.tom.kafka.consumer;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;

@Lazy(false)
@Configuration
@KafkaStatusInterface
public class KafkaConsumerConfig {

	@Value("${spring.kafka.bootstrap-servers}")
	private String bootstrapServers;

	@Bean
	ConsumerFactory<String, BookData> consumerFactory() {
		Map<String, Object> props = new HashMap<>();
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
		props.put(ConsumerConfig.GROUP_ID_CONFIG, "book-monitor-group");

		StringDeserializer stringDeserializer = new StringDeserializer();
		ErrorHandlingDeserializer<String> keyDeserializer = new ErrorHandlingDeserializer<>(stringDeserializer);

		JsonDeserializer<BookData> jsonDeserializer = new JsonDeserializer<>(BookData.class);
		jsonDeserializer.addTrustedPackages("*");
		jsonDeserializer.setRemoveTypeHeaders(false);
		jsonDeserializer.setUseTypeMapperForKey(false);

		ErrorHandlingDeserializer<BookData> valueDeserializer = new ErrorHandlingDeserializer<>(jsonDeserializer);

		return new DefaultKafkaConsumerFactory<>(props, keyDeserializer, valueDeserializer);

	}

	@Bean
	ConcurrentKafkaListenerContainerFactory<String, BookData> kafkaListenerContainerFactory() {
		ConcurrentKafkaListenerContainerFactory<String, BookData> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(consumerFactory());
		factory.setConcurrency(2);
		factory.getContainerProperties().setStopImmediate(true);
		return factory;
	}

}

/*
 * 
 * JacksonJsonDeserializer<BookDTO> jsonDeserializer = new
 * JacksonJsonDeserializer<>(BookDTO.class);
 * 
 * jsonDeserializer.addTrustedPackages("*");
 * jsonDeserializer.setRemoveTypeHeaders(false);
 * jsonDeserializer.setUseTypeMapperForKey(false);
 * 
 * ErrorHandlingDeserializer<BookDTO> valueDeserializer = new
 * ErrorHandlingDeserializer<>(jsonDeserializer);
 * 
 */