package mipt.app.secondmemory.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "buckets")
@Getter
@Schema(name = "Bucket", description = "Сущность корневой папки")
@Builder(toBuilder = true)
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BucketEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Schema(description = "Название корневой папки", example = "root", type = "String")
  @Column(name = "name", length = 30)
  private String name;
}
