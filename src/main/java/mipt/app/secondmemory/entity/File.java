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

import java.sql.Timestamp;


@Entity
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "files_info")
@Schema(name = "File", description = "Сущность Файла")
public class File {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 50)
    @NotNull(message = "File name have to be filled")
    @Schema(description = "Имя файла", example = "Red Hat.png", type = "String")
    private String name;

    @Column(name = "capacity", nullable = false)
    @NotNull(message = "File capacity have to be filled")
    @Schema(description = "Размер файла в байтах", example = "1024", type = "long")
    private long capacity;

    @Schema(description = "Id пользователя, который создал данный файл", example = "1", type = "Long")
    @Column(name = "owner_id")
    private Long ownerId;


    @Schema(description = "Id бакета, в котором хранится данный файл", example = "12", type = "Long")
    @Column(name = "bucket_id")
    private Long bucketId;

    @Schema(description = "Дата создания файла", type = "Timestamp")
    @Column(name = "creation_date")
    private Timestamp creationDate;

    @Schema(description = "Последняя дата обновления файла", type = "Timestamp")
    @Column(name = "last_modified_date")
    private Timestamp lastModifiedDate;
}
