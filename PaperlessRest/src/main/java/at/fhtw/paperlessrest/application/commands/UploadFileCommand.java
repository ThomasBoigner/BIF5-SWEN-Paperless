package at.fhtw.paperlessrest.application.commands;

import lombok.Builder;
import org.jspecify.annotations.Nullable;

@Builder
public record UploadFileCommand(
        @Nullable String description
) { }
