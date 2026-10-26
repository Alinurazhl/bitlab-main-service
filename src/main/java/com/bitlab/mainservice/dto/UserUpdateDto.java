package com.bitlab.mainservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Данные для обновления профиля и пароля")
public class UserUpdateDto {
    private String firstName;
    private String lastName;
    private String email;
    private String password; // Если не пустое — сбрасываем пароль
    private String role;     // Желаемая роль (изменение доступно только ADMIN)
}
