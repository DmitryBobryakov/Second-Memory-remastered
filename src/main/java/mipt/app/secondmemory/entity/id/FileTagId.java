package mipt.app.secondmemory.entity.id;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FileTagId implements Serializable {
  private Long fileId;
  private Long tagId;
}
