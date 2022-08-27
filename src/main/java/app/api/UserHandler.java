package app.api;

import app.domain.user.User;
import app.errors.ApplicationExceptions;
import app.errors.GlobalExceptionHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import app.domain.user.UserDto;
import app.domain.user.UserService;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class UserHandler extends Handler {

    private final UserService userService;

    public UserHandler(UserService userService, ObjectMapper objectMapper,
                       GlobalExceptionHandler exceptionHandler) {
        super(objectMapper, exceptionHandler);
        this.userService = userService;
    }

    @Override
    protected void execute(HttpExchange exchange) throws IOException {
        byte[] response;
        if ("POST".equals(exchange.getRequestMethod())) {
            ResponseEntity e = createUser(exchange.getRequestBody());
            exchange.getResponseHeaders().putAll(e.getHeaders());
            exchange.sendResponseHeaders(e.getStatusCode().getCode(), 0);
            response = super.writeResponse(e.getBody());
        }
        else if ("DELETE".equals(exchange.getRequestMethod())) {
            ResponseEntity e = deleteUser(exchange.getRequestBody());
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

    private ResponseEntity deleteUser(InputStream requestBody) {
        UserDto userRequest = super.readRequest(requestBody, UserDto.class);
        String username = userRequest.getUsername();

        String response;
        if (userService.deleteUser(username))
            response = "User deleted successfully";
        else response = "User not found";

        return new ResponseEntity<>(response,
                getHeaders(Constants.CONTENT_TYPE, Constants.APPLICATION_JSON), StatusCode.OK);
    }

    private ResponseEntity createUser(InputStream is) {
        UserDto userDto = super.readRequest(is, UserDto.class);

        User user = userService.createUser(userDto);
        String response;
        if (user != null)
            response = "User created successfully";
        else response = "User already exists";

        return new ResponseEntity<>(response,
            getHeaders(Constants.CONTENT_TYPE, Constants.APPLICATION_JSON), StatusCode.OK);
    }

}
