package at.fhtw.ocrworker.application.commands;

import lombok.Builder;

import java.util.UUID;

@Builder
public record ExtractTextCommand(byte[] imageBytes, UUID fileToken) {
}
