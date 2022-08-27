package app.api;

import com.sun.net.httpserver.Headers;
import lombok.Value;

@Value
public class ResponseEntity<T> {

    private final String body;
    private final Headers headers;
    private final StatusCode statusCode;
}