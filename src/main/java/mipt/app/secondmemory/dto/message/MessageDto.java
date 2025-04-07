package mipt.app.secondmemory.dto.message;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MessageDto {
  String email;
  String username;
  MessageType type;
}
