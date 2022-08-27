package app.api;

import app.domain.role.Role;
import app.domain.user.User;
import app.domain.user.UserDto;
import app.domain.user.UserService;
import app.errors.ApplicationExceptions;
import app.errors.GlobalExceptionHandler;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.stream.Collectors;

public class LoginHandler extends Handler{
    private final UserService userService;

    public LoginHandler(UserService userService, ObjectMapper objectMapper,
                       GlobalExceptionHandler exceptionHandler) {
        super(objectMapper, exceptionHandler);
        this.userService = userService;
    }

    @Override
    protected void execute(HttpExchange exchange) throws IOException {
        byte[] response;
        if ("POST".equals(exchange.getRequestMethod())) {
            ResponseEntity e = login(exchange.getRequestBody());
            exchange.getResponseHeaders().putAll(e.getHeaders());
            exchange.sendResponseHeaders(e.getStatusCode().getCode(), 0);
            response = super.writeResponse(e.getBody());
        }
        else {
            throw ApplicationExceptions.methodNotAllowed(
                    "Method " + exchange.getRequestMethod() + " is not allowed for " + exchange.getRequestURI()).get();
        }

        OutputStream os = exchange.getResponseBody();
        os.write(response);
        os.close();
    }

    private ResponseEntity login(InputStream is) {
        UserDto userDto = super.readRequest(is, UserDto.class);

        User authenticatedUser = userService.authenticate(userDto.getUsername(), userDto.getPassword());
        String response;
        if (authenticatedUser != null) {
            String token = JWT.create()
                    .withSubject(userDto.getUsername())
                    .withExpiresAt(new Date(System.currentTimeMillis() + 2 * 60 * 1000))
                    .withClaim("roles", authenticatedUser.getRoles().stream().map(Role::getName).collect(Collectors.toList()))
                    .sign(Algorithm.HMAC256("secret".getBytes()));

            response = token;
        }
        else response = "Incorrect credentials!";

        return new ResponseEntity<>(response,
                getHeaders(Constants.CONTENT_TYPE, Constants.APPLICATION_JSON), StatusCode.OK);
    }
}
