package mipt.app.secondmemory.entity;

import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;

@Getter
@Setter
public class FileTagId implements Serializable {
    private Long fileId;
    private Long TagId;
}
