package com.devmatch.user.config;

import com.devmatch.user.dto.UserCreatedEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StringDeserializer;
import java.util.Map;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

@Configuration
public class KafkaConsumerConfig {

//  @Bean
//  public ConcurrentKafkaListenerContainerFactory<String, UserCreatedEvent> userCreatedFactory(
//      ConsumerFactory<String, UserCreatedEvent> cf) {
//    var factory = new ConcurrentKafkaListenerContainerFactory<String, UserCreatedEvent>();
//    factory.setConsumerFactory(cf);
//    factory.setCommonErrorHandler(errorHandler());
//    factory.setConcurrency(3);
//    return factory;
//  }
//
//  @Bean
//  public ConsumerFactory<String, UserCreatedEvent> consumerFactory(ObjectMapper mapper) {
//    var jd = new JsonDeserializer<>(UserCreatedEvent.class, mapper, false);
//    jd.addTrustedPackages("com.devmatch.*");
//    return new DefaultKafkaConsumerFactory<>(
//        Map.of(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092"),
//        new StringDeserializer(),
//        jd
//    );
//  }

}
