package mipt.app.secondmemory.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import lombok.extern.slf4j.Slf4j;
import mipt.app.secondmemory.dto.message.MessageDto;
import org.hibernate.sql.exec.ExecutionException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaProducerService {
  private final KafkaTemplate<String, String> kafkaTemplate;
  private final ObjectMapper objectMapper;
  private final String topic;
  private final MeterRegistry registry;

  private Counter getMessagesCounter(String type) {
    return Counter.builder("kafka.requests")
        .description("Number of messages sent")
        .tags("type", type)
        .register(registry);
  }

  public KafkaProducerService(
      KafkaTemplate<String, String> kafkaTemplate,
      ObjectMapper objectMapper,
      @Value("${topic-to-send-message}") String topic,
      MeterRegistry registry) {
    this.kafkaTemplate = kafkaTemplate;
    this.objectMapper = objectMapper;
    this.topic = topic;
    this.registry = registry;
  }

  public void sendMessage(MessageDto messageDto) throws JsonProcessingException {
    String message = objectMapper.writeValueAsString(messageDto);

    try {
      CompletableFuture<SendResult<String, String>> sendResult = kafkaTemplate.send(topic, message);
      getMessagesCounter("sentMessage").increment();
    } catch (ExecutionException | CompletionException ex) {
      log.error("Error occurred while sending message with kafka", ex);
    }
  }
}
