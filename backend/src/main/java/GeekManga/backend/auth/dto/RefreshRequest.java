package GeekManga.backend.auth.dto;

import jakarta.validation.constraints.NotBlank;

public record RefreshRequest(@NotBlank(message = "Falta el refreshToken") String refreshToken) {
}
