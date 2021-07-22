package pl.janekosa.nn.homework.api;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class ErrorResponse {
    private final int status;
    private final String description;
    private final String message;
}
