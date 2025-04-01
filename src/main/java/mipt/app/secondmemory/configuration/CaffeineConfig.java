package mipt.app.secondmemory.configuration;

import com.github.benmanes.caffeine.cache.Caffeine;
import java.util.concurrent.TimeUnit;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CaffeineConfig {

  @Bean
  public Caffeine createCaffeineCache() {
    return Caffeine.newBuilder().expireAfterWrite(120, TimeUnit.MINUTES).maximumWeight(2048);
  }
}