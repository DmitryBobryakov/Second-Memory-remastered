package mipt.app.secondmemory.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DtoMessage {
  String email;
  String username;
  MessageType type;
}
