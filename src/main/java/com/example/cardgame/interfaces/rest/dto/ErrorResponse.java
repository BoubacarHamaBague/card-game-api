package com.example.cardgame.interfaces.rest.dto;

import com.example.cardgame.domain.util.ErrorCode;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Error details returned when a request fails")
public record ErrorResponse(
    @Schema(description = "Human-readable error message", example = "Game not found: abc-123") String error,
    @Schema(description = "Machine-readable error code", example = "GAME_NOT_FOUND") String errorCode
) {
    public static ErrorResponse from(Exception ex, ErrorCode code) {
        String codeStr = (code != null) ? code.name() : null;
        return new ErrorResponse(ex.getMessage(), codeStr);
    }
}
