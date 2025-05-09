package mipt.app.secondmemory.controller.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import mipt.app.secondmemory.dto.user.AuthUserRequest;
import mipt.app.secondmemory.dto.user.RegisterUserResponse;
import mipt.app.secondmemory.entity.User;
import mipt.app.secondmemory.exception.session.SessionNotFoundException;
import mipt.app.secondmemory.exception.user.AuthenticationDataMismatchException;
import mipt.app.secondmemory.exception.user.UserNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Users API", description = "Управление пользователями")
public interface UsersController {
  @Operation(summary = "Аутентифицировать пользователя по почте и паролю")
  @ApiResponse(
      responseCode = "200",
      description = "Пользователь аутентифицирован",
      content = @Content)
  @ApiResponse(
      responseCode = "401",
      description = "UNAUTHORIZED | Неверные данные для пользователя",
      content = @Content)
  @ApiResponse(
      responseCode = "404",
      description = "NOT_FOUND | Пользователь с такими данными не найден",
      content = @Content)
  ResponseEntity<String> authenticateUser(
      @RequestBody AuthUserRequest user, HttpServletResponse response)
      throws UserNotFoundException, AuthenticationDataMismatchException, JsonProcessingException;

  @Operation(summary = "Зарегистрировать пользователя по почте, имени и паролю")
  @ApiResponse(responseCode = "201", description = "Пользователь зарегистрирован")
  @ApiResponse(
      responseCode = "400",
      description = "BAD_REQUEST | Пользователь уже зарегистрирован",
      content = @Content)
  ResponseEntity<RegisterUserResponse> registerUser(@RequestBody User user)
      throws JsonProcessingException;

  @Operation(summary = "Изменить данные пользователя")
  @ApiResponse(responseCode = "200", description = "Данные о пользователе изменены")
  @ApiResponse(
      responseCode = "404",
      description = "NOT_FOUND | Пользователь с такими данными не найден",
      content = @Content)
  ResponseEntity<String> updateUser(
      @RequestBody User user, @CookieValue("token") String cookieValue)
      throws UserNotFoundException, SessionNotFoundException;

  @Operation(summary = "Удалить пользователя")
  @ApiResponse(responseCode = "200", description = "Пользователь удален")
  @ApiResponse(
      responseCode = "404",
      description = "NOT_FOUND | Пользователь с такими данными не найден",
      content = @Content)
  ResponseEntity<String> deleteUser(
      @RequestBody String email, @CookieValue("token") String cookieValue)
      throws UserNotFoundException, JsonProcessingException;

  @Operation(summary = "Выйти из аккаунта")
  @ApiResponse(responseCode = "200", description = "Пользователь вышел из аккаунта")
  ResponseEntity<String> logOut(@CookieValue("token") String cookieValue, HttpServletResponse response)
      throws UserNotFoundException, SessionNotFoundException;
}
