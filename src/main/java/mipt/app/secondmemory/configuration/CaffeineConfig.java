package mipt.app.secondmemory.configuration;

import com.github.benmanes.caffeine.cache.Caffeine;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CaffeineConfig {

  @Bean
  public Caffeine createCaffeineCache(
      @Value("${mipt.app.caffeine_cache.expiration_time}") int expirationTime,
      @Value("${mipt.app.caffeine_cache.max_weight}") int maxWeight) {
    return Caffeine.newBuilder()
        .expireAfterWrite(expirationTime, TimeUnit.MINUTES)
        .maximumWeight(maxWeight);
  }
}
