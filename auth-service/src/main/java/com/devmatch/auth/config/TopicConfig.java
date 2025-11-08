package com.devmatch.auth.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class TopicConfig {

  @Bean
  NewTopic userCreated() {
    return TopicBuilder.name("devmatch.user.created.v1")
        .partitions(3).replicas(1).build();
  }

  @Bean
  NewTopic dlq() {
    return TopicBuilder.name("devmatch.dlq.v1")
        .partitions(3).replicas(1).build();
  }

}
