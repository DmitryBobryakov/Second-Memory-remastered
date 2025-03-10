package mipt.app.secondmemory.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Schema(name = "File", description = "Сущность Файла")
public class File {

    @NotNull(message = "File name have to be filled")
    @Setter
    @Schema(description = "Имя файла", example = "Red Hat.png", type = "String")
    private String name;

    @NotNull(message = "File capacity have to be filled")
    @Schema(description = "Размер файла в байтах", example = "1024", type = "int")
    private int capacity;


    protected File() {
    }

    public File(String name, int capacity) {
        this.name = name;
        this.capacity = capacity;
    }
}
