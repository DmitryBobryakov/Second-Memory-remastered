package mipt.app.secondmemory.configuration;

import com.github.benmanes.caffeine.cache.Caffeine;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CaffeineConfig {

  private static int EXPIRATION_TIME;
  private static int MAX_WEIGHT;

  @Value("${mipt.app.caffeine_cache.expiration_time}")
  public void setExpirationTime(int expirationTime) {
    CaffeineConfig.EXPIRATION_TIME = expirationTime;
  }

  @Value("${mipt.app.caffeine_cache.max_weight}")
  public void setMaxWeight(int maxWeight) {
    CaffeineConfig.MAX_WEIGHT = maxWeight;
  }

  @Bean
  public Caffeine createCaffeineCache() {
    return Caffeine.newBuilder().expireAfterWrite(EXPIRATION_TIME, TimeUnit.MINUTES)
        .maximumWeight(MAX_WEIGHT);
  }
}