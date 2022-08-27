package app.api;

import app.domain.UserRoleDto;
import app.domain.user.UserService;
import app.errors.ApplicationExceptions;
import app.errors.GlobalExceptionHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import app.domain.role.Role;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class UserRoleHandler extends Handler {
    private final UserService userService;

    public UserRoleHandler(UserService userService, ObjectMapper objectMapper, GlobalExceptionHandler exceptionHandler) {
        super(objectMapper, exceptionHandler);
        this.userService = userService;
    }
    @Override
    protected void execute(HttpExchange exchange) throws IOException {
        byte[] response;
        if ("POST".equals(exchange.getRequestMethod())) {
            ResponseEntity e = addUserRole(exchange.getRequestBody());
            exchange.getResponseHeaders().putAll(e.getHeaders());
            exchange.sendResponseHeaders(e.getStatusCode().getCode(), 0);
            response = super.writeResponse(e.getBody());
        }
        else if ("DELETE".equals(exchange.getRequestMethod())) {
            ResponseEntity e = deleteUserRole(exchange.getRequestBody());
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

    private ResponseEntity deleteUserRole(InputStream is) {
        UserRoleDto userRoleDto = super.readRequest(is, UserRoleDto.class);

        String roleName = userRoleDto.getRolename();
        String userName = userRoleDto.getUsername();

        String response;
        try {
            userService.deleteRole(userName, roleName);
            response = String.format("Role %s removed from User %s", roleName, userName);
        } catch (Exception e) {
            response = e.getMessage();
        }

        return new ResponseEntity<>(response,
                getHeaders(Constants.CONTENT_TYPE, Constants.APPLICATION_JSON), StatusCode.OK);
    }

    private ResponseEntity addUserRole(InputStream is) {
        UserRoleDto userRoleDto = super.readRequest(is, UserRoleDto.class);

        String roleName = userRoleDto.getRolename();
        String userName = userRoleDto.getUsername();

        String response;

        try {
            userService.addRole(userName, roleName);
            response = String.format("Role %s added to User %s successfully", roleName, userName);
        } catch (Exception e) {
            response = e.getMessage();
        }

        return new ResponseEntity<>(response,
                getHeaders(Constants.CONTENT_TYPE, Constants.APPLICATION_JSON), StatusCode.OK);
    }
}
