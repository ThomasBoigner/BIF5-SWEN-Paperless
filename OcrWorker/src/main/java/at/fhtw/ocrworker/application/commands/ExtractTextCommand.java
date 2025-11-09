package at.fhtw.ocrworker.application.commands;

import lombok.Builder;

@Builder
public record ExtractTextCommand(byte[] imageBytes) {
}
