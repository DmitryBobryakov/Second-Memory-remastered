package mipt.app.secondmemory.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import mipt.app.secondmemory.dto.RequestUserDto;
import mipt.app.secondmemory.dto.UserDto;
import mipt.app.secondmemory.entity.User;
import mipt.app.secondmemory.exception.AuthenticationDataMismatchException;
import mipt.app.secondmemory.exception.UserNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
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
      @RequestBody RequestUserDto user, HttpServletResponse response)
      throws UserNotFoundException, AuthenticationDataMismatchException;

  @Operation(summary = "Зарегистрировать пользователя по почте, имени и паролю")
  @ApiResponse(responseCode = "201", description = "Пользователь зарегистрирован")
  @ApiResponse(
      responseCode = "400",
      description = "BAD_REQUEST | Пользователь уже зарегистрирован",
      content = @Content)
  ResponseEntity<UserDto> registerUser(@RequestBody User user);

  @Operation(summary = "Изменить данные пользователя")
  @ApiResponse(responseCode = "200", description = "Данные о пользователе изменены")
  @ApiResponse(
      responseCode = "404",
      description = "NOT_FOUND | Пользователь с такими данными не найден",
      content = @Content)
  ResponseEntity<String> updateUser(@RequestBody User user, @CookieValue("data") String cookieValue)
      throws UserNotFoundException;

  @Operation(summary = "Удалить пользователя")
  @ApiResponse(responseCode = "200", description = "Пользователь удален")
  @ApiResponse(
      responseCode = "404",
      description = "NOT_FOUND | Пользователь с такими данными не найден",
      content = @Content)
  ResponseEntity<String> deleteUser(
      @PathVariable String username,
      @RequestBody String email,
      @CookieValue("data") String cookieValue)
      throws UserNotFoundException;

  @Operation(summary = "Выйти из аккаунта")
  @ApiResponse(responseCode = "200", description = "Пользователь вышел из аккаунта")
  ResponseEntity<String> logOut(@RequestBody String uuid, HttpServletResponse response)
      throws UserNotFoundException;
}
