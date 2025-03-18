package mipt.app.secondmemory.configuration;

import com.github.benmanes.caffeine.cache.Caffeine;
import java.util.concurrent.TimeUnit;
import org.springframework.context.annotation.Bean;

public class CaffeineConfig {

  @Bean
  public Caffeine caffeineConfig() {
    return Caffeine.newBuilder().expireAfterWrite(120, TimeUnit.MINUTES).maximumWeight(2048);
  }
}