package mipt.app.secondmemory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@SpringBootApplication
@EnableCaching
@EnableMethodSecurity
public class SecondMemoryApplication {
  public static void main(String[] args) {
    SpringApplication.run(SecondMemoryApplication.class, args);
  }
}
