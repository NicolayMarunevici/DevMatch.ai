package com.devmatch.auth.config;

import com.fasterxml.jackson.databind.ser.std.StringSerializer;
import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

@Configuration
public class KafkaConfig {

//  @Bean
//  public ProducerFactory<String, String> stringProducerFactory(
//      KafkaProperties props) {
//    Map<String, Object> cfg = new HashMap<>(props.buildProducerProperties());
//    cfg.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
//    cfg.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
//    return new DefaultKafkaProducerFactory<>(cfg);
//  }
//
//  @Bean
//  public KafkaTemplate<String, String> stringKafkaTemplate(
//      ProducerFactory<String, String> pf) {
//    return new KafkaTemplate<>(pf);
//  }
}
